package blserviceimpl.comparison;

import blservice.comparison.ComparisonService;
import dataservice.singlestock.StockDAO;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;
import po.StockPO;
import util.Statistics;
import vo.BasisAnalysisVO;
import vo.DailyClosingPriceVO;
import vo.DailyLogReturnVO;

import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Accident on 2017/3/9.
 * 两股对比服务逻辑实现代码
 */

public enum ComparisonImpl implements ComparisonService {
    COMPARISON_SERVICE;

    private StockDAO stockDAO;
    private DAOFactoryService factory;

    //TODO 关于界面设置可选日期的代码
    private LocalDate earliestDate;
    private LocalDate latestDate;

    private List<StockPO> allStockPOs;
    private List<StockPO> specificStockPOs;
    private List<DailyClosingPriceVO> dailyClosingPriceVOS;
    private List<DailyLogReturnVO> dailyLogReturnVOs;


    ComparisonImpl() {
        factory = new DAOFactoryServiceImpl();
        stockDAO = factory.createStockDAO();
    }

    @Override
    public void setDateRange(String stockCode, LocalDate beginDate, LocalDate endDate) throws RemoteException {
        allStockPOs = stockDAO.getStockInfoByCode(stockCode);
        specificStockPOs = allStockPOs.stream().filter((StockPO stockPO) -> stockPO.getDate().isAfter(beginDate.minusDays(1))
                && stockPO.getDate().isBefore(endDate.plusDays(1)))
                .collect(Collectors.toList());
    }

    @Override
    public BasisAnalysisVO getBasisAnalysis(String stockCode, LocalDate beginDate, LocalDate endDate) throws RemoteException {
        if (specificStockPOs == null || specificStockPOs.size() == 0)
            return null;
        BasisAnalysisVO result = new BasisAnalysisVO();
        result.openPrice = specificStockPOs.get(0).getOpen_Price();
        result.closePrice = specificStockPOs.get(specificStockPOs.size() - 1).getClose_Price();
        result.changeRate = (result.closePrice - result.openPrice) / result.openPrice;

        specificStockPOs.forEach(stockPO -> {
            if (stockPO.getLow_Price() < result.lowPrice)
                result.lowPrice = stockPO.getLow_Price();
            if (stockPO.getHigh_Price() > result.highPrice)
                result.highPrice = stockPO.getHigh_Price();
        });

        return result;
    }

    @Override
    public List<DailyClosingPriceVO> getDailyClosingPrice(String stockCode, LocalDate beginDate, LocalDate endDate) throws RemoteException {
        dailyClosingPriceVOS = new ArrayList<DailyClosingPriceVO>();
        specificStockPOs.forEach(stockPO -> {
            dailyClosingPriceVOS.add(new DailyClosingPriceVO(stockPO.getDate(), stockPO.getClose_Price()));
        });

        return dailyClosingPriceVOS;
    }

    @Override
    public List<DailyLogReturnVO> getDailyLogReturnAnalysis(String stockCode, LocalDate beginDate, LocalDate endDate) throws RemoteException {
        dailyLogReturnVOs = new ArrayList<DailyLogReturnVO>();
        dailyLogReturnVOs.add(new DailyLogReturnVO(specificStockPOs.get(0).getDate(), 0));
        for (int i = 1; i < specificStockPOs.size(); i++) {
            dailyLogReturnVOs.add(new DailyLogReturnVO(specificStockPOs.get(i).getDate(),
                    specificStockPOs.get(i - 1).getAdjCloseIndex() / specificStockPOs.get(i).getAdjCloseIndex()));
        }
        return dailyLogReturnVOs;
    }

    @Override
    public double getLogReturnVariance(String stockCode, LocalDate beginDate, LocalDate endDate) throws RemoteException {
        ArrayList<Double> data = new ArrayList<Double>();
        dailyLogReturnVOs.forEach(dailyLogReturnVO -> data.add(dailyLogReturnVO.logReturnIndex));
        Statistics statistics = new Statistics(data);

        return statistics.getVariance();
    }

    @Override
    public LocalDate getEarliestDate() throws RemoteException {
        return allStockPOs.get(0).getDate();
    }

    @Override
    public LocalDate getLatestDate() throws RemoteException {
        return allStockPOs.get(allStockPOs.size() - 1).getDate();
    }
}
