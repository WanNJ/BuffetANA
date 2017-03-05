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

@FXMLController(value = "/resources/fxml/ui/Thermometer.fxml" , title = "Material Design Example")
public class ThermometerController {

	@FXML private StackPane root;
	@FXML private JFXDatePicker datePicker;
	@FXML private JFXComboBox<String> jfxComboBox;
	@FXML private JFXComboBox<String> jfxEditableComboBox;
	
	@PostConstruct
	public void init() throws FlowException, VetoException {
		datePicker.setDialogParent(root);

		jfxComboBox.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				ValidationFacade.validate(jfxComboBox);
			}
		});
		
		ChangeListener<? super Boolean> comboBoxFocus = (o, oldVal, newVal) -> {
			if (!newVal) {
				ValidationFacade.validate(jfxEditableComboBox);
			}
		};		
		jfxEditableComboBox.focusedProperty().addListener(comboBoxFocus);
		jfxEditableComboBox.getJFXEditor().focusedProperty().addListener(comboBoxFocus);				
	}
	
}
