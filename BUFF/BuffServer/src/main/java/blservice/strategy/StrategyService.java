package blservice.strategy;

import vo.*;

import java.util.List;

/**
 * Created by slow_time on 2017/3/24.
 */
public interface StrategyService {

    void init(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO,
              List<StockPickIndexVO> stockPickIndexVOs);

    /**
     * 用户点击回测按钮后调用的方法
     * @param traceBackVO
     */
    void calculate(TraceBackVO traceBackVO);

    void setStrategyConditionVO(StrategyConditionVO strategyConditionVO);

    void setStockPoolConditionVO(StockPoolConditionVO stockPoolConditionVO);

    void setStockPickIndexVOList(List<StockPickIndexVO> stockPickIndexVOs);
    /**
     * 获得回测表数据
     * @return
     */
    BackDetailVO getBackDetailVO();

    /**
     * 获得回测累计收益比较图中的策略收益率累积折线图
     * @return
     */
    List<DayRatePieceVO> getStrategyDayRatePieceVO();

    /**
     * 获得回测累计收益比较图中的基准收益率累积折线图
     * @return
     */
    List<DayRatePieceVO> getBaseDayRatePieceVO();

    /**
     * 获得回测的形成期与持有期的TableView
     * @return
     */
    List<BetterTableVO> getBetterTableVO();

    /**
     * 通过形成期获得超额收益率图
     * @return
     */
    List<BetterPieceVO> getOverProfitRateByFormation();

    /**
     * 通过持有期获得超额收益率图
     * @return
     */
    List<BetterPieceVO> getOverProfitRateByHolding();

    /**
     * 通过形成期获得策略胜率图
     * @return
     */
    List<BetterPieceVO> getWinRateByFormation();

    /**
     * 通过持有期获得策略胜率图
     * @return
     */
    List<BetterPieceVO> getWinProfitRateByHolding();

    /**
     * 获得收益分布直方图
     * @return
     */
    List<Double> getProfitDistributeBar();

    /**
     * 获得收益分布饼图
     * @return
     */
    ProfitDistributionPieVO getProfitDistributePie();
}
