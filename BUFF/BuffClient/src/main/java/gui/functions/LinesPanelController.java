package gui.functions;

/**
 * Created by wshwbluebird on 2017/3/4.
 */

import com.jfoenix.controls.JFXToggleButton;
import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;


@FXMLController(value = "/resources/fxml/LinesPane.fxml" , title = "Lines Pane container")
public class LinesPanelController {
        @FXML
        private  JFXToggleButton  MAtoggle;
        @FXML
        private  JFXToggleButton  VOLToggle;
        @FXML
        private  JFXToggleButton  KDJToggle;
        @FXML
        private GridPane gridPane;


        private Parent KLineChild;//KLine node

        private Parent MALineChild;//MALineChild node

        @FXML
    private void initialize() {
        try {

            KLineChild = (Parent) FXMLLoader.load(getClass().getResource("/resources/fxml/KLine.fxml"));
            MALineChild = (Parent) FXMLLoader.load(getClass().getResource("/resources/fxml/MALine.fxml"));
            gridPane.setRowIndex(KLineChild,1);
            gridPane.setRowIndex(MALineChild,2);
            gridPane.getChildren().add(KLineChild);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        @FXML
        private void handleMAtoggle(){
            if(MAtoggle.isSelected()){
                gridPane.getChildren().add(MALineChild);
            }else{
                gridPane.getChildren().remove(MALineChild);
            }

        }

        @FXML
        private  void handleKDJtoggle(){
        }

        @FXML
        private void handleVOLtoggle(){

        }

//        private void loadOnTheScreen(){
//            gridPane.c
//        }
}
