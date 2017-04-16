package po;

import stockenum.StrategyType;

import java.time.LocalDate;

/**
 * Created by wshwbluebird on 2017/3/24.
 */
public class StrategyConditionPO {

    /**
     * 策略类型
     */
    private StrategyType strategyType;
    private LocalDate beginDate;
    private LocalDate endDate;
    /**
     * 是否升序排列
     */
    private boolean asd;


    /**
     *
     * @param strategyType
     * @param beginDate
     * @param endDate
     * @param asd
     */
    public StrategyConditionPO(StrategyType strategyType
                              , LocalDate beginDate, LocalDate endDate
                                , boolean asd) {
        this.strategyType = strategyType;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.asd =asd;
    }


    public StrategyType getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(StrategyType strategyType) {
        this.strategyType = strategyType;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isAsd() {
        return asd;
    }

    public void setAsd(boolean asd) {
        this.asd = asd;
    }
}
