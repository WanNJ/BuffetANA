package blserviceimpl.strategy;

/**
 * Created by wshwbluebird on 2017/4/7.
 */

import stockenum.StockPickIndex;

import java.time.LocalDate;

/**
 *
 * 为 了分离持仓期和持有期 设计的单独的backdata
 *
 *
 */
public class SingleBackData {

    public LocalDate date;
    public Number[] rankValues;
    public Number[] rilterValues;
    /**
     * 当日开盘价
     */
    public double open;

    /**
     * 当日收盘价
     */
    public double close;

    /**
     * 当日复权收盘价
     */
    public double AdjClose;

    /**
     * 当日成交量
     */
    public long volume;

    public SingleBackData(){
        rankValues =new Number[61];
        rilterValues= new Number[StockPickIndex.values().length];
    }


    public SingleBackData( LocalDate date , double open ,
                          double close , double AdjClose , long volume){
        rankValues =new Number[61];
        rilterValues= new Number[StockPickIndex.values().length];
        this.close = close;
        this.open = open;
        this.AdjClose = AdjClose;
        this.volume = volume;
        this.date =date;
    }

}
