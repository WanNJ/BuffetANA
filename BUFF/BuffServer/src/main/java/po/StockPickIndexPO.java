package po;

import stockenum.StockPickIndex;
import vo.StockPickIndexVO;

import java.util.List;

/**
 * Created by wshwbluebird on 2017/3/24.
 */
public class StockPickIndexPO {

    /**
     * 选股指标的类型
     */
    private  StockPickIndex stockPickIndex;
    /**
     * 选股指标的上界，根据不同的选股指标的类型确定具体的含义
     */
    private Double lowerBound;
    /**
     * 选股指标的下界，根据不同的选股指标的类型确定具体的含义
     */
    private  Double upBound;

    public StockPickIndexPO() {

    }

    /**
     *
     * @param stockPickIndex
     * @param lowerBound
     * @param upBound
     */
    public StockPickIndexPO(StockPickIndex stockPickIndex, Double lowerBound, Double upBound) {
        this.stockPickIndex = stockPickIndex;
        this.lowerBound = lowerBound;
        this.upBound = upBound;
    }

    public StockPickIndexPO(StockPickIndexVO stockPickIndexVO) {
        this.stockPickIndex = stockPickIndexVO.stockPickIndex;
        this.lowerBound = stockPickIndexVO.lowerBound;
        this.upBound = stockPickIndexVO.upBound;
    }

    public StockPickIndex getStockPickIndex() {
        return stockPickIndex;
    }

    public void setStockPickIndex(StockPickIndex stockPickIndex) {
        this.stockPickIndex = stockPickIndex;
    }

    public Double getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(Double lowerBound) {
        this.lowerBound = lowerBound;
    }

    public Double getUpBound() {
        return upBound;
    }

    public void setUpBound(Double upBound) {
        this.upBound = upBound;
    }
}
