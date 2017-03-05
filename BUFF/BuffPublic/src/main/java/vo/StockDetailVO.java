package vo;

/**
 * Created by slow_time on 2017/3/5.
 */
public class StockDetailVO {
    double highPrice;
    double lowPrice;
    double openPrice;
    double closePrice;
    long vol;
    /**
     * 复权收盘指数
     */
    double adjCloseIndex;

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
