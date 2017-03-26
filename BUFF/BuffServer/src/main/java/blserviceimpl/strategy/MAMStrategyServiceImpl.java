package blserviceimpl.strategy;

import blservice.strategy.StrategyService;
import vo.*;

import java.util.List;

/**
 * Created by slow_time on 2017/3/24.
 */
public class MAMStrategyServiceImpl implements StrategyService {

    private StrategyConditionVO strategyConditionVO;
    private StockPoolConditionVO stockPoolConditionVO;
    private List<StockPickIndexVO> stockPickIndexVOs;


    private  List<PickleData> getData() {
        return null;
    }

    @Override
    public void init(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexVOs) {

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
    public BackDetailVO getBackDetailVO(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexVOs) {
        return null;
    }

    @Override
    public List<DayRatePieceVO> getDayRatePieceVO(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexVOs) {
        return null;
    }

    @Override
    public List<BetterTableVO> getBetterTableVO(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexVOs) {
        return null;
    }

    @Override
    public List<BetterPieceVO> getOverProfitRateByFormation(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexVOs) {
        return null;
    }

    @Override
    public List<BetterPieceVO> getOverProfitRateByHolding(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexVOs) {
        return null;
    }

    @Override
    public List<BetterPieceVO> getWinRateByFormation(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexVOs) {
        return null;
    }

    @Override
    public List<BetterPieceVO> getWinProfitRateByHolding(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexVOs) {
        return null;
    }

    @Override
    public List<Double> getProfitDistributeBar(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexVOs) {
        return null;
    }

    @Override
    public ProfitDistributionPieVO getProfitDistributePie(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexVOs) {
        return null;
    }
}
