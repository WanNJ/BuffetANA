package vo;

import po.StockPickIndexPO;
import stockenum.StockPickIndex;

/**
 * Created by slow_time on 2017/3/24.
 */
public class StockPickIndexVO {

    /**
     * 选股指标的类型
     */
    public StockPickIndex stockPickIndex;
    /**
     * 选股指标的上界，根据不同的选股指标的类型确定具体的含义
     */
    public Double lowerBound;
    /**
     * 选股指标的下界，根据不同的选股指标的类型确定具体的含义
     */
    public Double upBound;

    public StockPickIndexVO() {

    }

    public StockPickIndexVO(StockPickIndex stockPickIndex, Double lowerBound, Double upBound) {
        this.stockPickIndex = stockPickIndex;
        this.lowerBound = lowerBound;
        this.upBound = upBound;
    }

    public StockPickIndexVO(StockPickIndexPO stockPickIndexPO) {
        this.stockPickIndex = stockPickIndexPO.getStockPickIndex();
        this.lowerBound = stockPickIndexPO.getLowerBound();
        this.upBound = stockPickIndexPO.getUpBound();
    }
}
