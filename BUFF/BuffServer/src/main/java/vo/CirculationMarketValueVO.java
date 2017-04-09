package vo;

/**
 * Created by slow_time on 2017/4/9.
 */

import java.time.LocalDate;

/**
 * 流通市值
 */
public class CirculationMarketValueVO {
    public LocalDate localDate;
    public double circulationMarketValue;

    public CirculationMarketValueVO() {

    }

    public CirculationMarketValueVO(LocalDate localDate, double circulationMarketValue) {
        this.localDate = localDate;
        this.circulationMarketValue = circulationMarketValue;
    }
}
