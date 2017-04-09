package util;

/**
 * Created by slow_time on 2017/4/9.
 */

/**
 * 股票策略评估结果
 * 里面每一个属性对应雷达图每一个角，范围都是0～20
 * 策略总得分范围是0～100
 */
public class StrategyScoreVO {
    /**
     * 盈利能力：策略的盈亏比(回测期间总利润除以总亏损)越大，该项分值越高；
     */
    int profitAbility;

    /**
     * 稳定性：策略的波动越小，该项分值越高
     */
    int stability;

    /**
     * 选股能力：策略的成功率越大，该项分值越高
     */
    int chooseStockAbility;

    /**
     * 绝对收益：策略的年化收益率越大，该项分值越高
     */
    int absoluteProfit;

    /**
     * 抗风险能力：策略的回撤越小，该项分值越高；
     */
    int antiRiskAbility;

    /**
     * 策略总得分，上面5项得分之和
     */
    int strategyScore;

    public StrategyScoreVO(int profitAbility, int stability, int chooseStockAbility, int absoluteProfit, int antiRiskAbility, int strategyScore) {
        this.profitAbility = profitAbility;
        this.stability = stability;
        this.chooseStockAbility = chooseStockAbility;
        this.absoluteProfit = absoluteProfit;
        this.antiRiskAbility = antiRiskAbility;
        this.strategyScore = strategyScore;
    }
}
