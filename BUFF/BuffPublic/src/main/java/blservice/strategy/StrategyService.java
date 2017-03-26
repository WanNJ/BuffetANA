package blservice.strategy;

import vo.*;

import java.util.List;

/**
 * Created by slow_time on 2017/3/24.
 */
public interface StrategyService {

     void init(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO,
              List<StockPickIndexVO> stockPickIndexVOs);

    void setStrategyConditionVO(StrategyConditionVO strategyConditionVO);

    void setStockPoolConditionVO(StockPoolConditionVO stockPoolConditionVO);

    void setStockPickIndexVOList(List<StockPickIndexVO> stockPickIndexVOs);
    /**
     * 获得回测表数据
     * @param strategyConditionVO
     * @param stockPoolConditionVO
     * @param stockPickIndexVOs
     * @return
     */
    BackDetailVO getBackDetailVO(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO,
                                 List<StockPickIndexVO> stockPickIndexVOs);

    /**
     * 获得回测累计收益比较图
     * @param strategyConditionVO
     * @param stockPoolConditionVO
     * @param stockPickIndexVOs
     * @return
     */
    List<DayRatePieceVO> getDayRatePieceVO(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO,
                                           List<StockPickIndexVO> stockPickIndexVOs);

    /**
     * 获得回测的形成期与持有期的TableView
     * @param strategyConditionVO
     * @param stockPoolConditionVO
     * @param stockPickIndexVOs
     * @return
     */
    List<BetterTableVO> getBetterTableVO(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO,
                                         List<StockPickIndexVO> stockPickIndexVOs);

    /**
     * 通过形成期获得超额收益率图
     * @param strategyConditionVO
     * @param stockPoolConditionVO
     * @param stockPickIndexVOs
     * @return
     */
    List<BetterPieceVO> getOverProfitRateByFormation(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO,
                                                     List<StockPickIndexVO> stockPickIndexVOs);

    /**
     * 通过持有期获得超额收益率图
     * @param strategyConditionVO
     * @param stockPoolConditionVO
     * @param stockPickIndexVOs
     * @return
     */
    List<BetterPieceVO> getOverProfitRateByHolding(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO,
                                                     List<StockPickIndexVO> stockPickIndexVOs);

    /**
     * 通过形成期获得策略胜率图
     * @param strategyConditionVO
     * @param stockPoolConditionVO
     * @param stockPickIndexVOs
     * @return
     */
    List<BetterPieceVO> getWinRateByFormation(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO,
                                                     List<StockPickIndexVO> stockPickIndexVOs);

    /**
     * 通过持有期获得策略胜率图
     * @param strategyConditionVO
     * @param stockPoolConditionVO
     * @param stockPickIndexVOs
     * @return
     */
    List<BetterPieceVO> getWinProfitRateByHolding(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO,
                                                   List<StockPickIndexVO> stockPickIndexVOs);

    /**
     * 获得收益分布直方图
     * @param strategyConditionVO
     * @param stockPoolConditionVO
     * @param stockPickIndexVOs
     * @return
     */
    List<Double> getProfitDistributeBar(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO,
                                        List<StockPickIndexVO> stockPickIndexVOs);

    /**
     * 获得收益分布饼图
     * @param strategyConditionVO
     * @param stockPoolConditionVO
     * @param stockPickIndexVOs
     * @return
     */
    ProfitDistributionPieVO getProfitDistributePie(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO,
                                                   List<StockPickIndexVO> stockPickIndexVOs);
}
