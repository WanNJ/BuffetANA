package vo;

import java.io.Serializable;

/**
 * Created by slow_time on 2017/3/12.
 */
public class MarketStockDetailVO implements Serializable {

    public String code;
    public String name;
    public double currentPrice;
    /**
     * 涨跌价格
     */
    public double changeValue;
    /**
     * 涨跌幅
     */
    public double changeValueRange;

    public MarketStockDetailVO() {

    }

    public MarketStockDetailVO(String code, String name, double currentPrice, double changeValue, double changeValueRange) {
        this.code = code;
        this.name = name;
        this.currentPrice = currentPrice;
        this.changeValue = changeValue;
        this.changeValueRange = changeValueRange;
    }
}
