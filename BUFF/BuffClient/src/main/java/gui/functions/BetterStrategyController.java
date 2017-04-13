package gui.functions;

import blservice.strategy.StrategyService;
import blstub.strategy.StrategyServiceImpl_Stub;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import gui.utils.TreeTableViewUtil;
import gui.utils.TreeTableViewValue;
import io.datafx.controller.FXMLController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by wshwbluebird on 2017/3/24.
 */

@FXMLController(value = "/resources/fxml/ui/BetterStrategy.fxml" , title = "EstimateStrategy")
public class BetterStrategyController {

    @FXML JFXComboBox<String> days;
    @FXML JFXTreeTableView<Peroid> treeTableView_formativePeriod;//固定形成期的TreeTableView
    @FXML LineChart excessEarnings_formativePeriod;//固定形成期的超额收益
    @FXML LineChart winningStrategies_formativePeriod;//固定形成期的策略胜率
    @FXML JFXTreeTableView<Peroid> treeTableView_holdingPeriod;//固定持有期的TreeTableView
    @FXML LineChart excessEarnings_holdingPeriod;//固定持有期的超额收益
    @FXML LineChart winningStrategies_holdingPeriod;//固定持有期的策略胜率

    private ObservableList<Peroid> peroids_formativePeriod;//所有固定形成期列表项的集合，动态绑定JFXTreeTableView的显示
    private ObservableList<Peroid> peroids_holdingPeriod;//所有固定持有期列表项的集合，动态绑定JFXTreeTableView的显示
    @PostConstruct
    public void init(){
        days.setOnAction(e->showData());

        peroids_formativePeriod= FXCollections.observableArrayList();
        peroids_holdingPeriod= FXCollections.observableArrayList();

        String titles[]={"相对强弱计算周期","超额收益率","一年内的胜率"};
        TreeTableViewUtil.initTreeTableView(treeTableView_formativePeriod,peroids_formativePeriod,titles);
        TreeTableViewUtil.initTreeTableView(treeTableView_holdingPeriod,peroids_holdingPeriod,titles);
        treeTableView_formativePeriod.getColumns().get(0).setPrefWidth(150);
        treeTableView_holdingPeriod.getColumns().get(0).setPrefWidth(150);

        showData();
    }

    private void showData(){
        StrategyService strategyService=new StrategyServiceImpl_Stub();

        peroids_formativePeriod.clear();
        peroids_holdingPeriod.clear();

        //添加两个TreeTableView的行
        int days=Integer.parseInt(this.days.getValue());
        peroids_formativePeriod.addAll(strategyService.getBetterTableVOByFormation(days).stream().map(
                betterTableVO -> new Peroid(betterTableVO.period,betterTableVO.overProfitRate,
                        betterTableVO.winRate)
        ).collect(Collectors.toList()));
        peroids_holdingPeriod.addAll(strategyService.getBetterTableVOByHolding(days).stream().map(
                betterTableVO -> new Peroid(betterTableVO.period,betterTableVO.overProfitRate,
                        betterTableVO.winRate)
        ).collect(Collectors.toList()));
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
