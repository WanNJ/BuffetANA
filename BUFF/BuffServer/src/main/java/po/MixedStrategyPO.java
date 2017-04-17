package po;

import stockenum.StrategyType;
import vo.MixedStrategyVO;

/**
 * Created by wshwbluebird on 2017/4/9.
 */
public class MixedStrategyPO {
    private StrategyType strategyType;
    /**
     * 权重
     */
    private double weight;
    /**
     * 排序方式
     */
    private boolean asc;

    /**
     * 持仓期
     */
    private int formationPeriod;

    public MixedStrategyPO(){

    }

    /**
     *
     * @param strategyType
     * @param weight
     * @param asc
     * @param formationPeriod
     */
    public MixedStrategyPO(StrategyType strategyType, double weight, boolean asc, int formationPeriod) {
        this.strategyType = strategyType;
        this.weight = weight;
        this.asc = asc;
        this.formationPeriod = formationPeriod;
    }

    public MixedStrategyPO(MixedStrategyVO mixedStrategyVO) {
        this.strategyType = mixedStrategyVO.strategyType;
        this.weight = mixedStrategyVO.weight;
        this.asc = mixedStrategyVO.asc;
        this.formationPeriod = mixedStrategyVO.formationPeriod;
    }

    public StrategyType getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(StrategyType strategyType) {
        this.strategyType = strategyType;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public int getFormationPeriod() {
        return formationPeriod;
    }

    public void setFormationPeriod(int formationPeriod) {
        this.formationPeriod = formationPeriod;
    }


}
