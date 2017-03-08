package vo;

import java.time.LocalDate;

/**
 * Created by slow_time on 2017/3/5.
 */
public class StockVolVO {

    public LocalDate date;
    public long vol;
    public boolean openAboveClose;


    public StockVolVO() {

    }

    public StockVolVO(LocalDate date, long vol,boolean openAboveClose) {
        this.date = date;
        this.vol = vol;
        this.openAboveClose = openAboveClose;
    }
}
