package gui.functions;

import com.jfoenix.controls.JFXTreeTableView;
import gui.RadarChart.MySpiderChart;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import util.StrategyScoreVO;

import javax.annotation.PostConstruct;

/**
 * Created by wshwbluebird on 2017/3/24.
 */

@FXMLController(value = "/resources/fxml/ui/EstimateResult.fxml" , title = "EstimateResult")
public class EstimateResultController {

    @FXMLViewFlowContext private ViewFlowContext context;

    @FXML JFXTreeTableView treeTableView;
    @FXML VBox vBox;
    @FXML Label scoreLabel;

    @PostConstruct
    public void init() throws FlowException {
        StrategyScoreVO fakeVo = new StrategyScoreVO(20, 21, 22, 23, 16, 92);
        StackPane spiderChartScene = null;
        try {
            spiderChartScene = MySpiderChart.getMySpiderChart(fakeVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        scoreLabel.setText(String.valueOf(fakeVo.strategyScore));
        vBox.getChildren().add(2, spiderChartScene);
        System.out.println("Bingo!");
    }

    @FXML
    private void setResult () {

    }
}
