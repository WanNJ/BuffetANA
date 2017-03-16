package rmi;

import blservice.comparison.ComparisonService;
import blservice.exception.DateIndexException;
import blservice.exception.InvalidDateException;
import blservice.exception.InvalidStockCodeException;
import blservice.market.MarketService;
import blservice.singlestock.*;
import blservice.thermometer.ThermometerService;
import blserviceimpl.comparison.ComparisonImpl;
import blserviceimpl.market.MarketServiceImpl;
import blserviceimpl.singlestock.*;
import blserviceimpl.thermometer.ThermometerServiceImpl;
import util.DateRange;
import vo.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.List;

public class DataRemoteObject extends UnicastRemoteObject implements
		KLineService, MALineService, StockDetailService, VolService,
		AllStockService, ComparisonService, MarketService,ThermometerService {
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
	private MarketService marketService;
	private ThermometerService thermometerService;

	protected DataRemoteObject() throws RemoteException {
		this.kLineService = KLineServiceImpl.K_LINE_SERVICE;
		this.stockDetailService = StockDetailServiceImpl.STOCK_DETAIL_SERVICE;
		this.volService = VolServiceImpl.VOL_SERVICE;
		this.allStockService = AllStockServiceImpl.ALL_STOCK_SERVICE;
        this.maLineService = MALineServiceImpl.MA_LINE_SERVICE;
		this.comparisonService = ComparisonImpl.COMPARISON_SERVICE;
		this.marketService = MarketServiceImpl.MARKET_SERVICE;
		this.thermometerService = ThermometerServiceImpl.THERMOMETER_SERVCE;
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
	public void init(String mainStockCode, String deputyStockCode, LocalDate beginDate, LocalDate endDate) throws RemoteException, InvalidStockCodeException, InvalidDateException {
		comparisonService.init(mainStockCode, deputyStockCode, beginDate, endDate);
	}

	@Override
	public BasisAnalysisVO getMainBasisAnalysis() throws RemoteException {
		return comparisonService.getMainBasisAnalysis();
	}

	@Override
	public List<DailyClosingPriceVO> getMainDailyClosingPrice() throws RemoteException {
		return comparisonService.getMainDailyClosingPrice();
	}

	@Override
	public List<DailyLogReturnVO> getMainDailyLogReturnAnalysis() throws RemoteException {
		return comparisonService.getMainDailyLogReturnAnalysis();
	}

	@Override
	public double getMainLogReturnVariance() throws RemoteException {
		return comparisonService.getMainLogReturnVariance();
	}

	@Override
	public BasisAnalysisVO getDeputyBasisAnalysis() throws RemoteException {
		return comparisonService.getDeputyBasisAnalysis();
	}

	@Override
	public List<DailyClosingPriceVO> getDeputyDailyClosingPrice() throws RemoteException {
		return comparisonService.getDeputyDailyClosingPrice();
	}

	@Override
	public List<DailyLogReturnVO> getDeputyDailyLogReturnAnalysis() throws RemoteException {
		return comparisonService.getDeputyDailyLogReturnAnalysis();
	}

	@Override
	public double getDeputyLogReturnVariance() throws RemoteException {
		return comparisonService.getDeputyLogReturnVariance();
	}

	@Override
	public LocalDate getEarliestDate() throws RemoteException {
		return comparisonService.getEarliestDate();
	}

	@Override
	public LocalDate getLatestDate() throws RemoteException {
		return comparisonService.getLatestDate();
	}

	@Override
	public List<KLinePieceVO> getMarketDailyKLine(LocalDate beginDate, LocalDate endDate) throws DateIndexException, RemoteException {
		return marketService.getMarketDailyKLine(beginDate, endDate);
	}

	@Override
	public List<StockVolVO> getMarketVol(LocalDate beginDate, LocalDate endDate) throws DateIndexException, RemoteException {
		return marketService.getMarketVol(beginDate, endDate);
	}

	@Override
	public List<MarketStockDetailVO> getMarketStockDetailVO() throws RemoteException {
		return marketService.getMarketStockDetailVO();
	}

	@Override
	public MarketStockDetailVO getHistoryStockDetailVO(String code) throws RemoteException {
		return marketService.getHistoryStockDetailVO(code);
	}

	@Override
	public List<StockVolVO> getTradingVolume(DateRange dateRange) throws RemoteException {
		return this.thermometerService.getTradingVolume(dateRange);
	}

	@Override
	public List<StockVolVO> getTradingVolume(DateRange dateRange, String shareID) throws RemoteException {
		return this.thermometerService.getTradingVolume(dateRange,shareID);
	}

	@Override
	public List<LongPeiceVO> getLimitUpNum(DateRange dateRange) throws RemoteException {
		return this.thermometerService.getLimitUpNum(dateRange);
	}

	@Override
	public List<LongPeiceVO> getLimitDownNum(DateRange dateRange) throws RemoteException {
		return this.thermometerService.getLimitDownNum(dateRange);
	}

	@Override
	public List<LongPeiceVO> getRiseOver5Num(DateRange dateRange) throws RemoteException {
		return this.thermometerService.getRiseOver5Num(dateRange);
	}

	@Override
	public List<LongPeiceVO> getFallOver5Num(DateRange dateRange) throws RemoteException {
		return this.thermometerService.getFallOver5Num(dateRange);
	}

	@Override
	public List<LongPeiceVO> getRiseOver5ThanLastDayNum(DateRange dateRange) throws RemoteException {
		return this.thermometerService.getRiseOver5ThanLastDayNum(dateRange);
	}

	@Override
	public List<LongPeiceVO> getFallOver5ThanLastDayNum(DateRange dateRange) throws RemoteException {
		return this.thermometerService.getFallOver5ThanLastDayNum(dateRange);
	}


	/**
	 * LoginCheckService的接口方法
	 */
}
