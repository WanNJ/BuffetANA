package vo;

import java.time.LocalDate;

/**
 * Created by wshwbluebird on 2017/3/8.
 */


/**
 * @author wsw
 * 用于存储在tooltip中的数据
 * TODO 在考虑是不是把MA线的显示数据 也放到这里   和还是加一个bar???
 */
public class VolExtraVO {
    /**
     * x 轴的显示时间
     */
    public LocalDate date;

    /**
     * 成交量
     */
    public long vol;
    /**
     * 与前一日相比的变化率
     *
     */
    public double changeRate;

    /**
     * 与前一日相比的变化值
     */
    public double changeValue;

    /**
     * 开盘价是否高于收盘价
     * 决定颜色用
     */
    boolean openAboveClose;

    public VolExtraVO(){

    }

    /**
     * 全部数据初始化构造方法
     * @param date
     * @param vol
     * @param changeRate
     * @param changeValue
     */
    public VolExtraVO(LocalDate date, long vol, double changeRate, double changeValue,boolean openAboveClose){
        this.changeRate = changeRate;
        this.changeRate = changeRate;
        this.date = date;
        this.vol = vol;
        this.openAboveClose = openAboveClose;
    }

    /**
     * 通过 StockVolVO 构造附加值数据
     * @param stockVolVO
     */
    public VolExtraVO(StockVolVO stockVolVO){
        this.vol = stockVolVO.vol;
        this.openAboveClose = stockVolVO.openAboveClose;
        this.date = stockVolVO.date;
        this.changeRate  = 0;
        this.changeValue = 0;
    }
}
