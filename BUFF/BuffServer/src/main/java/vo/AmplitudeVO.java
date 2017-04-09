package vo;

import java.time.LocalDate;

/**
 * Created by slow_time on 2017/4/9.
 */

/**
 * 昨日振幅的VO
 */
public class AmplitudeVO {
    public LocalDate localDate;
    public double amplitude;

    public AmplitudeVO() {

    }
    public AmplitudeVO(LocalDate localDate, double amplitude) {
        this.localDate = localDate;
        this.amplitude = amplitude;
    }
}
