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
import java.util.Collection;

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
	@ActionTrigger("StrategyBackTesting")
	private Label StrategyBackTesting;

	@FXML
	@ActionTrigger("StasticAnalysis")
	private Label StasticAnalysis;



	@FXML
	private JFXListView<Label> sideList;

	private FlowHandler contentFlowHandler;

	@PostConstruct
	public void init() throws FlowException, VetoException {
		//登记对象
		context.register(this);
		context.register("sideList",sideList);
		context.register("SingleStock",SingleStock);

		sideList.propagateMouseEventsToParent();
		contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
		Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
		bindNodeToController(Market, MarketController.class, contentFlow, contentFlowHandler);
		bindNodeToController(SingleStock, SingleStockController.class, contentFlow, contentFlowHandler);
		bindNodeToController(Comparison, ComparisonController.class, contentFlow, contentFlowHandler);
		bindNodeToController(Thermometer, ThermometerController.class, contentFlow, contentFlowHandler);
		bindNodeToController(StrategyBackTesting, StrategyBackTestingController.class, contentFlow, contentFlowHandler);
		bindNodeToController(StasticAnalysis, StasticANAController.class, contentFlow, contentFlowHandler);
	}

	private void bindNodeToController(Node node, Class<?> controllerClass, Flow flow, FlowHandler flowHandler) {
		flow.withGlobalLink(node.getId(), controllerClass);
		node.setOnMouseClicked((e) -> changeView((Label) node));
	}

	/**
	 * 跳转界面
	 * @param view 目标界面对应的SideMenu中的Label对象
	 */
	public void changeView(Label view){
		try {
			contentFlowHandler.handle(view.getId());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("can't handle object.  ID:"+view.getId());
		}
		//改变SideMenu的选中项
		sideList.getSelectionModel().select(view);
		//设置导航栏标题
		Label viewName= (Label) context.getRegisteredObject("viewName");
		assert viewName!=null:"can't find registered object:viewName";
		viewName.setText(view.getText());
	}

}
