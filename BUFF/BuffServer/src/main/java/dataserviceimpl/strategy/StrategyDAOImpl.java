package dataserviceimpl.strategy;

import blserviceimpl.strategy.BackData;
import blserviceimpl.strategy.NewPickleData;
import blserviceimpl.strategy.PickleData;
import dataservice.strategy.StrategyDAO;
import pick.PickStockService;
import pick.PickStockServiceImpl;
import po.StockPO;
import po.StockPoolConditionPO;
import stockenum.StockPool;
import stockenum.StrategyType;
import vo.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by wshwbluebird on 2017/3/26.
 */
public enum StrategyDAOImpl implements StrategyDAO {
    STRATEGY_DAO ;



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



    @Override
    public List<NewPickleData> getNewPickleData(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexVOs) {
        if(this.stocksInPool==null){
            //throw new NoStockInPoolException();
            stocksInPool = getStocksInPool(new StockPoolConditionPO(stockPoolConditionVO));
        }


        List<String> codePool = stocksInPool;


        return strategyConditionVO.strategyType.setAllValue
                (codePool,strategyConditionVO.beginDate,strategyConditionVO.endDate,stockPickIndexVOs);
    }


    /**
     * 这个方法 必须在获取股票池之后使用
     * @param beginDate
     * @param endDate
     * @param stockPoolConditionVO
     * @param stockPickIndexVOs
     * @param traceBackVO
     * @param mixedStrategyVOS
     * @return
     */
    @Override
    public List<PickleData> getPickleData(LocalDate beginDate , LocalDate endDate,
                                          StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexVOs,
                                          TraceBackVO traceBackVO, List<MixedStrategyVO> mixedStrategyVOS) {

        List<String> codePool = getStocksInPool(new StockPoolConditionPO(stockPoolConditionVO));

        List<PickleData> pickleDatas = pickStockService.seprateDaysByTrade(beginDate,endDate,traceBackVO.holdingPeriod);

        for(String code : codePool){
            List<StockPO> stockPOs = pickStockService
                    .getSingleCodeInfo(code, beginDate.minusDays(10), endDate.plusDays(10));
            //  记录  stockPOs  的指针
            int k = 0 ;
            for (int i = 0; i < pickleDatas.size(); i++) {
                PickleData pickleData = pickleDatas.get(i);
                /**
                 * 查看在持有期是否   出现停牌
                 */
                boolean isStop = false; //是否停牌
                double firstDayOpen;
                double lastDayClose;
                while (stockPOs.get(k).getDate().isBefore(pickleDatas.get(i).beginDate)) {
                    k++;
                }
                if (k == 0) {
                    continue;
                }
                firstDayOpen = stockPOs.get(k - 1).getAdjCloseIndex();
                while (stockPOs.get(k).getDate().isBefore(pickleDatas.get(i).endDate)) {
                    if (stockPOs.get(k).getVolume() == 0) {
                        isStop = true;
                        break;
                    }
                    k++;
                }
                if (stockPOs.get(k).getDate().isAfter(pickleDatas.get(i).endDate))
                    k--;
                lastDayClose = stockPOs.get(k).getAdjCloseIndex();

                //如果没有停牌 则加入可以进行进一步筛选和排序的队列
                if (!isStop) {
                    pickleData.stockCodes.add(new BackData(code, 0.0, firstDayOpen, lastDayClose));
                }
            }

            //加载排序参数
            for(MixedStrategyVO mixedStrategyVO : mixedStrategyVOS){
                pickleDatas = mixedStrategyVO.strategyType.setRankValue
                        (pickleDatas,code,beginDate,endDate,traceBackVO.formationPeriod);
            }
            //加载过滤参数

            for(StockPickIndexVO stockPickIndexVO : stockPickIndexVOs){
                pickleDatas = stockPickIndexVO.stockPickIndex.setFilterValue(pickleDatas,code);
            }


        }


        //归一化
        pickleDatas = normalization(pickleDatas,mixedStrategyVOS);


        //过滤和比较
        for (int i = 0; i < pickleDatas.size(); i++) {
            PickleData pickleData = pickleDatas.get(i);
            LocalDate begin = pickleData.beginDate;
            LocalDate end = pickleData.endDate;

            pickleData.stockCodes = pickleData.stockCodes.stream()
                    .filter(getPredictAll(stockPickIndexVOs, begin, end)) //根据所有条件过滤
                    .sorted((o1,o2)->{
                        if ((double) o1.rankValue > (double) o2.rankValue) return -1;
                        else return 1;
                    })
                    .limit(traceBackVO.holdingNum)
                    .collect(Collectors.toList());
        }



        //TODO计算基本收益率


        for(PickleData pickleData : pickleDatas) {
            double sum = 0.0;
            double buyMoney = 0;
            double sellMoney = 0;

            for(BackData backData : pickleData.stockCodes) {
                // sum += (backData.lastDayClose - backData.firstDayOpen) / backData.firstDayOpen;
                double cnt = 100/ backData.firstDayOpen;
                buyMoney+=100;
                sellMoney+= cnt * backData.lastDayClose;
                sum += (sellMoney-buyMoney)/buyMoney;
            }
            pickleData.baseProfitRate = sum / pickleData.stockCodes.size();
        }


        return pickleDatas;
    }


