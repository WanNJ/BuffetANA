package runner;

import blservice.strategy.StrategyService;
import blserviceimpl.strategy.StrategyServiceImpl;
import dataserviceimpl.strategy.StrategyDAOImpl;
import po.StockPoolConditionPO;
import stockenum.StockPickIndex;
import stockenum.StockPool;
import stockenum.StrategyType;
import util.RunTimeSt;
import util.StrategyScoreVO;
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

//        StrategyConditionVO strategyConditionVO1 = new StrategyConditionVO(StrategyType
//                .MA, LocalDate.of(2013,1,1),LocalDate.of(2014,1,1),true);


        StrategyConditionVO strategyConditionVO1 = new StrategyConditionVO(null
                , LocalDate.of(2013,1,1),LocalDate.of(2014,1,1),true);
//        strategyConditionVO1.holdingRate = 0.2;
        StockPoolConditionVO stockPoolConditionVO1  =new StockPoolConditionVO(StockPool.HS300,null,null,false);
        List<StockPickIndexVO> stockPickIndexVOs1 = new ArrayList<>();
        List<MixedStrategyVO> mixedStrategyVOs = new ArrayList<>();
        mixedStrategyVOs.add(new MixedStrategyVO(StrategyType.MA,10,false,20));
//        mixedStrategyVOs.add(new MixedStrategyVO(StrategyType.MOM,1,false,20));
        mixedStrategyVOs.add(new MixedStrategyVO(StrategyType.MA,9,false,18));

        mixedStrategyVOs.add(new MixedStrategyVO(StrategyType.MA,8,false,16));

        mixedStrategyVOs.add(new MixedStrategyVO(StrategyType.MA,7,false,14));

      //  mixedStrategyVOs.add(new MixedStrategyVO(StrategyType.MA,-1,false,60));


		stockPickIndexVOs1.add(new StockPickIndexVO(StockPickIndex.CHANGE_RATE,0.02,4.0));
        StrategyDAOImpl.STRATEGY_DAO.getStocksInPool(new StockPoolConditionPO(stockPoolConditionVO1));


        StrategyService strategyService =  StrategyServiceImpl.STRATEGY_SERVICE;
//        strategyService.init(strategyConditionVO1, stockPoolConditionVO1, stockPickIndexVOs1);
        strategyService.initMixed( LocalDate.of(2013,1,1)
                ,LocalDate.of(2014,1,1),stockPoolConditionVO1,stockPickIndexVOs1,new TraceBackVO(20,10,10)
       ,mixedStrategyVOs);
//        strategyService.calculate(new TraceBackVO(20,10,10));
//        strategyService.init(strategyConditionVO1, stockPoolConditionVO1, stockPickIndexVOs1);
//        strategyService.initMixed( LocalDate.of(2013,1,1),LocalDate.of(2014,1,1),stockPoolConditionVO1,
//                stockPickIndexVOs1,new TraceBackVO(20,10,200)
//        ,mixedStrategyVOs);
//        strategyService.calculate(new TraceBackVO(20,10,10));
//        strategyService.calculate(new TraceBackVO(20,10,0, 0.2));
        BackDetailVO backDetailVO = strategyService.getBackDetailVO();
//        List<StockNameAndCodeVO> stockNameAndCodeVOS = strategyService.getAllStocksInPool();
//        stockNameAndCodeVOS.forEach(stockNameAndCodeVO -> System.out.println(stockNameAndCodeVO.code + " " + stockNameAndCodeVO.name));
//        System.out.println(stockNameAndCodeVOS.size());


        System.out.println("alpha: " + backDetailVO.alpha);
        System.out.println("beta: " + backDetailVO.beta);
        System.out.println("yearProfitRate: " + backDetailVO.yearProfitRate);
        System.out.println("baseYearProfitRate: " + backDetailVO.baseYearProfitRate);
        System.out.println("sharpRate: " + backDetailVO.sharpRate);
        System.out.println("largestBackRate: " + backDetailVO.largestBackRate);

        StrategyScoreVO strategyScoreVO = strategyService.getStrategyEstimateResult();
        System.out.println("盈利能力: " + strategyScoreVO.profitAbility);
        System.out.println("稳定性: " + strategyScoreVO.stability);
        System.out.println("选股能力: " + strategyScoreVO.chooseStockAbility);
        System.out.println("绝对收益: " + strategyScoreVO.absoluteProfit);
        System.out.println("抗风险能力: " + strategyScoreVO.antiRiskAbility);
        System.out.println("策略总得分: " + strategyScoreVO.strategyScore);

       // List<BetterTableVO> betterTableVOS = strategyService.getBetterTableVOByHolding(5);
        //betterTableVOS.forEach(betterTableVO -> System.out.println(betterTableVO.period + "  " + betterTableVO.overProfitRate + "  " + betterTableVO.winRate));

//
//        PickStockService pickStockService = PickStockServiceImpl.PICK_STOCK_SERVICE;
//        List<LongPeiceVO> longPeiceVOs = pickStockService.getLastVol("000017",LocalDate.of(2013,1,1),LocalDate.of(2014,1,1));
//        longPeiceVOs.stream().forEach(t-> System.out.println(t.localDate+"   "+t.amount));

        RunTimeSt.getRunTime("结束");

	}
	
}
