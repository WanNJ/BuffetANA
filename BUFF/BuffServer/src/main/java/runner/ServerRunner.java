package runner;

import blserviceimpl.strategy.PickleData;
import dataserviceimpl.singlestock.StockDAOImpl;
import dataserviceimpl.strategy.StrategyDAOImpl;
import pick.PickStockServiceImpl;
import po.StockPO;
import po.StockPoolConditionPO;
import stockenum.StockPickIndex;
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
//						LocalDate.of(2012,11,15),LocalDate.of(2012,11,15));
//		list.forEach(t-> System.out.println(t.getDate()+"  "+t.getVolume()));
//		List<DayMA>  list = PickStockServiceImpl.PICK_STOCK_SERVICE
//				.getSingleCodeMAInfo("300187",LocalDate.of(2014,4,25),LocalDate.of(2014,4,29),3);
//
//		list.stream().forEach(t-> System.out.println(t.date+"  "+t.MAValue));


        StrategyConditionVO strategyConditionVO = new StrategyConditionVO(StrategyType
                .MA,3,10,LocalDate.of(2013,1,1),LocalDate.of(2013,1,21),10,false);

        StockPoolConditionVO stockPoolConditionVO  =new StockPoolConditionVO(StockPool.All,null,null,false);
        List<StockPickIndexVO> stockPickIndexVOs = new ArrayList<>();
        List<PickleData> list = StrategyDAOImpl.STRATEGY_DAO.getPickleData(strategyConditionVO,
                stockPoolConditionVO,stockPickIndexVOs);


        for(PickleData  p: list){
            System.out.println(p.beginDate+"    "+p.endDate);
            p.stockCodes.stream().forEach(t-> System.out.println(t));
            System.out.println();
        }


	}
	
}
