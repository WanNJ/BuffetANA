package vo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 每日收盘价VO
 * Created by Accident on 2017/3/9.
 */
public class DailyClosingPriceVO implements Serializable{
    public LocalDate date;
    public double closePrice;

    public DailyClosingPriceVO (LocalDate date, double closePrice) {
        this.date = date;
        this.closePrice = closePrice;
    }
}
