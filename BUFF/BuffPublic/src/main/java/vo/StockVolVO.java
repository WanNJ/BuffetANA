package vo;

import java.time.LocalDate;

/**
 * Created by slow_time on 2017/3/5.
 */
public class StockVolVO {

    LocalDate date;
    long vol;


    public StockVolVO() {

    }

    public StockVolVO(LocalDate date, long vol) {
        this.date = date;
        this.vol = vol;
    }
}
