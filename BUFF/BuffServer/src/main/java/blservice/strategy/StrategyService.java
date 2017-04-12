package blservice.strategy;

import blserviceimpl.strategy.PickleData;
import util.StrategyScoreVO;
import vo.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by slow_time on 2017/3/24.
 */
public interface StrategyService {

    void init(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO,
              List<StockPickIndexVO> stockPickIndexVOs);

    /**
     * 混合策略的初始化
     * 或者是自定义策略
     */
    void initMixed(LocalDate beginDate , LocalDate endDate,
                   StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexVOs,
                   TraceBackVO traceBackVO, List<MixedStrategyVO> mixedStrategyVOS);

    /**
     * 用户点击回测按钮后调用的方法
     * @param traceBackVO
     */
    void calculate(TraceBackVO traceBackVO);

    void setStrategyConditionVO(StrategyConditionVO strategyConditionVO);

    void setStockPoolConditionVO(StockPoolConditionVO stockPoolConditionVO);

    void setStockPickIndexVOList(List<StockPickIndexVO> stockPickIndexVOs);

    void setTraceBackVO(TraceBackVO traceBackVO);
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
     * ！！！！！Attention！！！！！！
     * ！！！！！此接口已废弃！！！！！
     * 该接口在当时考虑不周，应该分割成getBetterTableVOByFormation，getBetterTableVOByHolding这两个接口
     * 获得回测的形成期与持有期的TableView
     * @return
     */
    List<BetterTableVO> getBetterTableVO();


    /**
     * !!!!!!!!Attention!!!!!!
     * 为了在逻辑层提高计算效率，写界面逻辑时，一定要保证！！！！
     * 此方法必须先于getOverProfitRateByFormation, getWinRateByFormation调用！！！
     * 通过形成期获得回测的形成期与持有期的TableView
     * @param formationPeriod 固定的形成期
     * @return
     */
    List<BetterTableVO> getBetterTableVOByFormation(int formationPeriod);

    /**
     * !!!!!!!!Attention!!!!!!
     * 为了在逻辑层提高计算效率，写界面逻辑时，一定要保证！！！！
     * 此方法必须先于getOverProfitRateByHolding，getWinProfitRateByHolding调用！！！
     * 通过持有期获得回测的形成期与持有期的TableView
     * @param holdingPeriod 固定的持仓期
     * @return
     */
    List<BetterTableVO> getBetterTableVOByHolding(int holdingPeriod);

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

    /**
     * 获得策略的评估结果
     * @return
     */
    StrategyScoreVO getStrategyEstimateResult();

    /**
     * 获取股票历史记录
     * @return
     */
    List<PickleData> getPickleData();
}
