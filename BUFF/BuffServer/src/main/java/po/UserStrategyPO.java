package po;

import vo.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/4/16.
 */
public class UserStrategyPO {
    private String strategyName; //策略的名字

    /**
     * 开始日期
     */
    private String begin;


    /**
     * 结束日期
     */

    private String end;

    /**
     * 股票池的选择 存储
     * 两种策略都存
     */
    private StockPoolConditionPO stockPoolConditionPO;


    /**
     * 混合策略表
     * usermode 存储
     */
    private List<MixedStrategyPO> mixedStrategyPOList;

    /**
     * 股票过滤
     * 两种都存
     */
    private  List<StockPickIndexPO> stockPickIndexList;

    /**
     * 持有期 形成期  数目
     * 两个都存
     */
    private TraceBackPO traceBackPO;

    public UserStrategyPO(){

    }

    /**
     * 混合策略类型的保存 持久化
     * @param strategyName
     * @param begin
     * @param end
     * @param stockPoolConditionPO
     * @param mixedStrategyPOList
     * @param stockPickIndexList
     * @param traceBackPO
     */
    public UserStrategyPO(String strategyName, String begin, String end,
                          StockPoolConditionPO stockPoolConditionPO,
                          List<MixedStrategyPO> mixedStrategyPOList,
                          List<StockPickIndexPO> stockPickIndexList,
                          TraceBackPO traceBackPO){
        this.begin  =begin;
        this.end = end;
        this.mixedStrategyPOList = mixedStrategyPOList;
        this.stockPoolConditionPO = stockPoolConditionPO;
        this.strategyName =  strategyName;
        this.stockPickIndexList = stockPickIndexList;
        this.traceBackPO = traceBackPO;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public StockPoolConditionPO getStockPoolConditionPO() {
        return stockPoolConditionPO;
    }

    public void setStockPoolConditionPO(StockPoolConditionPO stockPoolConditionPO) {
        this.stockPoolConditionPO = stockPoolConditionPO;
    }

    public List<MixedStrategyPO> getMixedStrategyPOList() {
        return mixedStrategyPOList;
    }

    public void setMixedStrategyPOList(List<MixedStrategyPO> mixedStrategyPOList) {
        this.mixedStrategyPOList = mixedStrategyPOList;
    }

    public List<StockPickIndexPO> getStockPickIndexList() {
        return stockPickIndexList;
    }

    public void setStockPickIndexList(List<StockPickIndexPO> stockPickIndexList) {
        this.stockPickIndexList = stockPickIndexList;
    }

    public TraceBackPO getTraceBackPO() {
        return traceBackPO;
    }

    public void setTraceBackPO(TraceBackPO traceBackPO) {
        this.traceBackPO = traceBackPO;
    }
}
