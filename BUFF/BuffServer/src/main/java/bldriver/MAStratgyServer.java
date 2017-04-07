package bldriver;

import blserviceimpl.strategy.PickleData;
import dataserviceimpl.strategy.StrategyDAOImpl;
import po.StockPoolConditionPO;
import stockenum.StockPickIndex;
import stockenum.StockPool;
import stockenum.StrategyType;
import util.RunTimeSt;
import vo.StockPickIndexVO;
import vo.StockPoolConditionVO;
import vo.StrategyConditionVO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/4/2.
 */
public class MAStratgyServer {
    public static void main(String[] args) {


        RunTimeSt.Start();


        StrategyConditionVO strategyConditionVO = new StrategyConditionVO(StrategyType
                .MA, LocalDate.of(2013,1,1),LocalDate.of(2014,1,1),false);

        StockPoolConditionVO stockPoolConditionVO  =new StockPoolConditionVO(StockPool.All,null,null,false);
        List<StockPickIndexVO> stockPickIndexVOs = new ArrayList<>();
        stockPickIndexVOs.add(new StockPickIndexVO(StockPickIndex.PREVIOUS_DAY_VOL,100.0,100.0));

        StrategyDAOImpl.STRATEGY_DAO.getStocksInPool(new StockPoolConditionPO(stockPoolConditionVO));

        List<PickleData> list = StrategyDAOImpl.STRATEGY_DAO.getPickleData(strategyConditionVO,
                stockPoolConditionVO,stockPickIndexVOs);
        System.out.println("finish");
        for(PickleData  p: list){
            System.out.println(p.beginDate+"    "+p.endDate);
            System.out.println(p.baseProfitRate);
            System.out.println();
        }
    }
}
