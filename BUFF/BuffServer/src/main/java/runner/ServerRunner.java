package runner;

import blserviceimpl.strategy.PickleData;
import dataserviceimpl.strategy.StrategyDAOImpl;
import pick.PickStockServiceImpl;
import po.StockPoolConditionPO;
import stockenum.StockPool;
import stockenum.StrategyType;
import util.DayMA;
import vo.StockPickIndexVO;
import vo.StockPoolConditionVO;
import vo.StrategyConditionVO;

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


        StrategyConditionVO strategyConditionVO = new StrategyConditionVO(StrategyType
                .MA,10,10,LocalDate.of(2013,1,1),LocalDate.of(2014,1,1),10,false);

        StockPoolConditionVO stockPoolConditionVO  =new StockPoolConditionVO(StockPool.All,null,null,false);
        List<StockPickIndexVO> stockPickIndexVOs = new ArrayList<>();

        StrategyDAOImpl.STRATEGY_DAO.getStocksInPool(new StockPoolConditionPO(stockPoolConditionVO));

        List<PickleData> list = StrategyDAOImpl.STRATEGY_DAO.getPickleData(strategyConditionVO,
                stockPoolConditionVO,stockPickIndexVOs);
//
        System.out.println("finish");
        for(PickleData  p: list){
            System.out.println(p.beginDate+"    "+p.endDate);
            p.stockCodes.stream().forEach(t-> System.out.println(t.code+"   "+t.rankValue));
            System.out.println();
        }


	}
	
}
