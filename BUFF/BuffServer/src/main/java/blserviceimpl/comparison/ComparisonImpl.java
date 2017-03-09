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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Accident on 2017/3/9.
 * 两股对比服务逻辑实现代码
 */
public class ComparisonImpl implements ComparisonService{

    private StockDAO stockDAO;
    private DAOFactoryService factory;

    private String code;
    private LocalDate beginDate;
    private LocalDate endDate;

    private List<StockPO> allStockPOs;
    private List<StockPO> specificStockPOs;
    private List<DailyClosingPriceVO> dailyClosingPriceVOS;
    private List<DailyLogReturnVO> dailyLogReturnVOs;

    public ComparisonImpl(String stockCode, LocalDate beginDate, LocalDate endDate) {
        factory = new DAOFactoryServiceImpl();
        stockDAO = factory.createStockDAO();
        code = stockCode;
        this.beginDate = beginDate;
        this.endDate = endDate;
        allStockPOs = stockDAO.getStockInfoByCode(code);
        resetDateRange(this.beginDate, this.endDate);
    }

    @Override
    public void resetDateRange(LocalDate beginDate, LocalDate endDate) {
         specificStockPOs = allStockPOs.stream()
                .filter(stockPO -> stockPO.getDate().isAfter(beginDate) && stockPO.getDate().isBefore(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public BasisAnalysisVO getBasisAnalysis(String stockCode, LocalDate beginDate, LocalDate endDate) {
        BasisAnalysisVO result = new BasisAnalysisVO();
        result.openPrice = specificStockPOs.get(0).getOpen_Price();
        result.closePrice = specificStockPOs.get(specificStockPOs.size() - 1).getClose_Price();
        result.changeRate = (result.closePrice - result.openPrice)/result.openPrice;

        specificStockPOs.forEach(stockPO -> {
            if (stockPO.getLow_Price() < result.lowPrice)
                result.lowPrice = stockPO.getLow_Price();
            if (stockPO.getHigh_Price() > result.highPrice)
                result.highPrice = stockPO.getHigh_Price();
        });

        return result;
    }

    @Override
    public List<DailyClosingPriceVO> getDailyClosingPrice(String stockCode, LocalDate beginDate, LocalDate endDate) {
        dailyClosingPriceVOS = new ArrayList<DailyClosingPriceVO>();
        specificStockPOs.forEach(stockPO -> {
            dailyClosingPriceVOS.add(new DailyClosingPriceVO(stockPO.getDate(), stockPO.getClose_Price()));
        });

        return dailyClosingPriceVOS;
    }

    @Override
    public List<DailyLogReturnVO> getDailyLogReturnAnalysis(String stockCode, LocalDate beginDate, LocalDate endDate) {
        dailyLogReturnVOs = new ArrayList<DailyLogReturnVO>();
        dailyLogReturnVOs.add(new DailyLogReturnVO(dailyClosingPriceVOS.get(0).date, 0));
        for(int i = 1; i < dailyClosingPriceVOS.size(); i++) {
            dailyLogReturnVOs.add(new DailyLogReturnVO(dailyClosingPriceVOS.get(i).date,
                    dailyClosingPriceVOS.get(i - 1).closePrice / dailyClosingPriceVOS.get(i).closePrice));
        }

        return dailyLogReturnVOs;
    }

    @Override
    public double getLogReturnVariance(String stockCode, LocalDate beginDate, LocalDate endDate) {
        ArrayList<Double> data = new ArrayList<Double>();
        dailyLogReturnVOs.forEach(dailyLogReturnVO -> data.add(dailyLogReturnVO.logReturnIndex));
        Statistics statistics = new Statistics(data);

        return statistics.getVariance();
    }
}
