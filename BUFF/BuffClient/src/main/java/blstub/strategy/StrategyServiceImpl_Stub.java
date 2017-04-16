package blstub.strategy;

import blservice.strategy.StrategyService;
import blserviceimpl.strategy.BackData;
import blserviceimpl.strategy.PickleData;
import util.StrategyScoreVO;
import vo.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by asus-a on 2017/4/12.
 */
public class StrategyServiceImpl_Stub implements StrategyService{
    @Override
    public void init(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexVOs) {

    }

    @Override
    public void initMixed(LocalDate beginDate, LocalDate endDate, StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexVOs, TraceBackVO traceBackVO, List<MixedStrategyVO> mixedStrategyVOS) {

    }

    @Override
    public void calculate(TraceBackVO traceBackVO) {

    }

    @Override
    public void setStrategyConditionVO(StrategyConditionVO strategyConditionVO) {

    }

    @Override
    public void setStockPoolConditionVO(StockPoolConditionVO stockPoolConditionVO) {

    }

    @Override
    public void setStockPickIndexVOList(List<StockPickIndexVO> stockPickIndexVOs) {

    }

    @Override
    public void setTraceBackVO(TraceBackVO traceBackVO) {

    }

    @Override
    public List<StockNameAndCodeVO> getAllStocksInPool() {
        return null;
    }

    @Override
    public BackDetailVO getBackDetailVO() {
        return new BackDetailVO(0.357, 0.124, 1.29, 0.238, 0.146, 0.97);
    }

    @Override
    public List<DayRatePieceVO> getStrategyDayRatePieceVO() {
        List<DayRatePieceVO> list = new ArrayList<>();
        list.add(new DayRatePieceVO(LocalDate.of(2000, 1, 1), -0.1));
        list.add(new DayRatePieceVO(LocalDate.of(2000, 2, 1), 0.2));
        list.add(new DayRatePieceVO(LocalDate.of(2000, 3, 1), 0.25));
        list.add(new DayRatePieceVO(LocalDate.of(2000, 4, 1), 0.7));
        return list;
    }

    @Override
    public List<DayRatePieceVO> getBaseDayRatePieceVO() {
        List<DayRatePieceVO> list = new ArrayList<>();
        list.add(new DayRatePieceVO(LocalDate.of(2000, 1, 1), -0.4));
        list.add(new DayRatePieceVO(LocalDate.of(2000, 2, 1), 0.1));
        list.add(new DayRatePieceVO(LocalDate.of(2000, 3, 1), 0.2));
        list.add(new DayRatePieceVO(LocalDate.of(2000, 4, 1), 0.5));
        return list;
    }

    @Override
    public List<BetterTableVO> getBetterTableVO() {
        return null;
    }

    @Override
    public List<BetterTableVO> getBetterTableVOByFormation(int formationPeriod) {
        return Arrays.asList(new BetterTableVO(2,0.8,58),
                new BetterTableVO(4,2.9,65),
                new BetterTableVO(6,3.0,65),
                new BetterTableVO(8,2.7,58),
                new BetterTableVO(10,2.5,60));
    }

    @Override
    public List<BetterTableVO> getBetterTableVOByHolding(int holdingPeriod) {
        return Arrays.asList(new BetterTableVO(2,0.8,58),
                new BetterTableVO(4,2.9,65),
                new BetterTableVO(6,3.0,65),
                new BetterTableVO(8,2.7,58),
                new BetterTableVO(10,2.5,60));
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
        return null;
    }

    @Override
    public ProfitDistributionPieVO getProfitDistributePie() {
        return null;
    }

    @Override
    public StrategyScoreVO getStrategyEstimateResult() {
        return new StrategyScoreVO(20, 21, 22, 23, 16, 92);
    }

    @Override
    public List<PickleData> getPickleData() {
        List<BackData> stockCodes=Arrays.asList(new BackData("code1",null,3.155,3.333),
                new BackData("code2",null,3.155,3.333),
                new BackData("code3",null,3.155,3.333),
                new BackData("code4",null,3.155,3.333),
                new BackData("code5",null,3.155,3.333),
                new BackData("code6",null,3.155,3.333));
        return Arrays.asList(new PickleData(LocalDate.now(),LocalDate.now(),stockCodes),
                new PickleData(LocalDate.now(),LocalDate.now(),stockCodes),
                new PickleData(LocalDate.now(),LocalDate.now(),stockCodes),
                new PickleData(LocalDate.now(),LocalDate.now(),stockCodes));
    }
}
