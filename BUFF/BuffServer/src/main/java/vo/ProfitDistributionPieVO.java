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

}
