package vo;

/**
 * Created by slow_time on 2017/4/8.
 */

import java.time.LocalDate;

/**
 * 只是一种  日期和数据绑定的数据类型   没有加具体的意义
 */
public class AdjVO {
    /**
     * 日期
     */
    public LocalDate localDate;
    /**
     * 复权平均价
     */
    public double adj;

    public AdjVO() {

    }

    public AdjVO(LocalDate localDate, double adj) {
        this.localDate = localDate;
        this.adj = adj;
    }
}
