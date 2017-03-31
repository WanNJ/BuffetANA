package dataserviceimpl.strategy;

import blserviceimpl.strategy.BackData;
import blserviceimpl.strategy.PickleData;
import dataservice.strategy.StrategyDAO;
import pick.PickStockService;
import pick.PickStockServiceImpl;
import po.StockPoolConditionPO;
import stockenum.StockPool;
import util.RunTimeSt;
import vo.StockPickIndexVO;
import vo.StockPoolConditionVO;
import vo.StrategyConditionVO;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by wshwbluebird on 2017/3/26.
 */
public enum StrategyDAOImpl implements StrategyDAO {
    STRATEGY_DAO;


    private PickStockService pickStockService;
    private List<String> stocksInPool;

    StrategyDAOImpl() {
        this.pickStockService = PickStockServiceImpl.PICK_STOCK_SERVICE;
    }


    /**
     * 外部注入 pickStockService
     *
     * @param pickStockService
     */
    public void setPickStockService(PickStockService pickStockService) {
        this.pickStockService = pickStockService;
    }

    /**
     * 说一个严重的问题    股票代码没有统一格式        000001 和 1不一样
     * 我暂时在这里初步解决一下
     * 沪深300的股票池方法暂时不能调用，我已经将其修改为就是沪深300那300支实际的股票，不再是所有股票，一旦调用会报错（切记！！！！）
     * 等后期我们加进去了沪深300的其他股票，调用该方法的沪深300股票池时才不会报错！！！！
     * @param stockPoolConditionPO 筛选股票池的条件参数
     * @return
     */
    @Override
    public List<String> getStocksInPool(StockPoolConditionPO stockPoolConditionPO) {
        //是否为全部股票
        if (stockPoolConditionPO.getStockPool().equals(StockPool.All)) {
            return readAllList(stockPoolConditionPO.isExcludeST());
        }

        //是否为沪深300  暂时把沪深300 也当作全部股票
        /*
        修改 by TY
        增加了获取沪深300的股票的代码，沪深300中的股票没有ST类型的所以这个isExcludeST参数被我删除了
        不过由于老师给的数据没有涵盖沪深300所有的数据，所以这个方法暂时用不到
         */
        //TODO 在界面层记得当用户选择了沪深300时，要将是否排除ST那个选项置为不排除，且设为不可编辑
        if (stockPoolConditionPO.getStockPool().equals(StockPool.HS300)) {
            return readHS300List();
        }
        final boolean excludeST = stockPoolConditionPO.isExcludeST();
        //usernode
        List<String> codeList = new ArrayList<>();
        List<String> blockList;
        List<String> industryList;

        HashSet<String> block = (HashSet<String>) stockPoolConditionPO.getBlock();
        HashSet<String> industry = (HashSet<String>) stockPoolConditionPO.getIndustry();

        blockList = block.parallelStream().map(t -> readFromBlock(t, excludeST)).
                flatMap(t -> t.stream()).collect(Collectors.toList());


        industryList = industry.parallelStream().map(t -> readFromIndustry(t, excludeST)).
                flatMap(t -> t.stream()).collect(Collectors.toList());

        codeList.addAll(blockList);
        codeList.addAll(industryList);
        codeList = codeList.parallelStream().distinct().collect(Collectors.toList());

        this.stocksInPool = codeList;
        return codeList;
    }

    /**
     * 这个方法必须在getStocksInPool之后调用！！！Attention！！！！！
     * @param strategyConditionVO
     * @param stockPoolConditionVO
     * @param stockPickIndexVOs
     * @return
     */
    @Override
    public List<PickleData> getPickleData(StrategyConditionVO strategyConditionVO,
                                          StockPoolConditionVO stockPoolConditionVO,
                                          List<StockPickIndexVO> stockPickIndexVOs) {

        /**
         * TODO
         * add by wsw
         * 我觉得可以 加一个提醒 比如stocksInPool 为空时 抛出异常 或者直接调用
         */
        if(this.stocksInPool==null){
            //throw new NoStockInPoolException();
            stocksInPool = getStocksInPool(new StockPoolConditionPO(stockPoolConditionVO));
        }

        /**
        修改 BY TY
        避免重复调用getStocksInPool方法，减少一次读文件的次数
        这样就必须保证该方法必须在getStocksInPool方法之后调用
        本来腌制数据也必须应该在股票池数据确定下来后才能进行腌制，所以从逻辑上讲，也应该是这么个顺序
        调用时，切记要在getStocksInPool之后调用，测试该方法时尤其要注意！！！！！
         */
        List<String> codePool = stocksInPool;




        //首先分割天数
        List<PickleData> pickleDatas =
                pickStockService.seprateDaysinCommon(strategyConditionVO.beginDate
                        , strategyConditionVO.endDate, strategyConditionVO.holdingPeriod);
        /**
         * 已经注入好了要比较的信息
         */
        List<PickleData> pickleDataList =
                strategyConditionVO.strategyType.setRankValue(pickleDatas, codePool
                        , strategyConditionVO.beginDate, strategyConditionVO.endDate
                        , strategyConditionVO.formationPeriod,stockPickIndexVOs);

        RunTimeSt.getRunTime("注入完成");

        //在每个区间内 确定有效的股票
        for (int i = 0; i < pickleDataList.size(); i++) {
            PickleData pickleData = pickleDatas.get(i);
            LocalDate begin = pickleData.beginDate;
            LocalDate end = pickleData.endDate;

            pickleData.stockCodes = pickleData.stockCodes.stream()
                    .filter(getPredictAll(stockPickIndexVOs, begin, end)) //根据所有条件过滤
                    .sorted(strategyConditionVO.strategyType            //根据rank模式进行排序
                            .getCompareRank(strategyConditionVO.asd))
                    .limit(strategyConditionVO.holdingNum)
                    .collect(Collectors.toList());
        }


        //返回已经排好序 决定后的要买的股票代码
        return pickleDataList;
    }


