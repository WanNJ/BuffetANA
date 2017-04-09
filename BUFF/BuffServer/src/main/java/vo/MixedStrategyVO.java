package vo;

import stockenum.StrategyType;

/**
 * Created by slow_time on 2017/4/9.
 */
public class MixedStrategyVO {
    StrategyType strategyType;
    /**
     * 权重
     */
    double weight;
    /**
     * 排序方式
     */
    boolean asc;

    public MixedStrategyVO(StrategyType strategyType, double weight, boolean asc) {
        this.strategyType = strategyType;
        this.weight = weight;
        this.asc = asc;
    }
}
