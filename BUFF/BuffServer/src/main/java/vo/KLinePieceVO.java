package vo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by slow_time on 2017/3/5.
 */
public class KLinePieceVO implements Serializable {

    public LocalDate date;
    public double highPrice;
    public double lowPrice;
    public double openPrice;
    public double closePrice;

    public KLinePieceVO() {

    }

    public KLinePieceVO(LocalDate date, double highPrice, double lowPrice, double openPrice, double closePrice) {
        this.date = date;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
    }
}
