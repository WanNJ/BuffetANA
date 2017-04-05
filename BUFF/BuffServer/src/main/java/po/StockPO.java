package po;

import java.time.LocalDate;

/**
 * Created by slow_time on 2017/3/3.
 */
public class StockPO {

    private String name; //股票名字
    private String market; //市场
    private String code;  //股票代码
    private LocalDate date; //日期
    private double high_Price; //日期最高价
    private double low_Price;  //日期最低价
    private double open_Price; //日期开盘价
    private double close_Price; //日期收盘价
    private long volume; //日期成交量
    private double adjCloseIndex; //日期复盘收益指数


    public StockPO() {

    }

    /**
     * add by wsw 用于剔除
     * @param code
     * @param date
     */
    public StockPO(String code , LocalDate date) {
        this.code = code;
        this.date = date;
    }

    public StockPO(String name, String market, String code, LocalDate date, double high_Price, double low_Price,
                   double open_Price, double close_Price, long volume, double adjCloseIndex) {
        this.name = name;
        this.market = market;
        this.code = code;
        this.date = date;
        this.high_Price = high_Price;
        this.low_Price = low_Price;
        this.open_Price = open_Price;
        this.close_Price = close_Price;
        this.volume = volume;
        this.adjCloseIndex = adjCloseIndex;

    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMarket() {
        return market;
    }
    public void setMarket(String market) {
        this.market = market;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public double getHigh_Price() {
        return high_Price;
    }
    public void setHigh_Price(double high_Price) {
        this.high_Price = high_Price;
    }
    public double getLow_Price() {
        return low_Price;
    }
    public void setLow_Price(double low_Price) {
        this.low_Price = low_Price;
    }
    public double getOpen_Price() {
        return open_Price;
    }
    public void setOpen_Price(double open_Price) {
        this.open_Price = open_Price;
    }
    public double getClose_Price() {
        return close_Price;
    }
    public void setClose_Price(double close_Price) {
        this.close_Price = close_Price;
    }
    public long getVolume() {
        return volume;
    }
    public void setVolume(long volume) {
        this.volume = volume;
    }
    public double getAdjCloseIndex() { return adjCloseIndex; }
    public void setAdjCloseIndex(double adjCloseIndex) { this.adjCloseIndex = adjCloseIndex; }



    @Override
    public boolean equals(Object obj) {
        StockPO po1 = this;
        StockPO po2 = (StockPO)obj;
        return po1.date.isEqual(po2.getDate()) && po1.code.equals(po2.code);
    }
}
