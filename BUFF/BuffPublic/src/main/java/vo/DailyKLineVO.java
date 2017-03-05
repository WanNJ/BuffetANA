package vo;

import java.time.LocalDate;

/**
 * Created by slow_time on 2017/3/5.
 */
public class DailyKLineVO {

    String name;
    String market;
    String code;
    LocalDate date;
    double highPrice;
    double lowPrice;
    double openPrice;
    double closePrice;
    StockState state;
    long volume;

    public DailyKLineVO(String name, String market, String code, LocalDate date, double highPrice,
                        double lowPrice, double openPrice, double closePrice, StockState state, long volume) {
        this.name = name;
        this.market = market;
        this.code = code;
        this.date = date;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.state = state;
        this.volume = volume;
    }
}
