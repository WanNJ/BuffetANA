package bldriver;

import dataserviceimpl.strategy.StategyHistoryDAOImpl;
import dataserviceimpl.strategy.StrategyDAOImpl;
import po.*;
import stockenum.StockPickIndex;
import stockenum.StockPool;
import stockenum.StrategyType;
import vo.MixedStrategyVO;
import vo.StockPickIndexVO;
import vo.StockPoolConditionVO;
import vo.StrategyConditionVO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/4/17.
 */
public class SaveStrateguDriver {

    public static void main(String[] args) {
        SaveStrateguDriver obj = new SaveStrateguDriver();
        StategyHistoryDAOImpl stategyHistoryDAO = StategyHistoryDAOImpl.STATEGY_HISTORY_DAO;
//
//        StrategyConditionPO strategyConditionVO1 = new StrategyConditionPO(null
//                , LocalDate.of(2013,1,1).toString(),LocalDate.of(2014,1,1).toString(),true);
////        strategyConditionVO1.holdingRate = 0.2;
//        StockPoolConditionPO stockPoolConditionVO1  =new StockPoolConditionPO(StockPool.All,null,null,false);
//        List<StockPickIndexPO> stockPickIndexVOs1 = new ArrayList<>();
//        List<MixedStrategyPO> mixedStrategyVOs = new ArrayList<>();
//        stockPickIndexVOs1.add(new StockPickIndexPO(StockPickIndex.PREVIOUS_DAY_VOL,10.0,100.0));
//
//        mixedStrategyVOs.add(new MixedStrategyPO(StrategyType.MA,10,false,20));
////        mixedStrategyVOs.add(new MixedStrategyVO(StrategyType.MOM,1,false,20));
//        mixedStrategyVOs.add(new MixedStrategyPO(StrategyType.MA,9,false,18));
//
//        mixedStrategyVOs.add(new MixedStrategyPO(StrategyType.MA,8,false,16));
//
//        mixedStrategyVOs.add(new MixedStrategyPO(StrategyType.MA,7,true,14));
//
//        //  mixedStrategyVOs.add(new MixedStrategyVO(StrategyType.MA,-1,false,60));
//
//
//        stockPickIndexVOs1.add(new StockPickIndexPO(StockPickIndex.CHANGE_RATE,0.02,4.0));
//
//        UserStrategyPO userStrategyPO = new UserStrategyPO("test",LocalDate.of(196,11,25).toString(),LocalDate.of(1997,04,05).toString(),
//                stockPoolConditionVO1,mixedStrategyVOs,stockPickIndexVOs1,new TraceBackPO(10,10,10,0.2));

//        SingleStrategyPO singleStrategyPO  =new SingleStrategyPO("eee",stockPoolConditionVO1,stockPickIndexVOs1,strategyConditionVO1,new TraceBackPO(10,10,10,0.2));
//
//        System.out.println(stategyHistoryDAO.saveSingle(singleStrategyPO));


//        SingleStrategyPO userStrategyPO  = stategyHistoryDAO.getSingleStrategyPO("eee");
//
//        System.out.println(userStrategyPO==null);
//       // System.out.println(userStrategyPO.getBegin()+"  "+userStrategyPO.getEnd());
//        userStrategyPO.getStockPickIndexList().stream().forEach
//                (t-> System.out.println(t.getStockPickIndex()+"  "+t.getLowerBound()+"  "+t.getUpBound()));

        stategyHistoryDAO.getUserHistoryList();





    }
}
