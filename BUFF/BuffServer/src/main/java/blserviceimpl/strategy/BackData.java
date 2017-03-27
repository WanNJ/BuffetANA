package blserviceimpl.strategy;

/**
 * Created by wshwbluebird on 2017/3/27.
 */

import stockenum.StockPickIndex;

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

    public Number[] filterData;

    public BackData(String code , Number rankValue ){
        this.code = code;
        this.rankValue = rankValue;
        filterData  =new Number[StockPickIndex.values().length];
    }

}
