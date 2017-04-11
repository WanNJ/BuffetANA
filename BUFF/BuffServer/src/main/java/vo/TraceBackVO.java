package vo;

/**
 * Created by slow_time on 2017/4/7.
 */
public class TraceBackVO {

    /**
     * 如果是动量模型，就是形成期
     * 如果是回归模型，就是均线期
     */
    public int formationPeriod;
    /**
     * 持仓期
     */
    public int holdingPeriod;

    /**
     * 持有股票数
     */
    public int holdingNum;

    /**
     * 注意！！！！！必须手动注入！！！！！！
     * 动量策略中的取前百分之多少的股票
     */
    public double holdingRate;

    /**
     *
     * @param formationPeriod
     * @param holdingPeriod
     * @param holdingNum
     */
    public TraceBackVO(int formationPeriod, int holdingPeriod, int holdingNum) {
        this.formationPeriod = formationPeriod;
        this.holdingPeriod = holdingPeriod;
        this.holdingNum = holdingNum;
        this.holdingRate = 0.0;
    }

    /**
     *
     * @param formationPeriod
     * @param holdingPeriod
     * @param holdingNum
     * @param holdingRate
     */
    public TraceBackVO(int formationPeriod, int holdingPeriod, int holdingNum, double holdingRate) {
        this.formationPeriod = formationPeriod;
        this.holdingPeriod = holdingPeriod;
        this.holdingNum = holdingNum;
        this.holdingRate = holdingRate;
    }
}