    @Override
    public List<PickleData> rankAndFilterPickleData(
            List<PickleData> pickleDataList, List<StockPickIndexVO> stockPickIndexVOs,
            StrategyType strategyType , int holdingNum , double holdingRate, boolean asd) {
        if(holdingRate != 0) {
            //在每个区间内 确定有效的股票
            for (int i = 0; i < pickleDataList.size(); i++) {
                PickleData pickleData = pickleDataList.get(i);
                LocalDate begin = pickleData.beginDate;
                LocalDate end = pickleData.endDate;
                holdingNum = (int)Math.ceil(holdingRate * pickleData.stockCodes.size());
//                if(i == 0) {
//                    System.out.println(pickleData.stockCodes.size());
//                    for(BackData backData : pickleData.stockCodes) {
//                        System.out.println(pickleData.beginDate + "   " + pickleData.endDate);
//                        System.out.println(backData.code + "   " + backData.rankValue);
//                    }
//                }
                pickleData.stockCodes = pickleData.stockCodes.stream()
                        .filter(getPredictAll(stockPickIndexVOs, begin, end)) //根据所有条件过滤
                        .sorted(strategyType            //根据rank模式进行排序
                                .getCompareRank(asd))
                        .limit(holdingNum)
                        .collect(Collectors.toList());
//                if(i == 0) {
//                    System.out.println(pickleData.stockCodes.size());
//                    for(BackData backData : pickleData.stockCodes) {
//                        System.out.println(pickleData.beginDate + "   " + pickleData.endDate);
//                        System.out.println(backData.code + "   " + backData.rankValue);
//                    }
//                }
            }

        }
        else {
            //在每个区间内 确定有效的股票
            for (int i = 0; i < pickleDataList.size(); i++) {
                PickleData pickleData = pickleDataList.get(i);
                LocalDate begin = pickleData.beginDate;
                LocalDate end = pickleData.endDate;

                pickleData.stockCodes = pickleData.stockCodes.stream()
                        .filter(getPredictAll(stockPickIndexVOs, begin, end)) //根据所有条件过滤
                        .sorted(strategyType            //根据rank模式进行排序
                                .getCompareRank(asd))
                        .limit(holdingNum)
                        .collect(Collectors.toList());
            }
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

        //System.out.println(stockPickIndexVOs.size());

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
     * 对于混合策略 排序参数的线性归一化
     * @param pickleDatas
     * @param mixedStrategyVOs
     * @return
     */
    private List<PickleData> normalization(List<PickleData> pickleDatas, List<MixedStrategyVO> mixedStrategyVOs){
        for(int i = 0 ; i < pickleDatas.size() ;i++){
            PickleData pickleData = pickleDatas.get(i);
            for (MixedStrategyVO mixedStrategyVO: mixedStrategyVOs){
                double max,min;

                DoubleSummaryStatistics doubleSummaryStatistics = pickleData.stockCodes.stream()
                        .mapToDouble(t->(double)t.mixRank[mixedStrategyVO.strategyType.ordinal()])
                        .summaryStatistics();


                max = doubleSummaryStatistics.getMax();
                min = doubleSummaryStatistics.getMin();


                pickleData.stockCodes = pickleData.stockCodes.stream().map(t->{
                    double x = (double)t.mixRank[mixedStrategyVO.strategyType.ordinal()];
                    double y = (double)t.rankValue;
                    t.rankValue =y+ mixedStrategyVO.weight*(x-min)/(max-min);
                    if(mixedStrategyVO.asc)  t.rankValue = -(double)t.rankValue;
                    return t;
                }).collect(Collectors.toList());
            }
        }
        return pickleDatas;
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
