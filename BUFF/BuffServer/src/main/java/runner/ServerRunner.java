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


//        StrategyConditionVO strategyConditionVO = new StrategyConditionVO(StrategyType
//                .MA,3,10,LocalDate.of(2013,1,1),LocalDate.of(2014,1,1),10,false);
//
//        StockPoolConditionVO stockPoolConditionVO  =new StockPoolConditionVO(StockPool.All,null,null,false);
//        List<StockPickIndexVO> stockPickIndexVOs = new ArrayList<>();
//        List<PickleData> list = StrategyDAOImpl.STRATEGY_DAO.getPickleData(strategyConditionVO,
//                stockPoolConditionVO,stockPickIndexVOs);
//
//
//        for(PickleData  p: list){
//            System.out.println(p.beginDate+"    "+p.endDate);
//            p.stockCodes.stream().forEach(t-> System.out.println(t));
//            System.out.println();
//        }
//        double d  = 0;
//       // Number i  = d;
//        int r = 123;
//        //System.out.println(i);
//        //i = r;
//
//
//        Number[] ss = new Number[5];
//        ss[0] = d;
//        ss[1] = r;
//
//        System.out.println(ss[0]+"  "+ss[1]);

//		int x = StockPickIndex.values().length;
//		Number[] ss  = new Number[x+1];
//		System.out.println(StockPool.All.ordinal());
//		System.out.println(StockPool.UserMode.ordinal());
//		//ss[StockPickIndex.PREVIOUS_DAY_UPRATE.ordinal()]



	}
	
}
