package rmi;

import blservice.comparison.ComparisonService;
import blservice.market.MarketService;
import blservice.singlestock.*;

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

	public AllStockService getAllStockService() {
		return (AllStockService)remote;
	}

	public ComparisonService getComparisonService() { return (ComparisonService)remote; }

	public MarketService getMarketService() {
		return (MarketService)remote;
	}

}
