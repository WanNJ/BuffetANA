package gui.sidemenu;

import com.jfoenix.controls.JFXListView;
import gui.functions.*;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import javax.annotation.PostConstruct;

@FXMLController(value = "/resources/fxml/SideMenu.fxml", title = "Material Design Example")
public class SideMenuController {

	@FXMLViewFlowContext
	private ViewFlowContext context;

	@FXML
	@ActionTrigger("market")
	private Label market;

	@FXML
	@ActionTrigger("KDJLine")
	private Label KDJLine;

	@FXML
	@ActionTrigger("Kline")
	private Label Kline;

	@FXML
	@ActionTrigger("LinesPanel")
	private Label LinesPanel;

	@FXML
	@ActionTrigger("MALine")
	private Label MALine;

	@FXML
	@ActionTrigger("SingleStock")
	private Label SingleStock;

	@FXML
	@ActionTrigger("VOLLine")
	private Label VOLLine;

	@FXML
	@ActionTrigger("thermometer")
	private Label thermometer;
	
	@FXML
	private JFXListView<?> sideList;

	@PostConstruct
	public void init() throws FlowException, VetoException {
		sideList.propagateMouseEventsToParent();
		FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
		Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
		bindNodeToController(market, MarketController.class, contentFlow, contentFlowHandler);
		bindNodeToController(KDJLine, KDJLineController.class, contentFlow, contentFlowHandler);
		bindNodeToController(Kline, KlineController.class, contentFlow, contentFlowHandler);
		bindNodeToController(LinesPanel, LinesPanelController.class, contentFlow, contentFlowHandler);
		bindNodeToController(MALine, MALineController.class, contentFlow, contentFlowHandler);
		bindNodeToController(SingleStock, SingleStockController.class, contentFlow, contentFlowHandler);
		bindNodeToController(VOLLine, VOLLineController.class, contentFlow, contentFlowHandler);
		bindNodeToController(thermometer, ThermometerController.class, contentFlow, contentFlowHandler);
	}

	private void bindNodeToController(Node node, Class<?> controllerClass, Flow flow, FlowHandler flowHandler) {
		flow.withGlobalLink(node.getId(), controllerClass);
		node.setOnMouseClicked((e) -> {
			try {				
				flowHandler.handle(node.getId());				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
	}

}
