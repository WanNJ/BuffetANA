package util;

/**
 * Created by wshwbluebird on 2017/3/26.
 */

import java.time.LocalDate;

/**
 * 用来存储数据的形式
 */
public class DayMA {

    public LocalDate date;

    public double MAValue;


    /**
     * init
     * @param date
     * @param MAValue
     */
    public DayMA(LocalDate date , double MAValue){
        this.date  =date;
        this.MAValue =MAValue;
    }

    /**
     * init without param
     */
    public DayMA(){

    }
}
