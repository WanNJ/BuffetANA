package vo;

import java.io.Serializable;

/**
 * Created by slow_time on 2017/3/5.
 */
public class StockDetailVO implements Serializable {
    public double highPrice;
    public double lowPrice;
    public double openPrice;
    public double closePrice;
    public  long vol;
    /**
     * 复权收盘指数
     */
    public double adjCloseIndex;

    public StockDetailVO() {

    }

    public StockDetailVO(double highPrice, double lowPrice, double openPrice, double closePrice, long vol, double adjCloseIndex) {
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.vol = vol;
        this.adjCloseIndex = adjCloseIndex;
    }
}
