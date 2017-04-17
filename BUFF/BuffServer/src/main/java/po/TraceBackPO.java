package po;

import vo.TraceBackVO;

/**
 * Created by slow_time on 2017/4/7.
 */
public class TraceBackPO {

    /**
     * 如果是动量模型，就是形成期
     * 如果是回归模型，就是均线期
     */
    private int formationPeriod;
    /**
     * 持仓期
     */
    private int holdingPeriod;

    /**
     * 持有股票数
     */
    private int holdingNum;

    /**
     * 动量策略中的取前百分之多少的股票
     */
    private double holdingRate;


    public  TraceBackPO(){

    }

     /**
     * @param formationPeriod
     * @param holdingPeriod
     * @param holdingNum
     * @param holdingRate
     */
    public TraceBackPO(int formationPeriod, int holdingPeriod, int holdingNum, double holdingRate) {
        this.formationPeriod = formationPeriod;
        this.holdingPeriod = holdingPeriod;
        this.holdingNum = holdingNum;
        this.holdingRate = holdingRate;
    }

    public TraceBackPO(TraceBackVO traceBackVO) {
        this.formationPeriod = traceBackVO.formationPeriod;
        this.holdingPeriod = traceBackVO.holdingPeriod;
        this.holdingNum = traceBackVO.holdingNum;
        this.holdingRate = traceBackVO.holdingRate;
    }


    public int getFormationPeriod() {
        return formationPeriod;
    }

    public void setFormationPeriod(int formationPeriod) {
        this.formationPeriod = formationPeriod;
    }

    public int getHoldingPeriod() {
        return holdingPeriod;
    }

    public void setHoldingPeriod(int holdingPeriod) {
        this.holdingPeriod = holdingPeriod;
    }

    public int getHoldingNum() {
        return holdingNum;
    }

    public void setHoldingNum(int holdingNum) {
        this.holdingNum = holdingNum;
    }

    public double getHoldingRate() {
        return holdingRate;
    }

    public void setHoldingRate(double holdingRate) {
        this.holdingRate = holdingRate;
    }
}
