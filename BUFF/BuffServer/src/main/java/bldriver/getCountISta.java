package bldriver;

import dataservice.singlestock.StockDAO;
import dataserviceimpl.singlestock.StockDAOImpl;
import dataserviceimpl.strategy.StrategyDAOImpl;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;
import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;
import po.StockPO;
import po.StockPoolConditionPO;
import stockenum.StockPool;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Created by wshwbluebird on 2017/4/12.
 */
public class getCountISta {

     DAOFactoryService daoFactoryService = new DAOFactoryServiceImpl();

    static int cntg = 0 ;
    static int cntb = 0;

    public static void main(String[] args){

        StrategyDAOImpl strategyDAO = StrategyDAOImpl.STRATEGY_DAO;
        List<String> codeName = strategyDAO.getStocksInPool(new StockPoolConditionPO(StockPool.All,null,null,false));
        getCountISta sta = new getCountISta();
        for(String str: codeName){
            System.out.println(str);
            sta.calK(str);
        }

        System.out.println("good:  "+cntg);
        System.out.println("bad:  "+cntb);


    }


    private void calK(String code){
        StockDAO stockDAO = StockDAOImpl.STOCK_DAO_IMPL;
        List<StockPO> list = stockDAO.getStockInFoInRangeDate(code, LocalDate.of(2012,1,1),LocalDate.of(2014,1,1));
        //list.forEach(t-> System.out.println(t.getDate()));
        List<Double>  doubleList = list.stream().filter(t->t.getVolume()>0).map(t->t.getAdjCloseIndex()).collect(Collectors.toList());
        //doubleList.forEach(t-> System.out.println(t));
        if(doubleList.size()<50)  return;
        List<RangeF> rangeFList = getDep(doubleList,30);
        List<Double> values= rangeFList.stream().map(t->(double)t.cnt).collect(Collectors.toList());
        Kurtosis kurtosis = new Kurtosis();

        double[]  vv = new double[values.size()];
        for (int i = 0 ; i < values.size() ; i++){
            vv[i] = values.get(i);
        }
        double kk = kurtosis.evaluate(vv);
        System.out.println(code+"  "+  kk);
        if(kk>= 0 ) cntg++;
        else cntb++;

    }


    private List<RangeF> getDep( List<Double>  doubleList , int sep){
        DoubleSummaryStatistics summaryStatistics = doubleList.stream().mapToDouble(t->(double)t).summaryStatistics();
        double max = summaryStatistics.getMax();
        double min = summaryStatistics.getMin();

        double iterval =  (max-min)/sep;
        List<RangeF> rangeFList=  new ArrayList<>();
        double beg, end;
        beg = min;
        end = min+iterval;
        for(;end<=max; beg = end ,end+=iterval){
            rangeFList.add(new RangeF(beg,end));
        }
        //System.out.println(rangeFList.size());


        rangeFList = rangeFList.stream().map(t->{
            long cnt = doubleList.stream().filter(d->d>=t.small&& d<=t.big).count();
            t.cnt = cnt;
            return t;
        }).collect(Collectors.toList());


        //rangeFList.stream().forEach(t-> System.out.println(t.small+"  "+t.big+"  "+t.cnt));


        return rangeFList;
    }




}
