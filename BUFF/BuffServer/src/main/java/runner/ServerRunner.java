package runner;

import blservice.strategy.StrategyService;
import blserviceimpl.strategy.StrategyServiceImpl;
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
        RunTimeSt.Start();


        StrategyConditionVO strategyConditionVO1 = new StrategyConditionVO(StrategyType
                .MOM, LocalDate.of(2013,1,1),LocalDate.of(2014,1,1),true);

        StockPoolConditionVO stockPoolConditionVO1  =new StockPoolConditionVO(StockPool.All,null,null,false);
        List<StockPickIndexVO> stockPickIndexVOs1 = new ArrayList<>();


		stockPickIndexVOs1.add(new StockPickIndexVO(StockPickIndex.CHANGE_RATE,0.02,4.0));


        StrategyService strategyService =  StrategyServiceImpl.STRATEGY_SERVICE;
        strategyService.init(strategyConditionVO1, stockPoolConditionVO1, stockPickIndexVOs1);



        strategyService.calculate(new TraceBackVO(10,5,10, 0.2));

        BackDetailVO backDetailVO = strategyService.getBackDetailVO();



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



        List<BetterTableVO> betterTableVOS = strategyService.getBetterTableVOByFormation(60);
        betterTableVOS.forEach(betterTableVO -> System.out.println(betterTableVO.period + "  " + betterTableVO.overProfitRate + "  " + betterTableVO.winRate));

        RunTimeSt.getRunTime("结束");

	}
	
}
