package vo;

/**
 * Created by wshwbluebird on 2017/3/7.
 */

import java.io.Serializable;

/**
 * 用于储存 放在tooltip中的额外信息
 */
public class KLineExtraVO implements Serializable {

    public double highPrice;
    public double lowPrice;
    public double openPrice;
    public double closePrice;
    /**
     * 股票当天的平均值(目前先定位算术平均值)
     */
    public double averagePrice;

    /**
     * 初始化
     * @param highPrice
     * @param lowPrice
     * @param openPrice
     * @param closePrice
     * @param averagePrice
     */
    public KLineExtraVO(double highPrice,double lowPrice, double openPrice,double closePrice,double averagePrice){
        this.averagePrice = averagePrice;
        this.closePrice = closePrice;
        this.openPrice = openPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
    }

    public KLineExtraVO(){

    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }
}