    /**
     * 读取该板块所有符合条件的股票代码
     *
     * @param blockName
     * @return 该文件内部所有的 stock code
     */
    private static List<String> readFromBlock(String blockName, boolean excludeST) {
        final String pref = "../Data/Block/";
        String pathOfFile = pref + blockName + ".csv";

        List<String> codelist = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(pathOfFile), Charset.forName("UTF-8"))) {
            List<String> list = br.lines().collect(Collectors.toList());
            codelist = list.parallelStream().map(t -> {
                String[] codeAndName = t.split(",");
                if (excludeST && (codeAndName[1].startsWith("*ST") || codeAndName[1].startsWith("ST")))
                    return "ST";
                else
                    return codeAndName[0];
            }).collect(Collectors.toList());

        } catch (IOException e) {
            System.err.println(pathOfFile + " is not exits");
        } finally {
            return codelist;
        }
    }

    /**
     * 读取该行业所有符合条件的股票代码
     *
     * @param industryName
     * @return 该文件内部所有的 stock code
     */
    private static List<String> readFromIndustry(String industryName, boolean excludeST) {
        final String pref = "../Data/Industry/";
        String pathOfFile = pref + industryName + ".csv";

        List<String> codelist = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(pathOfFile), Charset.forName("UTF-8"))) {
            List<String> list = br.lines().collect(Collectors.toList());
            codelist = list.parallelStream().map(t -> {
                String[] codeAndName = t.split(",");
                if (excludeST && (codeAndName[1].startsWith("*ST") || codeAndName[1].startsWith("ST")))
                    return "ST";
                else
                    return codeAndName[0];
            }).filter(t -> !t.equals("ST")).map(t -> {
                while (t.startsWith("0")) {
                    t = t.substring(1, t.length());
                }
                return t;
            }).collect(Collectors.toList());

        } catch (IOException e) {
            System.err.println(pathOfFile + " is not exits");
        } finally {
            return codelist;
        }
    }

    private static List<String> readAllList(boolean excludeST) {
        final String pref = "../Data/StockMap";
        String pathOfFile = pref + ".csv";

        List<String> codeList = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(pathOfFile), Charset.forName("UTF-8"))) {
            List<String> list = br.lines().collect(Collectors.toList());
            codeList = list.parallelStream().map(t -> {
                String[] codeAndName = t.split(",");
                if (excludeST && (codeAndName[0].startsWith("*ST") || codeAndName[0].startsWith("ST")))
                    return "ST";
                else
                    return codeAndName[1];
            }).filter(t -> !t.equals("ST")).map(t -> {
                //TODO 这一步没看懂是干嘛的，名字为什么会以0开头？
                while (t.startsWith("0")) {
                    t = t.substring(1, t.length());
                }
                return t;
            }).collect(Collectors.toList());

        } catch (IOException e) {
            System.err.println(pathOfFile + " is not exits");
        } finally {
            return codeList;
        }
    }

    private static List<String> readHS300List() {
        final String pref = "../Data/HS300";
        String pathOfFile = pref + ".csv";

        List<String> codeList = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(pathOfFile), Charset.forName("UTF-8"))) {
            List<String> list = br.lines().collect(Collectors.toList());
            codeList = list.parallelStream().map(t -> {
                String[] codeAndName = t.split(",");
                return codeAndName[1];
            }).filter(t -> !t.equals("ST")).map(t -> {
                //TODO 这一步没看懂是干嘛的，名字为什么会以0开头？
                while (t.startsWith("0")) {
                    t = t.substring(1, t.length());
                }
                return t;
            }).collect(Collectors.toList());

        } catch (IOException e) {
            System.err.println(pathOfFile + " is not exits");
        } finally {
            return codeList;
        }
    }


    /**
     * 获取全部的过滤器
     *
     * @param stockPickIndexVOs
     * @param begindate
     * @param endDate
     * @return
     */
    private Predicate<BackData> getPredictAll(List<StockPickIndexVO> stockPickIndexVOs
            , LocalDate begindate, LocalDate endDate) {
        Predicate<BackData> predicateAll = new Predicate<BackData>() {
            @Override
            public boolean test(BackData s) {
                for (StockPickIndexVO vo:stockPickIndexVOs) {

                    boolean cur =
                            vo.stockPickIndex.getFilter(vo.lowerBound,vo.upBound).test(s);

                    //如果检测结果不符合要求  直接 返回false
                    if(!cur)  return false;
                }


                return true;
            }
        };

        return predicateAll;
    }


    /**
     * 偷偷用来做测试的东西 233333
     */
//    public static void main(String[] args)  {
////
//        Set<String> block =  new HashSet<>();
//        block.add("LED");
//        block.add("LED");
//        //block.add("IPV6");
//
//
//        Set<String>  ind =  new HashSet<>();
//        //ind.add("电子元器件");
//        //ind.add("电子设备");
//        StockPoolConditionPO stockPoolConditionPO  =  new StockPoolConditionPO(StockPool.UserMode,
//                block,ind,true);
//
//        StrategyDAOImpl.STRATEGY_DAO.getStocksInPool(stockPoolConditionPO).
//                stream().forEach(t-> System.out.println(t));
//
//    }

}
