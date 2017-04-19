package gui.functions;

import blservice.strategy.StrategyService;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableView;
import factory.BLFactorySeviceOnlyImpl;
import factory.BlFactoryService;
import gui.ChartController.controller.PeriodRateChartController;
import gui.utils.TreeTableViewUtil;
import gui.utils.TreeTableViewValue;
import gui.utils.Updatable;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import vo.BetterTableVO;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wshwbluebird on 2017/3/24.
 */

@FXMLController(value = "/resources/fxml/ui/BetterStrategy.fxml" , title = "EstimateStrategy")
public class BetterStrategyController implements Updatable{
    @FXMLViewFlowContext private ViewFlowContext context;

    @FXML JFXComboBox<String> days;
    @FXML JFXTreeTableView<Peroid> treeTableView_formativePeriod;//固定形成期的TreeTableView
    @FXML StackPane excessEarnings_formativePeriod;//固定形成期的超额收益
    @FXML StackPane winningStrategies_formativePeriod;//固定形成期的策略胜率
    @FXML JFXTreeTableView<Peroid> treeTableView_holdingPeriod;//固定持有期的TreeTableView
    @FXML StackPane excessEarnings_holdingPeriod;//固定持有期的超额收益
    @FXML StackPane winningStrategies_holdingPeriod;//固定持有期的策略胜率

    private ObservableList<Peroid> peroids_formativePeriod;//所有固定形成期列表项的集合，动态绑定JFXTreeTableView的显示
    private ObservableList<Peroid> peroids_holdingPeriod;//所有固定持有期列表项的集合，动态绑定JFXTreeTableView的显示

