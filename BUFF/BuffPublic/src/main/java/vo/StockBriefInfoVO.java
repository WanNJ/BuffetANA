package vo;

import java.time.LocalDate;

/**
 * Created by slow_time on 2017/3/5.
 */
public class StockBriefInfoVO {
    String name;
    LocalDate date;
    String code;
    double closePrice;
    double range;

    public StockBriefInfoVO(String name, LocalDate date, String code, double closePrice, double range) {
        this.name = name;
        this.date = date;
        this.code = code;
        this.closePrice = closePrice;
        this.range = range;
    }
}