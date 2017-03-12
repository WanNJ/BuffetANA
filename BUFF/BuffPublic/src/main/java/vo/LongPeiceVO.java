package vo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by wshwbluebird on 2017/3/13.
 */

/**
 * 只是一种  日期和数据绑定的数据类型   没有加具体的意义
 */
public class LongPeiceVO implements Serializable {
    /**
     * 日期
     */
    public LocalDate localDate;
    public long amount;

    /**
     * 默认的构造方法
     */
    public LongPeiceVO(){

    }


    public LongPeiceVO(LocalDate localDate, long amount){
        this.localDate  =localDate;
        this.amount = amount;
    }
}
