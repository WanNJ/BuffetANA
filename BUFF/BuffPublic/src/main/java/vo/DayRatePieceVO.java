package vo;

/**
 * Created by wshwbluebird on 2017/3/24.
 */

import java.time.LocalDate;

/**
 * 用于画图
 * 存储每一个坐标的行列信息
 *
 */
public class DayRatePieceVO {

    public LocalDate date;  //x轴的日期

    public double profitRate ;// y轴的收益率


    /**
     * 提供的无参数初始化方法 不要删!
     */
    public DayRatePieceVO(){

    }

    /**
     * 有参数的初始方法
     * @param date
     * @param profitRate
     */
    public DayRatePieceVO(LocalDate date , double profitRate){
        this.date = date;
        this.profitRate = profitRate;
    }


}
