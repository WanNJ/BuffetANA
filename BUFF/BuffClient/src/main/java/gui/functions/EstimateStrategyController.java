package gui.functions;

import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import javax.annotation.PostConstruct;

/**
 * Created by wshwbluebird on 2017/3/24.
 */
@FXMLController(value = "/resources/fxml/ui/EstimateStrategy.fxml" , title = "EstimateStrategy")
public class EstimateStrategyController {
    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    BorderPane winRateCalBorder;  //画固定形成期  算最佳持有期的四张图

    private FlowHandler betterStrategyHandler; //画固定形成期  算最佳持有期的四张图的handler

    private StackPane betterStrategyPane; //画固定形成期  算最佳持有期的四张图的pane

    @PostConstruct
    public void init() throws FlowException {


        Flow incomeBArPieFlow = new Flow(BetterStrategyController.class);


        betterStrategyHandler = incomeBArPieFlow.createHandler(context);

        context.register("incomeBarPieHandler", betterStrategyHandler);

        betterStrategyPane = betterStrategyHandler.start();

        winRateCalBorder.centerProperty().setValue(betterStrategyPane);
    }
}
