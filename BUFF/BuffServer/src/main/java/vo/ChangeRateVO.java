package vo;

/**
 * Created by slow_time on 2017/4/9.
 */

import java.time.LocalDate;

/**
 * 换手率
 */
public class ChangeRateVO {
    public LocalDate localDate;
    public double changeRate;

    public ChangeRateVO() {

    }

    public ChangeRateVO(LocalDate localDate, double changeRate) {
        this.localDate = localDate;
        this.changeRate = changeRate;
    }
}
