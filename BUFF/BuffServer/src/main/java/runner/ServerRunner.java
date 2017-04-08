package runner;

import blservice.strategy.StrategyService;
import blserviceimpl.strategy.StrategyServiceImpl;
import dataserviceimpl.strategy.StrategyDAOImpl;
import po.StockPoolConditionPO;
import stockenum.StockPickIndex;
import stockenum.StockPool;
import stockenum.StrategyType;
import util.RunTimeSt;
import vo.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServerRunner {
	
	public ServerRunner() {
		//new RemoteHelper();
	}
	
	public static void main(String[] args)  {
//		List<StockPO> list =
//				StockDAOImpl.STOCK_DAO_IMPL.getStockInFoInRangeDate("1",
//						LocalDate.of(2013,1,1),LocalDate.of(2014,1,1));
//		list.forEach(t-> System.out.println(t.getDate()+"  "+t.getVolume()));

//		List<DayMA>  list2 = PickStockServiceImpl.PICK_STOCK_SERVICE
//				.getSingleCodeMAInfo("300187",LocalDate.of(2013,12,1),LocalDate.of(2014,1,1),2);
//
//		list2.stream().forEach(t-> System.out.println(t.date+"  "+t.MAValue));

        RunTimeSt.Start();


//        StrategyConditionVO strategyConditionVO = new StrategyConditionVO(StrategyType
//                .MA,10,10,LocalDate.of(2013,1,1),LocalDate.of(2014,1,1),10,false);
//
//        StockPoolConditionVO stockPoolConditionVO  =new StockPoolConditionVO(StockPool.All,null,null,false);
//        List<StockPickIndexVO> stockPickIndexVOs = new ArrayList<>();
//        stockPickIndexVOs.add(new StockPickIndexVO(StockPickIndex.PREVIOUS_DAY_VOL,100.0,100.0));
//
//        StrategyDAOImpl.STRATEGY_DAO.getStocksInPool(new StockPoolConditionPO(stockPoolConditionVO));
//
//        List<PickleData> list = StrategyDAOImpl.STRATEGY_DAO.getPickleData(strategyConditionVO,
//                stockPoolConditionVO,stockPickIndexVOs);
//        System.out.println("finish");
//        for(PickleData  p: list){
//            System.out.println(p.beginDate+"    "+p.endDate);
//            p.stockCodes.stream().forEach(t-> System.out.println(t.code+"   "+t.rankValue));
//            System.out.println();
//        }

        StrategyConditionVO strategyConditionVO1 = new StrategyConditionVO(StrategyType
                .MA, LocalDate.of(2013,1,1),LocalDate.of(2014,1,1),true);
//        strategyConditionVO1.holdingRate = 0.2;
        StockPoolConditionVO stockPoolConditionVO1  =new StockPoolConditionVO(StockPool.All,null,null,false);
        List<StockPickIndexVO> stockPickIndexVOs1 = new ArrayList<>();


		stockPickIndexVOs1.add(new StockPickIndexVO(StockPickIndex.PREVIOUS_DAY_VOL,100.0,100000000000.0));
        StrategyDAOImpl.STRATEGY_DAO.getStocksInPool(new StockPoolConditionPO(stockPoolConditionVO1));
//        List<PickleData> list1 = StrategyDAOImpl.STRATEGY_DAO.getPickleData(strategyConditionVO1,
//                stockPoolConditionVO1,stockPickIndexVOs1);
//        System.out.println("finish");
//        for(PickleData  p: list1){
//            System.out.println(p.beginDate+"    "+p.endDate);
//            p.stockCodes.stream().forEach(t-> System.out.println(t.code+"   "+t.rankValue));
//            System.out.println();
//        }

        StrategyService strategyService = new StrategyServiceImpl();
        strategyService.init(strategyConditionVO1, stockPoolConditionVO1, stockPickIndexVOs1);

        strategyService.calculate(new TraceBackVO(20,10,10));
//        strategyService.calculate(new TraceBackVO(20,10,0, 0.2));

        BackDetailVO backDetailVO = strategyService.getBackDetailVO();



        System.out.println("alpha: " + backDetailVO.alpha);
        System.out.println("beta: " + backDetailVO.beta);
        System.out.println("yearProfitRate: " + backDetailVO.yearProfitRate);
        System.out.println("baseYearProfitRate: " + backDetailVO.baseYearProfitRate);
        System.out.println("sharpRate: " + backDetailVO.sharpRate);
        System.out.println("largestBackRate: " + backDetailVO.largestBackRate);

        List<BetterTableVO> betterTableVOS = strategyService.getBetterTableVOByHolding(5);
        betterTableVOS.forEach(betterTableVO -> System.out.println(betterTableVO.period + "  " + betterTableVO.overProfitRate + "  " + betterTableVO.winRate));

//
//        PickStockService pickStockService = PickStockServiceImpl.PICK_STOCK_SERVICE;
//        List<LongPeiceVO> longPeiceVOs = pickStockService.getLastVol("000017",LocalDate.of(2013,1,1),LocalDate.of(2014,1,1));
//        longPeiceVOs.stream().forEach(t-> System.out.println(t.localDate+"   "+t.amount));

        RunTimeSt.getRunTime("结束");

	}
	
}
