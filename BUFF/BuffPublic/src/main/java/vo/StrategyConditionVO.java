package vo;

import stockenum.StrategyType;

import java.time.LocalDate;

/**
 * Created by slow_time on 2017/3/24.
 */
public class StrategyConditionVO {

    /**
     * 策略类型
     */
    public StrategyType strategyType;
    /**
     * 如果是动量模型，就是形成期
     * 如果是回归模型，就是均线期
     */
    public int formationPeriod;
    /**
     * 持仓期
     */
    public int holdingPeriod;
    public LocalDate beginDate;
    public LocalDate endDate;
    /**
     * 是否升序排列
     */
    public boolean asd;
    /**
     * 持有股票数
     */
    public int holdingNum;


    public StrategyConditionVO() {

    }

    public StrategyConditionVO(StrategyType strategyType, int formationPeriod,
                               int holdingPeriod, LocalDate beginDate, LocalDate endDate,
                               int holdingNum ,boolean asd) {
        this.strategyType = strategyType;
        this.formationPeriod = formationPeriod;
        this.holdingPeriod = holdingPeriod;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.holdingNum = holdingNum;
        this.asd =asd;
    }
}
