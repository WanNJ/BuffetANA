package vo;

import java.time.LocalDate;

/**
 * Created by slow_time on 2017/3/5.
 */
public class DailyKLineVO {

    private String name;
    private String market;
    private String code;
    private LocalDate date;
    private double highPrice;
    private double lowPrice;
    private double openPrice;
    private double closePrice;
    private StockState state;
    private long volume;


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
    public double getHighPrice() {
        return highPrice;
    }
    public void setHighPrice(double high_Price) {
        this.highPrice = high_Price;
    }
    public double getLowPrice() {
        return lowPrice;
    }
    public void setLowPrice(double low_Price) {
        this.lowPrice = low_Price;
    }
    public double getOpenPrice() {
        return openPrice;
    }
    public void setOpenPrice(double open_Price) {
        this.openPrice = open_Price;
    }
    public double getClosePrice() {
        return closePrice;
    }
    public void setClosePrice(double close_Price) {
        this.closePrice = close_Price;
    }
    public StockState getState() {
        return state;
    }
    public void setState(StockState state) {
        this.state = state;
    }
    public long getVolume() {
        return volume;
    }
    public void setVolume(long volume) {
        this.volume = volume;
    }

}
