package vo;

import java.io.Serializable;

/**
 * Created by Accident on 2017/3/9.
 */
public class BasisAnalysisVO implements Serializable{
    public double highPrice;
    public double lowPrice;
    public double openPrice;
    public double closePrice;
    public double changeRate;

    public BasisAnalysisVO() {
        highPrice = 0;
        lowPrice = Double.MAX_VALUE;
        openPrice = 0;
        closePrice = 0;
        changeRate = 0;
    }
}
