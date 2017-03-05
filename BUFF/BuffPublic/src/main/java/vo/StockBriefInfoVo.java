package vo;

import java.time.LocalDate;

/**
 * Created by slow_time on 2017/3/5.
 */
public class StockBriefInfoVo {
    String name;
    LocalDate date;
    String code;
    double closePrice;
    double range;
    StockState stockState;

    public StockBriefInfoVo(String name, LocalDate date, String code,
                            double closePrice, double range, StockState stockState) {
        this.name = name;
        this.date = date;
        this.code = code;
        this.closePrice = closePrice;
        this.range = range;
        this.stockState = stockState;
    }
}
