package runner;

import dataserviceimpl.singlestock.StockDAOImpl;
import po.StockPO;

import java.time.LocalDate;
import java.util.List;

public class ServerRunner {
	
	public ServerRunner() {
		//new RemoteHelper();
	}
	
	public static void main(String[] args)  {
		List<StockPO> list =
				StockDAOImpl.STOCK_DAO_IMPL.getStockInFoInRangeDate("1",
						LocalDate.of(2012,11,15),LocalDate.of(2012,11,15));
		list.forEach(t-> System.out.println(t.getDate()+"  "+t.getVolume()));

	}
	
}
