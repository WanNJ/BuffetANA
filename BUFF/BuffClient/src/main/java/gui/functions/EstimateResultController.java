package gui.functions;

import com.jfoenix.controls.JFXTreeTableView;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.annotation.PostConstruct;

/**
 * Created by wshwbluebird on 2017/3/24.
 */

@FXMLController(value = "/resources/fxml/ui/EstimateResult.fxml" , title = "EstimateResult")
public class EstimateResultController {

    @FXMLViewFlowContext private ViewFlowContext context;

    @FXML VBox vBox;
    @FXML JFXTreeTableView treeTableView;
    @FXML LineChart chart;

    private FlowHandler incomeBarPieHandler; //画收益分布的直方图和饼图的handler
    private StackPane incomeBarPiePane; //画收益分布的直方图和饼图的pane

    @PostConstruct
    public void init() throws FlowException {

        Flow incomeBArPieFlow = new Flow(IncomeBarPieController.class);
        incomeBarPieHandler = incomeBArPieFlow.createHandler(context);
        context.register("incomeBarPieHandler",incomeBarPieHandler);
        incomeBarPiePane = incomeBarPieHandler.start();
        vBox.getChildren().addAll(incomeBarPiePane);
    }
}
