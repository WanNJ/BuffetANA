package vo;

/**
 * Created by slow_time on 2017/4/8.
 */

import java.time.LocalDate;

/**
 * 只是一种  日期和数据绑定的数据类型   没有加具体的意义
 */
public class UpRangeVO {
    /**
     * 日期
     */
    public LocalDate localDate;
    /**
     * 涨幅
     */
    public double upRange;

    public UpRangeVO() {

    }

    public UpRangeVO(LocalDate localDate, double upRange) {
        this.localDate = localDate;
        this.upRange = upRange;
    }
}
