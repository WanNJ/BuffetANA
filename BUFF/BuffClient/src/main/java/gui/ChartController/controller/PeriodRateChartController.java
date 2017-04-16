package gui.ChartController.controller;

import blservice.strategy.StrategyService;
import gui.ChartController.chart.PeriodRateChart;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import vo.BetterPieceVO;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by wshwbluebird on 2017/4/16.
 */


/**
 * 这个和别的controller 不一样
 * 这个自己 运行 及其初始化
 * 不经过 外面的那个ChartController
 */
public enum PeriodRateChartController implements Initializable {
    PERIOD_RATE_CHART_CONTROLLER;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    private PeriodRateChart excessEarnings_formativePeriod;//固定形成期的超额收益

    private PeriodRateChart winningStrategies_formativePeriod;//固定形成期的策略胜率

    private PeriodRateChart excessEarnings_holdingPeriod;//固定持有期的超额收益

    private PeriodRateChart winningStrategies_holdingPeriod;//固定持有期的策略胜率

    private StrategyService strategyService;

    /**
     * 初始化
     */
    public void init (){
        this.excessEarnings_formativePeriod =  PeriodRateChart.createChart
                (FXCollections.observableList(new ArrayList<BetterPieceVO>()),"持有期","超额收益率");

        this.winningStrategies_formativePeriod =  PeriodRateChart.createChart
                (FXCollections.observableList(new ArrayList<BetterPieceVO>()),"持有期","策略胜率");

        this.excessEarnings_holdingPeriod =  PeriodRateChart.createChart
                (FXCollections.observableList(new ArrayList<BetterPieceVO>()),"形成期","超额收益率");

        this.winningStrategies_holdingPeriod =  PeriodRateChart.createChart
                (FXCollections.observableList(new ArrayList<BetterPieceVO>()),"形成期","策略胜率");
    }

    /**
     * 画出固定的形成期和持有期
     * @param strategyService
     */
    public void update(StrategyService strategyService){
        this.strategyService = strategyService;

        this.excessEarnings_formativePeriod =  PeriodRateChart.createChart
                (FXCollections.observableList(strategyService.getOverProfitRateByFormation()),"持有期","超额收益率");

        this.winningStrategies_formativePeriod =  PeriodRateChart.createChart
                (FXCollections.observableList(strategyService.getWinRateByFormation()),"持有期","策略胜率");

        this.excessEarnings_holdingPeriod =  PeriodRateChart.createChart
                (FXCollections.observableList(strategyService.getOverProfitRateByHolding()),"形成期","超额收益率");

        this.winningStrategies_holdingPeriod =  PeriodRateChart.createChart
                (FXCollections.observableList(strategyService.getWinProfitRateByHolding()),"形成期","策略胜率");
    }


    public PeriodRateChart getExcessEarnings_holdingPeriod() {
        return excessEarnings_holdingPeriod;
    }

    public PeriodRateChart getExcessEarnings_formativePeriod() {

        return excessEarnings_formativePeriod;
    }

    public PeriodRateChart getWinningStrategies_formativePeriod() {
        return winningStrategies_formativePeriod;
    }

    public PeriodRateChart getWinningStrategies_holdingPeriod() {
        return winningStrategies_holdingPeriod;
    }
}
