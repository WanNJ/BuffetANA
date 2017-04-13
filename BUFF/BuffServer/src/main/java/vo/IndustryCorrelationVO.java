package vo;

/**
 * Created by slow_time on 2017/4/12.
 */

import java.util.List;

/**
 * 行业相关性最高的股票的代号、名称、相关性、以及可能的盈利率
 */
public class IndustryCorrelationVO {

    public String code;
    public String name;
    /**
     * 相关度
     * 其绝对值小于1
     * 正，则为正相关；负则为负相关
     * 绝对值为1时，表示线性相关
     */
    public double correlation;
    /**
     * 预测的盈利率
     */
    public double profitRate;

    public List<Double> base;

    public List<Double> compare;

    public void setBase(List<Double> base) {
        this.base = base;
    }

    public void setCompare(List<Double> compare) {
        this.compare = compare;
    }

    public IndustryCorrelationVO(String code, String name, double correlation, double profitRate) {
        this.code = code;
        this.name = name;
        this.correlation = correlation;
        this.profitRate = profitRate;
    }
}
