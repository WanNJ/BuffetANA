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
	@ActionTrigger("Market")
	private Label Market;

	@FXML
	@ActionTrigger("SingleStock")
	private Label SingleStock;

	@FXML
	@ActionTrigger("Comparison")
	private Label Comparison;

	@FXML
	@ActionTrigger("Thermometer")
	private Label Thermometer;
	
	@FXML
	private JFXListView<Label> sideList;

	@PostConstruct
	public void init() throws FlowException, VetoException {
		//登记对象
		context.register(this);
		context.register("sideList",sideList);
		context.register("SingleStock",SingleStock);

		sideList.propagateMouseEventsToParent();
		FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
		Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
		bindNodeToController(Market, MarketController.class, contentFlow, contentFlowHandler);
		bindNodeToController(SingleStock, SingleStockController.class, contentFlow, contentFlowHandler);
		bindNodeToController(Comparison, ComparisonController.class, contentFlow, contentFlowHandler);
		bindNodeToController(Thermometer, ThermometerController.class, contentFlow, contentFlowHandler);
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
