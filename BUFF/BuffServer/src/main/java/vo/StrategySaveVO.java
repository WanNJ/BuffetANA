package vo;

import po.*;
import stockenum.StockPickIndex;
import stockenum.StrategyType;
import util.DateUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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


    public StrategySaveVO(){

    }

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

    public StrategySaveVO(UserStrategyPO userStrategyPO) {
        this.strategyName = userStrategyPO.getStrategyName();
        this.stockPoolConditionVO  = new StockPoolConditionVO(userStrategyPO.getStockPoolConditionPO());
        this.userMode = true;
        this.mixedStrategyVOList = userStrategyPO.getMixedStrategyPOList()
                .stream()
                .map(t->new MixedStrategyVO(t))
                .collect(Collectors.toList());
        this.traceBackVO = new TraceBackVO(userStrategyPO.getTraceBackPO());
        this.stockPickIndexList = userStrategyPO.getStockPickIndexList()
                .stream()
                .map(t->new StockPickIndexVO(t))
                .collect(Collectors.toList());
        this.strategyConditionVO = new StrategyConditionVO
                (null, DateUtil.parseLine(userStrategyPO.getBegin())
                        ,DateUtil.parseLine(userStrategyPO.getEnd()),true);

        this.begin = DateUtil.parseLine(userStrategyPO.getBegin());
        this.end = DateUtil.parseLine(userStrategyPO.getEnd());

    }

    public StrategySaveVO(SingleStrategyPO singleStrategyPO) {
        this.strategyName = singleStrategyPO.getStrategyName();
        this.stockPoolConditionVO  = new StockPoolConditionVO(singleStrategyPO.getStockPoolConditionPO());
        this.userMode = true;
        this.mixedStrategyVOList = new ArrayList<>();
        this.traceBackVO = new TraceBackVO(singleStrategyPO.getTraceBackPO());
        this.stockPickIndexList = singleStrategyPO.getStockPickIndexList()
                .stream()
                .map(t->new StockPickIndexVO(t))
                .collect(Collectors.toList());
        this.strategyConditionVO = new StrategyConditionVO(singleStrategyPO.getStrategyConditionVO());

        this.begin = strategyConditionVO.beginDate;
        this.end =strategyConditionVO.endDate;
    }

    /**
     * 提取策略中 均值或者动量的保存信息
     * @return
     */
    public SingleStrategyPO toSingleStrategyPO(){

        List<StockPickIndexPO> stockPickIndexPOs =
                this.stockPickIndexList.stream()
                        .map(t->new StockPickIndexPO(t))
                        .collect(Collectors.toList());

        SingleStrategyPO singleStrategyPO = new SingleStrategyPO
                (this.strategyName
                        , new StockPoolConditionPO(this.stockPoolConditionVO)
                        , stockPickIndexPOs
                        ,new StrategyConditionPO(this.strategyConditionVO)
                         ,new TraceBackPO(this.traceBackVO));

        return singleStrategyPO;
    }


    /**
     * 提取策略中 用户自定义部分的保存信息
     * @return
     */
    public UserStrategyPO toUserStrategyPO(){

        List<StockPickIndexPO> stockPickIndexPOs =
                this.stockPickIndexList.stream()
                        .map(t->new StockPickIndexPO(t))
                        .collect(Collectors.toList());

        List<MixedStrategyPO> mixedStrategyPOs =
                this.mixedStrategyVOList.stream()
                        .map(t->new MixedStrategyPO(t))
                        .collect(Collectors.toList());

        UserStrategyPO userStrategyPO = new UserStrategyPO
                (this.strategyName,begin.toString(),end.toString()
                        , new StockPoolConditionPO(this.stockPoolConditionVO)
                        ,mixedStrategyPOs
                        , stockPickIndexPOs
                        ,new TraceBackPO(this.traceBackVO));

        return userStrategyPO;
    }



}
