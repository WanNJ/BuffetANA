package blserviceimpl.statistics;

import blservice.statistics.IndustryCorrelationService;
import dataservice.industry.IndustryDAO;
import dataservice.singlestock.StockDAO;
import dataservice.stockmap.StockNameToCodeDAO;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;
import po.StockNameAndCodePO;
import po.StockPO;
import vo.IndustryCorrelationVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by slow_time on 2017/4/12.
 */
public enum IndustryCorrelationServiceImpl implements IndustryCorrelationService {
    INDUSTRY_CORRELATION_SERVICE;

    private StockNameToCodeDAO stockNameToCodeDAO;
    private IndustryDAO industryDAO;
    private StockDAO stockDAO;
    private List<StockNameAndCodePO> stockNameAndCodePOs;
    private DAOFactoryService factory;

    IndustryCorrelationServiceImpl() {
        factory = new DAOFactoryServiceImpl();
        stockNameToCodeDAO = factory.createStockNameToCodeDAO();
        industryDAO = factory.createIndustryDAO();
        stockDAO = factory.createStockDAO();
    }


    @Override
    public IndustryCorrelationVO getInIndustryCorrelationResult(String code, int holdingPeriod) {
        IndustryCorrelationVO industryCorrelationVO = null;
        String industry = industryDAO.getIndustryByCode(code);
        stockNameAndCodePOs = stockNameToCodeDAO.getSameIndustryStocks(industry);
        List<StockPO> base = stockDAO.getStockInfoByCode(code);
        // 原本是按日期从小到大，现在调整成从大到小
        Collections.reverse(base);
        // 选择最近200个交易日数据
        base = base.stream().filter(t -> t.getVolume() != 0).limit(200).collect(Collectors.toList());
        if (base.size() == 0)
            return null;
        double maxCorrelation = 0.0;

        for(StockNameAndCodePO stock : stockNameAndCodePOs) {
            if(stock.getCode().equals(code))
                continue;
            List<StockPO> test = stockDAO.getStockInfoByCode(stock.getCode());
            // 原本是按日期从小到大，现在调整成从大到小
            Collections.reverse(test);
            test = test.stream().filter(t -> t.getVolume() != 0).limit(200 + holdingPeriod).collect(Collectors.toList());
            if (test.size() - holdingPeriod <= 0)
                continue;
            List<StockPO> baseTemp = new ArrayList<>();
            if (test.size() - holdingPeriod < base.size()) {
                baseTemp = base.stream().limit(test.size() - holdingPeriod).collect(Collectors.toList());
            }
            else {
                baseTemp.addAll(base);
            }
            List<StockPO> testTemp = test.subList(holdingPeriod, test.size());
            double correlation = getCorrelation(baseTemp, testTemp);
            if (Math.abs(correlation) > Math.abs(maxCorrelation)) {
                maxCorrelation = correlation;
                double profitRate = (test.get(0).getAdjCloseIndex() - test.get(holdingPeriod -1).getAdjCloseIndex()) / test.get(holdingPeriod -1).getAdjCloseIndex() * correlation;
                industryCorrelationVO = new IndustryCorrelationVO(stock.getCode(), stock.getName(), correlation, profitRate);
                Collections.reverse(baseTemp);
                Collections.reverse(testTemp);
                industryCorrelationVO.setBase(baseTemp.stream().map(StockPO::getAdjCloseIndex).collect(Collectors.toList()));
                industryCorrelationVO.setCompare(testTemp.stream().map(StockPO::getAdjCloseIndex).collect(Collectors.toList()));
            }
        }


        return industryCorrelationVO;
    }

    /**
     * 获得相关系数
     * @param base
     * @param test
     * @return
     */
    private double getCorrelation(List<StockPO> base, List<StockPO> test) {
        List<Double> a = base.stream().map(StockPO::getAdjCloseIndex).collect(Collectors.toList());
        List<Double> b = test.stream().map(StockPO::getAdjCloseIndex).collect(Collectors.toList());
        return getCOV(a, b) / (getSTD(a) * getSTD(b));
    }

    /**
     * 获得协方差
     */
    private double getCOV(List<Double> a, List<Double> b) {
        double aAve = getAverage(a);
        double bAve = getAverage(b);
        double sum = 0.0;
        for(int i = 0; i < a.size(); i++) {
            sum += (a.get(i) - aAve) * (b.get(i) - bAve);
        }
        return sum / a.size();
    }

    /**
     * 获得方差
     * @param a
     * @return
     */
    private double getVariance(List<Double> a) {
        double ave = getAverage(a);
        double sum = 0.0;
        for(double i : a) {
            sum += Math.pow((i - ave), 2);
        }
        return sum / a.size();
    }

    private double getSTD(List<Double> a) {
        return Math.sqrt(getVariance(a));
    }

    /**
     * 获得平均值
     * @param a
     * @return
     */
    private double getAverage(List<Double> a) {
        double sum = 0.0;
        for(double temp : a)
            sum += temp;
        return sum / a.size();
    }
}
