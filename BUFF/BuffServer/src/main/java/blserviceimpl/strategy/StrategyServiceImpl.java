package blserviceimpl.strategy;

import blservice.strategy.StrategyService;
import dataservice.stockmap.StockNameToCodeDAO;
import dataservice.strategy.StrategyDAO;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;
import pick.PickStockService;
import pick.PickStockServiceImpl;
import po.StockNameAndCodePO;
import po.StockPoolConditionPO;
import util.StrategyScoreVO;
import vo.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by slow_time on 2017/3/24.
 */
public enum StrategyServiceImpl implements StrategyService {
    STRATEGY_SERVICE ;



    private StrategyConditionVO strategyConditionVO;
    private StockPoolConditionVO stockPoolConditionVO;
    private List<StockPickIndexVO> stockPickIndexVOs;
    private TraceBackVO traceBackVO;
    private DAOFactoryService daoFactoryService;
    PickStockService pickStockService;
    private StrategyDAO strategyDAO;
    private StockNameToCodeDAO stockNameToCodeDAO;
    private List<PickleData> pickleDatas;
    private List<NewPickleData> newpickleDatas;
    private List<BetterTableVO> betterTableVOSByFormation;
    private List<BetterTableVO> betterTableVOSByHolding;

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
     * 最大回撤率
     */
    private double largestBackRate;


    /**
     * 可以用于后期test时将工厂替换成stub
     * @param daoFactoryService
     */
    public void setFactory(DAOFactoryService daoFactoryService) {
        this.daoFactoryService = daoFactoryService;
        this.stockNameToCodeDAO = daoFactoryService.createStockNameToCodeDAO();
        this.strategyDAO = this.daoFactoryService.createStrategyDAO();
        this.newpickleDatas = strategyDAO.getNewPickleData(strategyConditionVO, stockPoolConditionVO, stockPickIndexVOs);
    }

    public void setPickStockService(PickStockService pickStockService){
        this.pickStockService = pickStockService;
    }

    @Override
    public void init(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexVOs) {
        this.strategyConditionVO = strategyConditionVO;
        this.stockPoolConditionVO = stockPoolConditionVO;
        this.stockPickIndexVOs = stockPickIndexVOs;
        setFactory(new DAOFactoryServiceImpl());
        setPickStockService(PickStockServiceImpl.PICK_STOCK_SERVICE);

        //this.newpickleDatas = strategyDAO.getNewPickleData()
    }

    @Override
    public void initMixed(LocalDate beginDate ,  LocalDate endDate ,
                          StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexVOs,
                          TraceBackVO traceBackVO, List<MixedStrategyVO> mixedStrategyVOS) {

        //不知道用没用   strategyConditionVO  先将能设好的设好

        this.strategyConditionVO = new StrategyConditionVO(null , beginDate , endDate,false);
        this.stockPoolConditionVO = stockPoolConditionVO;
        this.stockPickIndexVOs = stockPickIndexVOs;
        this.traceBackVO = traceBackVO;
        this.daoFactoryService = new DAOFactoryServiceImpl();
        this.strategyDAO = this.daoFactoryService.createStrategyDAO();
        this.stockNameToCodeDAO = daoFactoryService.createStockNameToCodeDAO();


        this.pickleDatas = strategyDAO.getPickleData(beginDate,endDate,stockPoolConditionVO,
                stockPickIndexVOs,traceBackVO,mixedStrategyVOS);

        // 初始化一些必要的重复计算的参数
        baseYearProfitRate = 0.0;
        yearProfitRate = 0.0;
        baseRates = new ArrayList<>();
        strategyRates = new ArrayList<>();
        pickleDatas= pickleDatas.stream().filter(t->t.stockCodes.size()>0).collect(Collectors.toList());
        double sumOfBase = 100000;
        double sumOfStrategy = 100000;
        for(PickleData pickleData : pickleDatas) {
            double tempRate = 0.0;
            sumOfBase += sumOfBase * pickleData.baseProfitRate;
            baseRates.add(pickleData.baseProfitRate);



            double buyMoney = 0;
            double sellMoney = 0;

            double perStockMoney = sumOfStrategy / pickleData.stockCodes.size();
            for(BackData backData : pickleData.stockCodes) {

                double cnt = 100/ backData.firstDayOpen;
                buyMoney+=100;
                sellMoney+= cnt * backData.lastDayClose;
                tempRate += (sellMoney-buyMoney)/buyMoney;
            }
            strategyRates.add(tempRate / pickleData.stockCodes.size());
            sumOfStrategy += sumOfStrategy * (tempRate / pickleData.stockCodes.size());
        }
        baseYearProfitRate = (sumOfBase - 100000) / 100000;
        baseYearProfitRate = baseYearProfitRate / strategyConditionVO.beginDate.until(strategyConditionVO.endDate, ChronoUnit.DAYS) * 365;
        yearProfitRate = (sumOfStrategy - 100000) / 100000;
        yearProfitRate = yearProfitRate / strategyConditionVO.beginDate.until(strategyConditionVO.endDate, ChronoUnit.DAYS) * 365;


    }


