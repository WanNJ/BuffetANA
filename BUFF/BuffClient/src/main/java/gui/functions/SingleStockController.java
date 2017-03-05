package gui.functions;

import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

/**
 * Created by slow_time on 2017/3/4.
 */

@FXMLController(value = "/resources/fxml/ui/SingleStock.fxml" , title = "Single Stock")
public class SingleStockController {
    @FXML private DatePicker datePicker;
    @FXML private Label openIndexLabel;
    @FXML private Label closeIndexLabel;
    @FXML private Label highIndexLabel;
    @FXML private Label lowIndexLabel;
    @FXML private Label volLabel;
    @FXML private Label adjCloseIndexLabel;

}
