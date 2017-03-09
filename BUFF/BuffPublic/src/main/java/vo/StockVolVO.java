package vo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by slow_time on 2017/3/5.
 */
public class StockVolVO implements Serializable {

    public LocalDate date;
    public long vol;
    /**
     * add by wsw
     *  这一天（周或月）的开盘价是否大于收盘价
     *  如果 开盘价大于收盘价  返回true
     *  如果 收盘价大于开盘价  返回false
     *
     */
    public boolean openAboveClose;


    public StockVolVO() {

    }

    public StockVolVO(LocalDate date, long vol,boolean openAboveClose) {
        this.date = date;
        this.vol = vol;
        this.openAboveClose = openAboveClose;
    }
}
