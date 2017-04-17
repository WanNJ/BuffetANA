package po;

import stockenum.StrategyType;
import vo.StrategyConditionVO;

import java.time.LocalDate;

/**
 * Created by wshwbluebird on 2017/3/24.
 */
public class StrategyConditionPO {

    /**
     * 策略类型
     */
    private StrategyType strategyType;
    private String beginDate;
    private String endDate;
    /**
     * 是否升序排列
     */
    private boolean asd;


    public StrategyConditionPO(){

    }

    /**
     *
     * @param strategyType
     * @param beginDate
     * @param endDate
     * @param asd
     */
    public StrategyConditionPO(StrategyType strategyType
                              , String beginDate, String endDate
                                , boolean asd) {
        this.strategyType = strategyType;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.asd =asd;
    }

    public StrategyConditionPO(StrategyConditionVO strategyConditionVO) {
        this.strategyType = strategyConditionVO.strategyType;
        this.beginDate = strategyConditionVO.beginDate.toString();
        this.endDate = strategyConditionVO.endDate.toString();
        this.asd =strategyConditionVO.asd;
    }


    public StrategyType getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(StrategyType strategyType) {
        this.strategyType = strategyType;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isAsd() {
        return asd;
    }

    public void setAsd(boolean asd) {
        this.asd = asd;
    }
}
