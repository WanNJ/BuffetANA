package gui.functions;

/**
 * Created by wshwbluebird on 2017/3/4.
 */

import com.jfoenix.controls.JFXToggleButton;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.util.VetoException;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import javax.annotation.PostConstruct;


@FXMLController(value = "/resources/fxml/LinesPane.fxml" , title = "Lines Pane container")
public class LinesPanelController {
        @FXML
        private  JFXToggleButton  MAtoggle;
        @FXML
        private  JFXToggleButton  VOLToggle;
        @FXML
        private  JFXToggleButton  KDJToggle;
        @FXML
        private  GridPane gridPane;



        @FXML
        private void handleMAtoggle(){
        }

        @FXML
        private  void handleKDJtoggle(){

        }

        @FXML
        private void handleVOLtoggle(){

        }
}
