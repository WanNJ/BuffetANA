package vo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 每日对数收益率VO
 * Created by Accident on 2017/3/9.
 */
public class DailyLogReturnVO implements Serializable{
    public LocalDate date;
    public double logReturnIndex;

    public DailyLogReturnVO(LocalDate date, double logReturnIndex) {
        this.date = date;
        this.logReturnIndex = logReturnIndex;
    }
}
