package rmi;

import blservice.exception.DateIndexException;
import blservice.singlestock.KLineService;
import blservice.singlestock.MALineService;
import blservice.singlestock.StockDetailService;
import blservice.singlestock.VolService;
import blserviceimpl.singlestock.KLineServiceImpl;
import blserviceimpl.singlestock.StockDetailServiceImpl;
import blserviceimpl.singlestock.VolServiceImpl;
import vo.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.List;

public class DataRemoteObject extends UnicastRemoteObject implements KLineService, MALineService, StockDetailService, VolService {
	/**
	 *  RMI 接口
	 */
	private static final long serialVersionUID = 4029039744279087114L;
	private KLineService kLineService;
	private MALineService maLineService;
	private StockDetailService stockDetailService;
	private VolService volService;

	protected DataRemoteObject() throws RemoteException {
		this.kLineService = KLineServiceImpl.K_LINE_SERVICE;
		this.stockDetailService = StockDetailServiceImpl.STOCK_DETAIL_SERVICE;
		this.volService = VolServiceImpl.VOL_SERVICE;
	}

	@Override
	public List<KLinePieceVO> getDailyKLine(String code, LocalDate beginDate, LocalDate endDate) throws DateIndexException, RemoteException {
		return kLineService.getDailyKLine(code, beginDate, endDate);
	}

	@Override
	public List<KLinePieceVO> getWeeklyKLine(String code, LocalDate beginDate, LocalDate endDate) throws RemoteException {
		return kLineService.getWeeklyKLine(code, beginDate, endDate);
	}

	@Override
	public List<KLinePieceVO> getMonthlyKLine(String code, LocalDate beginDate, LocalDate endDate) throws RemoteException {
		return kLineService.getMonthlyKLine(code, beginDate, endDate);
	}

	@Override
	public List<MAPieceVO> getMAInfo(String code, LocalDate beginDate, LocalDate endDate) {
		return null;
	}

	@Override
	public StockDetailVO getSingleStockDetails(String code, LocalDate date) throws RemoteException {
		return stockDetailService.getSingleStockDetails(code, date);
	}

	@Override
	public List<StockBriefInfoVO> getStockBriefInfo(String code) throws RemoteException {
		return stockDetailService.getStockBriefInfo(code);
	}

	@Override
	public List<StockVolVO> getStockVol(String code, LocalDate beginDate, LocalDate endDate) throws DateIndexException, RemoteException {
		return volService.getStockVol(code, beginDate, endDate);
	}


	/**
	 * LoginCheckService的接口方法
	 */


}
