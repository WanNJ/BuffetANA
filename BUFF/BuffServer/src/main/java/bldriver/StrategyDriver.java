package bldriver;

import blservice.strategy.StrategyHistoryService;
import blserviceimpl.strategy.NewPickleData;
import blserviceimpl.strategy.StrategyHistoryServiceImpl;
import dataservice.strategy.StategyHistoryDAO;
import dataserviceimpl.singlestock.StockDAOImpl;
import dataserviceimpl.strategy.StategyHistoryDAOImpl;
import dataserviceimpl.strategy.StrategyDAOImpl;
import po.StockPO;
import po.StockPoolConditionPO;
import stockenum.StockPickIndex;
import stockenum.StockPool;
import stockenum.StrategyType;
import util.DateUtil;
import util.RunTimeSt;
import vo.StockPickIndexVO;
import vo.StockPoolConditionVO;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wshwbluebird on 2017/4/5.
 */
public class StrategyDriver {
    public static void main(String[] args) {
//               RunTimeSt.Start();
//
//        StrategyType strategyType = StrategyType.MA;
//        StrategyDAOImpl strategyDAO = StrategyDAOImpl.STRATEGY_DAO;
//        StockPoolConditionVO stockPoolConditionVO1  =new StockPoolConditionVO(StockPool.All,null,null,false);
//
//        List<String> codelist = strategyDAO.getStocksInPool(new StockPoolConditionPO(stockPoolConditionVO1));
//
//        List<StockPickIndexVO> stockPickIndexVOs1 = new ArrayList<>();
//        List<NewPickleData> newPickleDataList = strategyType.setAllValue
//                (codelist,LocalDate.of(2013,1,1),LocalDate.of(2014,1,1),stockPickIndexVOs1);
//
//        RunTimeSt.getRunTime("成功读取  ");
//
//        newPickleDataList.get(0).singleBackDataList.forEach(t-> System.out.println(t.date+"   "+t.rankValues[2]));

        StategyHistoryDAO stategyHistoryDAO  = StategyHistoryDAOImpl.STATEGY_HISTORY_DAO;
        stategyHistoryDAO.getSingleStrategyPO("sdf");
    }



}
