package bldriver;

import blserviceimpl.statistics.SingleCodePredictServiceImpl;
import blserviceimpl.strategy.StrategyHistoryServiceImpl;
import dataservice.strategy.StategyHistoryDAO;
import dataserviceimpl.strategy.StategyHistoryDAOImpl;
import po.SingleStrategyPO;
import po.UserStrategyPO;
import vo.NormalStasticVO;
import vo.StrategySaveVO;

/**
 * Created by wshwbluebird on 2017/4/17.
 */
public class testLogic {

    public static void main(String[] args) {

        StrategySaveVO strategySaveVO =
                StrategyHistoryServiceImpl.STRATEGY_HISTORY_SERVICE.getStrategyHistory("ma1");

        StategyHistoryDAO stategyHistoryDAO = StategyHistoryDAOImpl.STATEGY_HISTORY_DAO;

        UserStrategyPO userStrategyPO = stategyHistoryDAO.getUserStrategyPO("ma1");

        System.out.println(strategySaveVO==null);
        System.out.println(strategySaveVO.begin);
        System.out.println(strategySaveVO.end);
        System.out.println(strategySaveVO.strategyName);
        System.out.println(strategySaveVO.traceBackVO.formationPeriod);
//        System.out.println(userStrategyPO==null);
//        System.out.println(userStrategyPO.getStrategyName());
//        System.out.println(userStrategyPO.getTraceBackPO().getHoldingNum());
//        System.out.println(userStrategyPO.getBegin());
//        System.out.println(userStrategyPO.getEnd());
//        System.out.println(userStrategyPO.getMixedStrategyPOList().get(0).getStrategyType());

    }
}
