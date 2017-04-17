package vo;

import po.StrategyConditionPO;
import stockenum.StrategyType;
import util.DateUtil;

import java.time.LocalDate;

/**
 * Created by slow_time on 2017/3/24.
 */
public class StrategyConditionVO {

    /**
     * 策略类型
     */
    public StrategyType strategyType;
    public LocalDate beginDate;
    public LocalDate endDate;
    /**
     * 是否升序排列
     */
    public boolean asd;




    public StrategyConditionVO(StrategyType strategyType
                              , LocalDate beginDate, LocalDate endDate
                                ,boolean asd) {
        this.strategyType = strategyType;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.asd =asd;
    }

    public StrategyConditionVO(StrategyConditionPO strategyConditionPO) {
        this.strategyType = strategyConditionPO.getStrategyType();
        this.beginDate = DateUtil.fromToString(strategyConditionPO.getBeginDate());
        this.endDate = DateUtil.fromToString(strategyConditionPO.getEndDate());
        this.asd =strategyConditionPO.isAsd();
    }
}
