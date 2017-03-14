package blserviceimpl.comparison;

import dataservice.singlestock.StockDAO;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;
import po.StockPO;
import util.Statistics;
import vo.BasisAnalysisVO;
import vo.DailyClosingPriceVO;
import vo.DailyLogReturnVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Accident on 2017/3/14.
 */
public class SingleForComparison {
    private StockDAO stockDAO;
    private DAOFactoryService factory;

    private List<StockPO> allStockPOs;
    private List<StockPO> specificStockPOs;

    private List<DailyClosingPriceVO> dailyClosingPriceVOS;
    private List<DailyLogReturnVO> dailyLogReturnVOs;


    public SingleForComparison(String stockCode, LocalDate beginDate, LocalDate endDate) throws RemoteException {
        factory = new DAOFactoryServiceImpl();
        stockDAO = factory.createStockDAO();
        setDateRange(stockCode, beginDate, endDate);
        dailyClosingPriceVOS = new ArrayList<DailyClosingPriceVO>();
        specificStockPOs.forEach(stockPO -> {
            dailyClosingPriceVOS.add(new DailyClosingPriceVO(stockPO.getDate(), stockPO.getClose_Price()));
        });
        dailyLogReturnVOs = new ArrayList<DailyLogReturnVO>();
        dailyLogReturnVOs.add(new DailyLogReturnVO(specificStockPOs.get(0).getDate(), 0));
        for (int i = 1; i < specificStockPOs.size(); i++) {
            dailyLogReturnVOs.add(new DailyLogReturnVO(specificStockPOs.get(i).getDate(),
                    Math.log(specificStockPOs.get(i - 1).getAdjCloseIndex() / specificStockPOs.get(i).getAdjCloseIndex())));
        }
    }

    /**
     * 如果无数据，则返回null
     * @return BasisAnalysisVO
     * @throws RemoteException
     */
    public BasisAnalysisVO getBasisAnalysis() throws RemoteException {
        if (specificStockPOs == null || specificStockPOs.size() == 0)
            return null;
        BasisAnalysisVO result = new BasisAnalysisVO();

        result.openPrice = specificStockPOs.get(0).getOpen_Price();
        result.closePrice = specificStockPOs.get(specificStockPOs.size() - 1).getClose_Price();
        result.changeRate = (result.closePrice - result.openPrice) / result.openPrice;

        //计算最高价和最低价
        specificStockPOs.forEach(stockPO -> {
            if (stockPO.getLow_Price() < result.lowPrice)
                result.lowPrice = stockPO.getLow_Price();
            if (stockPO.getHigh_Price() > result.highPrice)
                result.highPrice = stockPO.getHigh_Price();
        });

        return result;
    }

    public List<DailyClosingPriceVO> getDailyClosingPrice() throws RemoteException {
        return dailyClosingPriceVOS;
    }

    public List<DailyLogReturnVO> getDailyLogReturnAnalysis() throws RemoteException {
        return dailyLogReturnVOs;
    }

    public double getLogReturnVariance() throws RemoteException {
        ArrayList<Double> data = new ArrayList<Double>();
        dailyLogReturnVOs.forEach(dailyLogReturnVO -> data.add(dailyLogReturnVO.logReturnIndex));
        Statistics statistics = new Statistics(data);

        return statistics.getVariance();
    }

    public LocalDate getEarliestDate() throws RemoteException {
        return allStockPOs.get(0).getDate();
    }

    public LocalDate getLatestDate() throws RemoteException {
        return allStockPOs.get(allStockPOs.size() - 1).getDate();
    }

    /**
     * 获得可用的所有数据，并根据起止日期得到筛选后的数据
     * @param stockCode
     * @param beginDate
     * @param endDate
     * @throws RemoteException
     */
    private void setDateRange(String stockCode, LocalDate beginDate, LocalDate endDate) throws RemoteException {
        allStockPOs = stockDAO.getStockInfoByCode(stockCode);
        specificStockPOs = allStockPOs.stream().filter((StockPO stockPO) -> stockPO.getDate().isAfter(beginDate.minusDays(1))
                && stockPO.getDate().isBefore(endDate.plusDays(1)))
                .collect(Collectors.toList());
    }
}
