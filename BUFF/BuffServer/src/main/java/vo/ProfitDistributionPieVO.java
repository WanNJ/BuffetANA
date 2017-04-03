package vo;

/**
 * Created by slow_time on 2017/3/24.
 */
public class ProfitDistributionPieVO {

    /**
     * 收益为-3.5%到0的次数
     */
    public int green0;
    /**
     * 收益为-3.5%到-7.5%的次数
     */
    public int green35;
    /**
     * 收益小于-7.5%的次数
     */
    public int green75;
    /**
     * 收益为0到3.5%的次数
     */
    public int red0;
    /**
     * 收益为3.5%到7.5%的次数
     */
    public int red35;
    /**
     * 收益大于7.5%的次数
     */
    public int red75;

    public ProfitDistributionPieVO(int green0, int green35, int green75, int red0, int red35, int red75) {
        this.green0 = green0;
        this.green35 = green35;
        this.green75 = green75;
        this.red0 = red0;
        this.red35 = red35;
        this.red75 = red75;
    }
}