    @Override
    public void calculate(TraceBackVO traceBackVO) {
        setTraceBackVO(traceBackVO);

        this.pickleDatas = getOldPickleData(traceBackVO.holdingNum,traceBackVO.holdingPeriod,
                traceBackVO.formationPeriod,traceBackVO.holdingRate);
        // 初始化一些必要的重复计算的参数
        baseYearProfitRate = 0.0;
        yearProfitRate = 0.0;
        baseRates = new ArrayList<>();
        strategyRates = new ArrayList<>();
        pickleDatas= pickleDatas.stream().filter(t->t.stockCodes.size()>0).collect(Collectors.toList());
        double sumOfBase = 100000;
        double sumOfStrategy = 100000;
        for(PickleData pickleData : pickleDatas) {
            double tempRate = 0.0;
            baseRates.add(pickleData.baseProfitRate);
            sumOfBase += sumOfBase * pickleData.baseProfitRate;


            double buyMoney = 0;
            double sellMoney = 0;

            for(BackData backData : pickleData.stockCodes) {

                double cnt = 100/ backData.firstDayOpen;
                buyMoney+=100;
                sellMoney+= cnt * backData.lastDayClose;
                tempRate += (sellMoney-buyMoney)/buyMoney ;
            }
            strategyRates.add(tempRate / pickleData.stockCodes.size());
            sumOfStrategy += sumOfStrategy * (tempRate / pickleData.stockCodes.size());
        }
        baseYearProfitRate = (sumOfBase - 100000) / 100000;
        baseYearProfitRate = baseYearProfitRate / strategyConditionVO.beginDate.until(strategyConditionVO.endDate, ChronoUnit.DAYS) * 365;
        yearProfitRate = (sumOfStrategy - 100000) / 100000;
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
    public void setTraceBackVO(TraceBackVO traceBackVO) {
        this.traceBackVO = traceBackVO;
    }

    @Override
    public List<StockNameAndCodeVO> getAllStocksInPool() {
        List<String> codes = strategyDAO.getStocksInPool(new StockPoolConditionPO(stockPoolConditionVO));
        List<StockNameAndCodePO> stockNameAndCodePOS = stockNameToCodeDAO.getNameToCodeMap();
        return stockNameAndCodePOS.stream().filter(t -> codes.contains(t.getCode())).map(t -> new StockNameAndCodeVO(t.getName(), t.getCode())).collect(Collectors.toList());
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
        this.largestBackRate = largestBackRate;

        BackDetailVO backDetailVO = new BackDetailVO(yearProfitRate, baseYearProfitRate, sharpRate, largestBackRate, alpha, beta);

        return backDetailVO;
    }

    @Override
    public List<DayRatePieceVO> getStrategyDayRatePieceVO() {
        List<DayRatePieceVO> dayRatePieceVOS = new ArrayList<>();
        double sum = 100000.0;
        for(int i = 0; i < pickleDatas.size(); i++) {
            sum += strategyRates.get(i) * sum;
            DayRatePieceVO dayRatePieceVO = new DayRatePieceVO(pickleDatas.get(i).endDate, (sum - 100000.0) / 100000.0 * 100);
            dayRatePieceVOS.add(dayRatePieceVO);
        }
        return dayRatePieceVOS;
    }

    @Override
    public List<DayRatePieceVO> getBaseDayRatePieceVO() {
        List<DayRatePieceVO> dayRatePieceVOS = new ArrayList<>();
        double sum = 100000.0;
        for(int i = 0; i < pickleDatas.size(); i++) {
            sum += baseRates.get(i) * sum;
            DayRatePieceVO dayRatePieceVO = new DayRatePieceVO(pickleDatas.get(i).endDate, (sum - 100000.0) / 100000.0 * 100);
            dayRatePieceVOS.add(dayRatePieceVO);
        }
        return dayRatePieceVOS;
    }

    @Override
    public List<BetterTableVO> getBetterTableVO() {
        return null;
    }

    @Override
    public List<BetterTableVO> getBetterTableVOByFormation(int formationPeriod) {
        betterTableVOSByFormation = new ArrayList<>();
        for(int holdingPeriod = 2; holdingPeriod <= 60; holdingPeriod += 2) {
            calculate(new TraceBackVO(formationPeriod, holdingPeriod, traceBackVO.holdingNum, traceBackVO.holdingRate));
            // 计算策略赢的次数
            double winCount = 0;
            for(int i = 0; i < strategyRates.size(); i++) {
                if(strategyRates.get(i) > baseRates.get(i))
                    winCount++;
            }

            betterTableVOSByFormation.add(new BetterTableVO(holdingPeriod, (yearProfitRate - baseYearProfitRate) / baseRates.size() * 100, winCount / baseRates.size() * 100));
        }
        return betterTableVOSByFormation;
    }

    @Override
    public List<BetterTableVO> getBetterTableVOByHolding(int holdingPeriod) {
        betterTableVOSByHolding = new ArrayList<>();
        for(int formationPeriod = 2; formationPeriod <= 60; formationPeriod += 2) {
            calculate(new TraceBackVO(formationPeriod, holdingPeriod, traceBackVO.holdingNum, traceBackVO.holdingRate));
            // 计算策略赢的次数
            double winCount = 0;
            for(int i = 0; i < strategyRates.size(); i++) {
                if(strategyRates.get(i) > baseRates.get(i))
                {
                    winCount++;
                }
            }
            double winRate = winCount / baseRates.size();
            betterTableVOSByHolding.add(new BetterTableVO(formationPeriod, (yearProfitRate - baseYearProfitRate) / baseRates.size() * 100, winRate * 100));
        }
        return betterTableVOSByHolding;
    }

    @Override
    public List<BetterPieceVO> getOverProfitRateByFormation() {
        return betterTableVOSByFormation.stream().map(t -> new BetterPieceVO(t.period, t.overProfitRate)).collect(Collectors.toList());
    }

    @Override
    public List<BetterPieceVO> getOverProfitRateByHolding() {
        return betterTableVOSByHolding.stream().map(t -> new BetterPieceVO(t.period, t.overProfitRate)).collect(Collectors.toList());
    }

    @Override
    public List<BetterPieceVO> getWinRateByFormation() {
        return betterTableVOSByFormation.stream().map(t -> new BetterPieceVO(t.period, t.winRate)).collect(Collectors.toList());
    }

    @Override
    public List<BetterPieceVO> getWinProfitRateByHolding() {
        return betterTableVOSByHolding.stream().map(t -> new BetterPieceVO(t.period, t.winRate)).collect(Collectors.toList());
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

    @Override
    public StrategyScoreVO getStrategyEstimateResult() {

        int antiRiskAbility = (int)Math.round(20 - largestBackRate * 20);
        if (antiRiskAbility > 20)
            antiRiskAbility = 20;
        else if (antiRiskAbility < 0)
            antiRiskAbility = 0;

        int absoluteProfit = (int)Math.round(yearProfitRate / 2 * 20);
        if (absoluteProfit > 20)
            absoluteProfit = 20;
        else if (absoluteProfit < 0)
            absoluteProfit = 0;

        // 计算策略赢的次数
        double winCount = 0;
        //股票的盈亏比率
        double winRate = 0;
        double loseRate = 0;
        //最大赢率
        double maxWinRate = -1;
        //最大亏损率
        double maxLoseRate = 1;
        for(int i = 0; i < strategyRates.size(); i++) {
            if(strategyRates.get(i) > baseRates.get(i))
                winCount++;
            if(strategyRates.get(i) > 0)
                winRate += strategyRates.get(i);
            else
                loseRate += strategyRates.get(i);

            if(strategyRates.get(i) > maxWinRate)
                maxWinRate = strategyRates.get(i);
            else if(strategyRates.get(i) < maxLoseRate)
                maxLoseRate = strategyRates.get(i);
        }
        int chooseStockAbility = (int)Math.round(winCount / baseRates.size() * 20);
        if(chooseStockAbility > 20)
            chooseStockAbility = 20;
        else if (chooseStockAbility < 0)
            chooseStockAbility = 0;

        int profitAbility = 0;
        if (loseRate == 0)
            profitAbility = 20;
        else {
            profitAbility = (int)Math.round(winRate / (-loseRate) / 5 * 20);
            if (profitAbility > 20)
                profitAbility = 20;
            else if (profitAbility < 0)
                profitAbility = 0;
        }

        int stability = 20 - (int)Math.round((maxWinRate - maxLoseRate) / 2  * 20);
        if (stability > 20)
            stability = 20;
        else if (stability < 0)
            stability = 0;

        int strategyScore = profitAbility + stability + chooseStockAbility + absoluteProfit + antiRiskAbility;

        StrategyScoreVO strategyScoreVO = new StrategyScoreVO(profitAbility, stability, chooseStockAbility,
                absoluteProfit, antiRiskAbility, strategyScore);

        return strategyScoreVO;
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

    /**
     *
     * @param holdingNum 如果没有就传到0
     * @param holdingPeriod
     * @param formationPeriod
     * @param holdingRate 如果没有就传0
     * @return
     */
    private List<PickleData>  getOldPickleData(int holdingNum , int holdingPeriod , int formationPeriod , double holdingRate){

        //seprate day
        List<PickleData> sepPickle = pickStockService.seprateDaysByTrade
                (strategyConditionVO.beginDate,strategyConditionVO.endDate,holdingPeriod);

        // chaneg new pickle to old
        sepPickle = pickleDataNewToOld(sepPickle,formationPeriod);



        //rank and filter
        sepPickle = strategyDAO.rankAndFilterPickleData
                        (sepPickle,stockPickIndexVOs,strategyConditionVO.strategyType
                                ,holdingNum,holdingRate,strategyConditionVO.asd);



        return sepPickle;
    }

    /**
     * 将新的PickleData  根据时间划分 和 形成期 换成 老的PickleData
     * @param oldPickleData
     * @param formationPeriod
     * @return
     */
    private List<PickleData> pickleDataNewToOld(List<PickleData> oldPickleData,int formationPeriod){

        for(NewPickleData newPickleData : this.newpickleDatas){


            //记录newPickleData跑到哪里的指针
            int js = 0;
            String code  = newPickleData.code;
            for(int i = 0 ; i < oldPickleData.size() ; i++) {
                boolean isStop = false;
                double buy = 0;
                double sell= 0;
                PickleData pickleData = oldPickleData.get(i);
                while(newPickleData.singleBackDataList.get(js).date.isBefore(pickleData.beginDate)){
                    js++;
                }
                buy =  js == 0 ? newPickleData.lastAdj:newPickleData.singleBackDataList.get(js-1).AdjClose;
                Number rank = newPickleData.singleBackDataList.get(js).rankValues[formationPeriod];
                Number[] filters =  newPickleData.singleBackDataList.get(js).rilterValues;

                while(newPickleData.singleBackDataList.get(js).date.isBefore(pickleData.endDate)){
                    if(newPickleData.singleBackDataList.get(js).volume==0){
                        isStop = true;
                        break;
                    }
                    js++;
                }
                if(newPickleData.singleBackDataList.get(js).volume==0) isStop = true;

                sell = newPickleData.singleBackDataList.get(js).AdjClose;

                if(!isStop){
                    pickleData.stockCodes.add(new BackData(code,rank,buy,sell,filters));
                }
            }

            //TODO计算基本收益率


            for(PickleData pickleData : oldPickleData) {
                double sum = 0.0;
                double buyMoney = 0;
                double sellMoney = 0;

                for(BackData backData : pickleData.stockCodes) {
                   // sum += (backData.lastDayClose - backData.firstDayOpen) / backData.firstDayOpen;
                    double cnt = 100/ backData.firstDayOpen;
                    buyMoney+=100;
                    sellMoney+= cnt * backData.lastDayClose;
                    sum += (sellMoney-buyMoney)/buyMoney;
                }
                pickleData.baseProfitRate = sum / pickleData.stockCodes.size();
            }


        }



        return oldPickleData;
    }



    @Override
    public List<PickleData> getPickleData() {
        return this.pickleDatas;
    }

}
