package rmi;

import blservice.singlestock.KLineService;
import blservice.singlestock.MALineService;
import blservice.singlestock.StockDetailService;
import blservice.singlestock.VolService;

import java.rmi.Remote;

/**
 * 
 * @author wshwbluebird
 *
 */
public class RemoteHelper {
	private Remote remote;
	private static RemoteHelper remoteHelper = new RemoteHelper();
	public static RemoteHelper getInstance(){
		return remoteHelper;
	}
	
	private RemoteHelper() {
	}
	
	public void setRemote(Remote remote){
		this.remote = remote;
	}

	public KLineService getKLineService() {
		return (KLineService)remote;
	}

	public MALineService getMALineService() {
		return (MALineService)remote;
	}

	public StockDetailService getStockDetailService() {
		return (StockDetailService)remote;
	}

	public VolService getVolService() {
		return (VolService)remote;
	}
	
}
