package gui.functions;

import com.jfoenix.controls.JFXTreeTableView;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.annotation.PostConstruct;

/**
 * Created by wshwbluebird on 2017/3/24.
 */

@FXMLController(value = "/resources/fxml/ui/AccumulatedIncome.fxml" , title = "AccumulatedIncome")
public class AccumulatedIncomeController {

    @FXMLViewFlowContext private ViewFlowContext context;

    @FXML VBox vBox;
    @FXML Label annualizedReturn;//年化收益率
    @FXML Label baseAnnualizedReturn;//基准年化收益率
    @FXML Label alpha;//阿尔法
    @FXML Label beta;//贝塔
    @FXML Label sharpeRatio;//夏普比率
    @FXML Label returnVolatility;//收益波动率
    @FXML Label informationRatio;//信息比率
    @FXML Label maximumRetracement;//最大回撤
    @FXML Label turnoverRate;//换手率
    @FXML LineChart chart;

    @PostConstruct
    public void init() throws FlowException {

    }
}
