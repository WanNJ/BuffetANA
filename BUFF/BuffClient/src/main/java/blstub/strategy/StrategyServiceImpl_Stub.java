package blstub.strategy;

import blservice.strategy.StrategyService;
import blserviceimpl.strategy.PickleData;
import util.StrategyScoreVO;
import vo.*;

import java.time.LocalDate;
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
    public BackDetailVO getBackDetailVO() {
        return null;
    }

    @Override
    public List<DayRatePieceVO> getStrategyDayRatePieceVO() {
        return null;
    }

    @Override
    public List<DayRatePieceVO> getBaseDayRatePieceVO() {
        return null;
    }

    @Override
    public List<BetterTableVO> getBetterTableVO() {
        return null;
    }

    @Override
    public List<BetterTableVO> getBetterTableVOByFormation(int formationPeriod) {
        return null;
    }

    @Override
    public List<BetterTableVO> getBetterTableVOByHolding(int holdingPeriod) {
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
        return null;
    }

    @Override
    public ProfitDistributionPieVO getProfitDistributePie() {
        return null;
    }

    @Override
    public StrategyScoreVO getStrategyEstimateResult() {
        return null;
    }

    @Override
    public List<PickleData> getPickleData() {
        return Arrays.asList(new PickleData(LocalDate.now(),LocalDate.now(),null));
    }
}
