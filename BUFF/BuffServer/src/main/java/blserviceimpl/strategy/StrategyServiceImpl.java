package blserviceimpl.strategy;

import blservice.strategy.StrategyService;
import dataservice.strategy.StrategyDAO;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;
import vo.*;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slow_time on 2017/3/24.
 */
public class StrategyServiceImpl implements StrategyService {

    private StrategyConditionVO strategyConditionVO;
    private StockPoolConditionVO stockPoolConditionVO;
    private List<StockPickIndexVO> stockPickIndexVOs;
    private DAOFactoryService daoFactoryService;
    private StrategyDAO strategyDAO;
    private List<PickleData> pickleDatas;
    /**
     * 基准收益率列表
     */
    private ArrayList<Double> baseRates;
    /**
     * 策略收益率表
     */
    private ArrayList<Double> strategyRates;
    /**
     * 基准年化收益率
     */
    private double baseYearProfitRate;
    /**
     * 策略年化收益率
     */
    private double yearProfitRate;


    /**
     * 可以用于后期test时将工厂替换成stub
     * @param daoFactoryService
     */
    public void setFactory(DAOFactoryService daoFactoryService) {
        this.daoFactoryService = daoFactoryService;
        this.strategyDAO = this.daoFactoryService.createStrategyDAO();
        this.pickleDatas = strategyDAO.getPickleData(strategyConditionVO, stockPoolConditionVO, stockPickIndexVOs);
    }

    @Override
    public void init(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexVOs) {
        this.strategyConditionVO = strategyConditionVO;
        this.stockPoolConditionVO = stockPoolConditionVO;
        this.stockPickIndexVOs = stockPickIndexVOs;
        setFactory(new DAOFactoryServiceImpl());

        // 初始化一些必要的重复计算的参数
        baseYearProfitRate = 0.0;
        yearProfitRate = 0.0;
        baseRates = new ArrayList<>();
        strategyRates = new ArrayList<>();
        for(PickleData pickleData : pickleDatas) {
            double tempRate = 0.0;
            baseRates.add(pickleData.baseProfitRate);
            baseYearProfitRate += pickleData.baseProfitRate;
            for(BackData backData : pickleData.stockCodes) {
//                System.out.println(backData.code);
//                System.out.println(pickleData.beginDate + "   " + pickleData.endDate);
//                System.out.println(backData.lastDayClose);
//                System.out.println(backData.firstDayOpen);
//                System.out.println((backData.lastDayClose - backData.firstDayOpen) / backData.firstDayOpen);
//                System.out.println();
                tempRate += (backData.lastDayClose - backData.firstDayOpen) / backData.firstDayOpen;
            }
            strategyRates.add(tempRate / pickleData.stockCodes.size());
            yearProfitRate += tempRate / pickleData.stockCodes.size();
        }
        baseYearProfitRate = baseYearProfitRate / strategyConditionVO.beginDate.until(strategyConditionVO.endDate, ChronoUnit.DAYS) * 365;
        yearProfitRate = yearProfitRate / strategyConditionVO.beginDate.until(strategyConditionVO.endDate, ChronoUnit.DAYS) * 365;
    }

    @Override
    public void setStrategyConditionVO(StrategyConditionVO strategyConditionVO) {
        this.strategyConditionVO = strategyConditionVO;
    }

    @Override
    public void setStockPoolConditionVO(StockPoolConditionVO stockPoolConditionVO) {
        this.stockPoolConditionVO = stockPoolConditionVO;
    }

    @Override
    public void setStockPickIndexVOList(List<StockPickIndexVO> stockPickIndexVOs) {
        this.stockPickIndexVOs = stockPickIndexVOs;
    }

    @Override
    public BackDetailVO getBackDetailVO() {
        double beta = getCOV(strategyRates, baseRates) / getVariance(baseRates);

        /**
         * 无风险利率，使用中国1年期银行定期存款回报
         */
        double r = 0.0175;
        double alpha = (yearProfitRate - r) - beta * (baseYearProfitRate - r);

        double sharpRate = (yearProfitRate - r) / getSTD(strategyRates);

        double largestBackRate = getMaxDrawDown(strategyRates);

        BackDetailVO backDetailVO = new BackDetailVO(yearProfitRate, baseYearProfitRate, sharpRate, largestBackRate, alpha, beta);

        return backDetailVO;
    }

