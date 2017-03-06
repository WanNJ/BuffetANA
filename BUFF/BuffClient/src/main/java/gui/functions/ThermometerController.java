package gui.functions;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.validation.ValidationFacade;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.util.VetoException;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

import javax.annotation.PostConstruct;

/**
 * 市场温度计界面的控制器
 * @author zjy
 */
@FXMLController(value = "/resources/fxml/ui/Thermometer.fxml" , title = "Material Design Example")
public class ThermometerController {

	@FXML private StackPane root;
	
	@PostConstruct
	public void init() throws FlowException, VetoException {

	}
	
}