    private BlFactoryService factory=new BLFactorySeviceOnlyImpl();
    private StrategyService strategyService=factory.createStrategyService();
    @PostConstruct
    public void init(){
        //context.register(this);

        days.valueProperty().addListener((observable,oldValue,newValue)-> updateData());

        peroids_formativePeriod= FXCollections.observableArrayList();
        peroids_holdingPeriod= FXCollections.observableArrayList();
        String titles[]={"相对强弱计算周期","超额收益率","一年内的胜率"};
        TreeTableViewUtil.initTreeTableView(treeTableView_formativePeriod,peroids_formativePeriod,titles);
        TreeTableViewUtil.initTreeTableView(treeTableView_holdingPeriod,peroids_holdingPeriod,titles);
        treeTableView_formativePeriod.getColumns().get(0).setPrefWidth(150);
        treeTableView_holdingPeriod.getColumns().get(0).setPrefWidth(150);
        //初始化空表
        PeriodRateChartController periodRateChartController = PeriodRateChartController.PERIOD_RATE_CHART_CONTROLLER;
        periodRateChartController.init();
        this.excessEarnings_formativePeriod.getChildren().add
                (periodRateChartController.getExcessEarnings_formativePeriod());
        this.excessEarnings_holdingPeriod.getChildren().add
                (periodRateChartController.getExcessEarnings_holdingPeriod());
        this.winningStrategies_formativePeriod.getChildren().add
                (periodRateChartController.getWinningStrategies_formativePeriod());
        this.winningStrategies_holdingPeriod.getChildren().add(
                periodRateChartController.getWinningStrategies_holdingPeriod());



    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateData(){
        //必须先更新TreeTableView的数据再更新图标的数据
        updateTreeTableView(strategyService);

    }

    private void updateTreeTableView(StrategyService strategyService){
        peroids_formativePeriod.clear();
        peroids_holdingPeriod.clear();

        //添加两个TreeTableView的行
        int days=1;//初始默认值是1
        try{
            days=Integer.parseInt(this.days.getValue());
            if(days>60){//时间超过60天会抛异常
                days=60;
            }
        }catch (NumberFormatException e){
            this.days.setValue("1");
            return;
        }

        //显示加载动画，把界面变为不可操作
        StrategyBackTestingController strategyBackTestingController=context.getRegisteredObject(StrategyBackTestingController.class);
        strategyBackTestingController.getSpinnerPane().setVisible(true);
        strategyBackTestingController.getRoot().setDisable(true);
        //后台线程加载数据
        UpdateDataService<Void> updateDataService=new UpdateDataService(days);
        //加载完成时，恢复界面，分别更新每张图的数据
        updateDataService.setOnSucceeded(event -> {
            peroids_formativePeriod.addAll(updateDataService.betterTableVOByFormation.stream().map(
                    betterTableVO -> new Peroid(betterTableVO.period,betterTableVO.overProfitRate,
                            betterTableVO.winRate)
            ).collect(Collectors.toList()));
            peroids_holdingPeriod.addAll(updateDataService.betterTableVOByByHolding.stream().map(
                    betterTableVO -> new Peroid(betterTableVO.period,betterTableVO.overProfitRate,
                            betterTableVO.winRate)
            ).collect(Collectors.toList()));
            updateChart(strategyService);

            strategyBackTestingController.getSpinnerPane().setVisible(false);
            strategyBackTestingController.getRoot().setDisable(false);
        });
        updateDataService.start();
    }

    private void updateChart(StrategyService strategyService){
        PeriodRateChartController periodRateChartController =  PeriodRateChartController.PERIOD_RATE_CHART_CONTROLLER;
        periodRateChartController.update(strategyService);
        this.excessEarnings_formativePeriod.getChildren().clear();
        this.excessEarnings_formativePeriod.getChildren().add
                (periodRateChartController.getExcessEarnings_formativePeriod());
        this.excessEarnings_holdingPeriod.getChildren().clear();
        this.excessEarnings_holdingPeriod.getChildren().add
                (periodRateChartController.getExcessEarnings_holdingPeriod());
        this.winningStrategies_formativePeriod.getChildren().clear();
        this.winningStrategies_formativePeriod.getChildren().add
                (periodRateChartController.getWinningStrategies_formativePeriod());

        this.winningStrategies_holdingPeriod.getChildren().clear();
        this.winningStrategies_holdingPeriod.getChildren().add(
                periodRateChartController.getWinningStrategies_holdingPeriod());
    }


    /**
     * 后台获取TreeTableView线程的数据
     * @param <V>
     */
    private class UpdateDataService<V> extends Service<V> {
        /**
         * 用户输入的固定的天数
         */
        int days;
        /**
         * 返回的VO列表，线程执行完后结果放在这
         */
        List<BetterTableVO> betterTableVOByFormation;
        /**
         * 返回的VO列表，线程执行完后结果放在这
         */
        List<BetterTableVO> betterTableVOByByHolding;

        public UpdateDataService(int days) {
            super();
            this.days=days;
        }

        @Override
        protected Task<V> createTask() {
            return new Task<V>() {
                @Override
                protected V call() {
                    betterTableVOByFormation=strategyService.getBetterTableVOByFormation(days);
                    betterTableVOByByHolding=strategyService.getBetterTableVOByHolding(days);
                    return null;
                }
            };
        }
    }

    /**
     * 每一行的值
     */
    private  class Peroid extends TreeTableViewValue<Peroid> {
        StringProperty cycle;
        StringProperty excessEarnings;
        StringProperty winningProbability;

        /**
         *
         * @param cycle //相对强弱计算周期
         * @param excessEarnings //超额收益
         * @param winningProbability //一年内胜率%
         */
        public Peroid(int cycle, double excessEarnings, double winningProbability) {
            this.cycle = new SimpleStringProperty(cycle+"");
            this.excessEarnings = new SimpleStringProperty(String.format("%.2f",excessEarnings)+"%");
            this.winningProbability = new SimpleStringProperty(String.format("%.2f",winningProbability)+"%");

            values.addAll(Arrays.asList(this.cycle,this.excessEarnings,this.winningProbability));
        }
    }
}