    @Override
    public List<DayRatePieceVO> getStrategyDayRatePieceVO() {
        List<DayRatePieceVO> dayRatePieceVOS = new ArrayList<>();
        for(int i = 0; i < pickleDatas.size(); i++) {
            DayRatePieceVO dayRatePieceVO = new DayRatePieceVO(pickleDatas.get(i).endDate, strategyRates.get(i));
            dayRatePieceVOS.add(dayRatePieceVO);
        }
        return dayRatePieceVOS;
    }

    @Override
    public List<DayRatePieceVO> getBaseDayRatePieceVO() {
        List<DayRatePieceVO> dayRatePieceVOS = new ArrayList<>();
        for(int i = 0; i < pickleDatas.size(); i++) {
            DayRatePieceVO dayRatePieceVO = new DayRatePieceVO(pickleDatas.get(i).endDate, baseRates.get(i));
            dayRatePieceVOS.add(dayRatePieceVO);
        }
        return dayRatePieceVOS;
    }

    @Override
    public List<BetterTableVO> getBetterTableVO() {
        return null;
    }

    @Override
    public List<BetterPieceVO> getOverProfitRateByFormation() {
        return null;
    }

    @Override
    public List<BetterPieceVO> getOverProfitRateByHolding() {
        return null;
    }

    @Override
    public List<BetterPieceVO> getWinRateByFormation() {
        return null;
    }

    @Override
    public List<BetterPieceVO> getWinProfitRateByHolding() {
        return null;
    }

    @Override
    public List<Double> getProfitDistributeBar() {
        return strategyRates;
    }

    @Override
    public ProfitDistributionPieVO getProfitDistributePie() {
        /**
         * 收益为-3.5%到0的次数
         */
        int green0 = 0;
        /**
         * 收益为-3.5%到-7.5%的次数
         */
        int green35 = 0;
        /**
         * 收益小于-7.5%的次数
         */
        int green75 = 0;
        /**
         * 收益为0到3.5%的次数
         */
        int red0 = 0;
        /**
         * 收益为3.5%到7.5%的次数
         */
        int red35 = 0;
        /**
         * 收益大于7.5%的次数
         */
        int red75 = 0;
        for(double strategyRate : strategyRates) {
            if(strategyRate < 0 && strategyRate >= -0.035)
                green0++;
            else if(strategyRate < -0.035 && strategyRate >= -0.075)
                green35++;
            else if(strategyRate < -0.075)
                green75++;
            else if(strategyRate < 0.035 && strategyRate >= 0)
                red0++;
            else if(strategyRate < 0.075 && strategyRate >= 0.035)
                red35++;
            else
                red75++;
        }
        ProfitDistributionPieVO profitDistributionPieVO = new ProfitDistributionPieVO(green0, green35, green75, red0, red35, red75);
        return profitDistributionPieVO;
    }


    /**
     * 获得协方差
     */
    private double getCOV(ArrayList<Double> a, ArrayList<Double> b) {
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
    private double getVariance(ArrayList<Double> a) {
        double ave = getAverage(a);
        double sum = 0.0;
        for(double i : a) {
            sum += Math.pow((i - ave), 2);
        }
        return sum / a.size();
    }

    private double getSTD(ArrayList<Double> a) {
        return Math.sqrt(getVariance(a));
    }

    /**
     * 获得平均值
     * @param a
     * @return
     */
    private double getAverage(ArrayList<Double> a) {
        double sum = 0.0;
        for(double temp : a)
            sum += temp;
        return sum / a.size();
    }

    /**
     * 获得最大回撤率
     * @param a
     * @return
     */
    private double getMaxDrawDown(ArrayList<Double> a) {
        double minSum = 0.0, thisSum = 0.0;

        for(int i = 1; i < a.size(); i++) {
            thisSum += a.get(i);
            if(thisSum < minSum)
                minSum = thisSum;
            else if(thisSum > 0)
                thisSum = 0;
        }
        return -minSum;
    }
}