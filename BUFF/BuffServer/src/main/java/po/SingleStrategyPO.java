package po;

import vo.StockPickIndexVO;
import vo.StockPoolConditionVO;
import vo.StrategyConditionVO;
import vo.TraceBackVO;

import java.util.List;

/**
 * Created by wshwbluebird on 2017/4/16.
 */
public class SingleStrategyPO {
    private  String strategyName; //策略的名字
    private  StockPoolConditionPO stockPoolConditionPO;

    /**
     * 过滤条件
     */
    private  List<StockPickIndexPO> stockPickIndexList;

    /**
     * 策略条件  只有选定策略存储
     */
    private  StrategyConditionPO strategyConditionVO;

    /**
     * 持有期 形成期  数目
     * 两个都存
     */
    private  TraceBackPO traceBackPO;


    /**
     * 均值策略或者动量策略的选择持久化
     * @param strategyName
     * @param stockPoolConditionPO
     * @param stockPickIndexList
     * @param strategyConditionVO
     * @param traceBackPO
     */
    public  SingleStrategyPO(String strategyName , StockPoolConditionPO stockPoolConditionPO,
                             List<StockPickIndexPO> stockPickIndexList,
                             StrategyConditionPO strategyConditionVO ,
                             TraceBackPO traceBackPO){
        this.stockPickIndexList = stockPickIndexList;
        this.stockPoolConditionPO = stockPoolConditionPO;
        this.strategyConditionVO = strategyConditionVO;
        this.traceBackPO = traceBackPO;
        this.strategyName  =strategyName;
    }

    public SingleStrategyPO(){

    }


    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public StockPoolConditionPO getStockPoolConditionPO() {
        return stockPoolConditionPO;
    }

    public void setStockPoolConditionPO(StockPoolConditionPO stockPoolConditionPO) {
        this.stockPoolConditionPO = stockPoolConditionPO;
    }

    public List<StockPickIndexPO> getStockPickIndexList() {
        return stockPickIndexList;
    }

    public void setStockPickIndexList(List<StockPickIndexPO> stockPickIndexList) {
        this.stockPickIndexList = stockPickIndexList;
    }

    public StrategyConditionPO getStrategyConditionVO() {
        return strategyConditionVO;
    }

    public void setStrategyConditionVO(StrategyConditionPO strategyConditionVO) {
        this.strategyConditionVO = strategyConditionVO;
    }

    public TraceBackPO getTraceBackPO() {
        return traceBackPO;
    }

    public void setTraceBackPO(TraceBackPO traceBackPO) {
        this.traceBackPO = traceBackPO;
    }
}
