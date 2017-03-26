package dataserviceimpl.strategy;

import blserviceimpl.strategy.PickleData;
import dataservice.strategy.StrategyDAO;
import po.StockPoolConditionPO;
import stockenum.StockPool;
import vo.StockPickIndexVO;
import vo.StockPoolConditionVO;
import vo.StrategyConditionVO;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by wshwbluebird on 2017/3/26.
 */
public enum  StrategyDAOImpl implements StrategyDAO {
    STRATEGY_DAO ;

    /**
     * 说一个严重的问题    股票代码没有统一格式        000001 和 1不一样
     * 我暂时在这里初步解决一下
     * @param stockPoolConditionPO 筛选股票池的条件参数
     * @return
     */
    @Override
    public List<String> getStocksInPool(StockPoolConditionPO stockPoolConditionPO) {
        //是否为全部股票
        if(stockPoolConditionPO.getStockPool().equals(StockPool.All)){
            return readAllList(stockPoolConditionPO.isExcludeST());
        }

        //是否为沪深300  暂时把沪深300 也当作全部股票
        if(stockPoolConditionPO.getStockPool().equals(StockPool.HS300)){
            return readAllList(stockPoolConditionPO.isExcludeST());
        }
        final boolean  excludeST = stockPoolConditionPO.isExcludeST();
        //usernode
        List<String>  codeList  = new ArrayList<>();
        List<String>  blockList  = new ArrayList<>();
        List<String>  industryList  = new ArrayList<>();
        HashSet<String>  block = (HashSet<String>) stockPoolConditionPO.getBlock();
        HashSet<String>  industry = (HashSet<String>) stockPoolConditionPO.getIndustry();

        blockList = block.parallelStream().map(t->readFromBlock(t,excludeST)).
                flatMap(t->t.stream()).collect(Collectors.toList());


        industryList = industry.parallelStream().map(t->readFromIndustry(t,excludeST)).
                flatMap(t->t.stream()).collect(Collectors.toList());

        codeList.addAll(blockList);
        codeList.addAll(industryList);
        codeList = codeList.parallelStream().distinct().collect(Collectors.toList());

        return  codeList;
    }

    @Override
    public List<PickleData> getPickleData(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexVOs) {
        return null;
    }


    /**
     * 读取该板块所有符合条件的股票代码
     * @param blockName
     * @return 该文件内部所有的 stock code
     */
    private static List<String> readFromBlock(String blockName, boolean excludeST){
        final  String pref = "../Data/Block/";
        String pathOfFile = pref+blockName+".csv";

        List<String> codelist = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(pathOfFile), Charset.forName("UTF-8"))) {
            List<String> list = br.lines().collect(Collectors.toList());
            codelist = list.parallelStream().map(t->{
                String[] codeAndName = t.split(",");
                if(excludeST&&(codeAndName[1].startsWith("*ST") || codeAndName[1].startsWith("ST")))
                    return "ST";
                else
                    return codeAndName[0];
            }).filter(t->!t.equals("ST")).map(t->{
                while (t.startsWith("0")){
                    t = t.substring(1,t.length());
                }
                return t;
            }).collect(Collectors.toList());

        } catch (IOException e) {
            System.err.println(pathOfFile +" is not exits");
        }finally {
            return codelist;
        }
    }

    /**
     * 读取该行业所有符合条件的股票代码
     * @param industryName
     * @return 该文件内部所有的 stock code
     */
    private   static   List<String> readFromIndustry(String industryName ,boolean excludeST){
        final  String pref = "../Data/Industry/";
        String pathOfFile = pref+industryName+".csv";

        List<String> codelist = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(pathOfFile), Charset.forName("UTF-8"))) {
            List<String> list = br.lines().collect(Collectors.toList());
            codelist = list.parallelStream().map(t->{
                String[] codeAndName = t.split(",");
                if(excludeST&&(codeAndName[1].startsWith("*ST") || codeAndName[1].startsWith("ST")))
                    return "ST";
                else
                    return codeAndName[0];
            }).filter(t->!t.equals("ST")).map(t->{
                while (t.startsWith("0")){
                    t = t.substring(1,t.length());
                }
                return t;
            }).collect(Collectors.toList());

        } catch (IOException e) {
            System.err.println(pathOfFile +" is not exits");
        }finally {
            return codelist;
        }
    }

    private   static   List<String> readAllList(boolean excludeST){
        final  String pref = "../Data/StockMap";
        String pathOfFile = pref+".csv";

        List<String> codelist = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(pathOfFile), Charset.forName("UTF-8"))) {
            List<String> list = br.lines().collect(Collectors.toList());
            codelist = list.parallelStream().map(t->{
                String[] codeAndName = t.split(",");
                if(excludeST&&(codeAndName[1].startsWith("*ST") || codeAndName[1].startsWith("ST")))
                    return "ST";
                else
                    return codeAndName[0];
            }).filter(t->!t.equals("ST")).map(t->{
                while (t.startsWith("0")){
                    t = t.substring(1,t.length());
                }
                return t;
            }).collect(Collectors.toList());

        } catch (IOException e) {
            System.err.println(pathOfFile +" is not exits");
        }finally {
            return codelist;
        }
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
