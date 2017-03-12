package rmi;

import blservice.comparison.ComparisonService;
import blservice.exception.DateIndexException;
import blservice.singlestock.*;
import blserviceimpl.comparison.ComparisonImpl;
import blserviceimpl.singlestock.*;
import vo.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.List;

public class DataRemoteObject extends UnicastRemoteObject implements KLineService, MALineService, StockDetailService, VolService, AllStockService, ComparisonService {
	/**
	 *  RMI 接口
	 */
	private static final long serialVersionUID = 4029039744279087114L;
	private KLineService kLineService;
	private MALineService maLineService;
	private StockDetailService stockDetailService;
	private VolService volService;
	private AllStockService allStockService;
	private ComparisonService comparisonService;

	protected DataRemoteObject() throws RemoteException {
		this.kLineService = KLineServiceImpl.K_LINE_SERVICE;
		this.stockDetailService = StockDetailServiceImpl.STOCK_DETAIL_SERVICE;
		this.volService = VolServiceImpl.VOL_SERVICE;
		this.allStockService = AllStockServiceImpl.ALL_STOCK_SERVICE;
        this.maLineService = MALineServiceImpl.MA_LINE_SERVICE;
		this.comparisonService = ComparisonImpl.COMPARISON_SERVICE;
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
	public List<MAPieceVO> getMAInfo(String code, LocalDate beginDate, LocalDate endDate) throws RemoteException {
        return maLineService.getMAInfo(code, beginDate, endDate);
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

	@Override
	public List<StockNameAndCodeVO> getAllStock() throws RemoteException {
		return allStockService.getAllStock();
	}

	@Override
	public void resetDateRange(String stockCode, LocalDate beginDate, LocalDate endDate) throws RemoteException {
		comparisonService.resetDateRange(stockCode, beginDate, endDate);

	}

	@Override
	public BasisAnalysisVO getBasisAnalysis(String stockCode, LocalDate beginDate, LocalDate endDate) throws RemoteException {
		return comparisonService.getBasisAnalysis(stockCode, beginDate, endDate);
	}

	@Override
	public List<DailyClosingPriceVO> getDailyClosingPrice(String stockCode, LocalDate beginDate, LocalDate endDate) throws RemoteException {
		return comparisonService.getDailyClosingPrice(stockCode, beginDate, endDate);
	}

	@Override
	public List<DailyLogReturnVO> getDailyLogReturnAnalysis(String stockCode, LocalDate beginDate, LocalDate endDate) throws RemoteException {
		return comparisonService.getDailyLogReturnAnalysis(stockCode, beginDate, endDate);
	}

	@Override
	public double getLogReturnVariance(String stockCode, LocalDate beginDate, LocalDate endDate) throws RemoteException {
		return comparisonService.getLogReturnVariance(stockCode, beginDate, endDate);
	}

	@Override
	public LocalDate getEarliestDate() throws RemoteException {
		return comparisonService.getEarliestDate();
	}

	@Override
	public LocalDate getLatestDate() throws RemoteException {
		return comparisonService.getLatestDate();
	}


	/**
	 * LoginCheckService的接口方法
	 */


}
