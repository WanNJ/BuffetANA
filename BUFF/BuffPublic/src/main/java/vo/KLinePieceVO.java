package vo;

import java.time.LocalDate;

/**
 * Created by slow_time on 2017/3/5.
 */
public class KLinePieceVO {

    LocalDate date;
    double highPrice;
    double lowPrice;
    double openPrice;
    double closePrice;

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
