package vo;

import stockenum.StrategyType;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/4/16.
 */
public class StrategySaveVO {

    public String strategyName; //策略的名字

    public boolean userMode ;//是否是用户自定义的策略

    /**
     * 开始日期
     */
    public LocalDate begin;


    /**
     * 结束日期
     */

    public LocalDate end;

    /**
     * 股票池的选择 存储
     * 两种策略都存
     */
    public StockPoolConditionVO stockPoolConditionVO;


    /**
     * 混合策略表
     * usermode 存储
     */
    public List<MixedStrategyVO> mixedStrategyVOList;

    /**
     * 股票过滤
     * 两种都存
     */
    public  List<StockPickIndexVO> stockPickIndexList;

    /**
     * 策略条件  只有选定策略存储
     */
    public StrategyConditionVO strategyConditionVO;

    /**
     * 持有期 形成期  数目
     * 两个都存
     */
    public TraceBackVO traceBackVO;


    public StrategySaveVO(String strategyName, boolean userMode,StockPoolConditionVO stockPoolConditionVO,
                          List<MixedStrategyVO> mixedStrategyVOList
                        , List<StockPickIndexVO> stockPickIndexList
                        , StrategyConditionVO strategyConditionVO
                        , TraceBackVO traceBackVO, LocalDate begin ,LocalDate end){
        this.strategyName = strategyName;
        this.userMode = userMode;
        this.mixedStrategyVOList = mixedStrategyVOList;
        this.stockPickIndexList = stockPickIndexList;
        this.traceBackVO = traceBackVO;
        this.stockPoolConditionVO = stockPoolConditionVO;
        this.strategyConditionVO =strategyConditionVO;
        this.begin = begin;
        this.end = end;
    }


}
