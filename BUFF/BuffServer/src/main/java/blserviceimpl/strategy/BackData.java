package blserviceimpl.strategy;

/**
 * Created by wshwbluebird on 2017/3/27.
 */

import stockenum.StockPickIndex;
import stockenum.StrategyType;

/**
 * 用来封装要比较的数据类型
 * 减少文件的读如次数
 */
public class BackData {
    public String code;

    /**
     * 传进来用来比较数据的值
     */
    public Number rankValue;

    public Number[] mixRank;

    public Number[] filterData;

    /**
     * 第一天买入时的价格。对应第一天的开盘价
     */
    public double firstDayOpen;

    /**
     * 最后一天卖出时的价格，对应最后一天的收盘价
     */
    public double lastDayClose;

    /**
     * 不带过滤参数的初始化过滤方法
     * @param code
     * @param rankValue
     * @param firstDayOpen
     * @param lastDayClose
     */
    public BackData(String code , Number rankValue, double firstDayOpen, double lastDayClose){
        this.code = code;
        this.rankValue = rankValue;
        this.firstDayOpen = firstDayOpen;
        this.lastDayClose = lastDayClose;
        filterData  =new Number[StockPickIndex.values().length];
        mixRank = new Number[StrategyType.values().length];
    }

    /**
     * 带过滤参数的方法
     * @param code
     * @param rankValue
     * @param firstDayOpen
     * @param lastDayClose
     */
    public BackData(String code , Number rankValue, double firstDayOpen, double lastDayClose, Number[] filterData){
        this.code = code;
        this.rankValue = rankValue;
        this.firstDayOpen = firstDayOpen;
        this.lastDayClose = lastDayClose;
        this. filterData  = filterData;
    }

}
