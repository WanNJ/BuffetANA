package gui.functions;

import com.jfoenix.controls.JFXTreeTableView;
import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;

import javax.annotation.PostConstruct;

/**
 * Created by wshwbluebird on 2017/3/24.
 */

@FXMLController(value = "/resources/fxml/ui/BetterStrategy.fxml" , title = "EstimateStrategy")
public class BetterStrategyController {

    @FXML JFXTreeTableView treeTableView_formativePeriod;//固定形成期的TreeTableView
    @FXML LineChart excessEarnings_formativePeriod;//固定形成期的超额收益
    @FXML LineChart winningStrategies_formativePeriod;//固定形成期的策略胜率
    @FXML JFXTreeTableView treeTableView_holdingPeriod;//固定持有期的TreeTableView
    @FXML LineChart excessEarnings_holdingPeriod;//固定持有期的超额收益
    @FXML LineChart winningStrategies_holdingPeriod;//固定持有期的策略胜率

    @PostConstruct
    public void init(){

    }
}
