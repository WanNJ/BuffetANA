package gui.functions;

/**
 * Created by wshwbluebird on 2017/3/4.
 */

import com.jfoenix.controls.JFXToggleButton;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;

import javax.annotation.PostConstruct;
import java.io.IOException;


@FXMLController(value = "/resources/fxml/ui/LinesPane.fxml" , title = "Lines Pane container")
public class LinesPanelController {
        @FXMLViewFlowContext
        private ViewFlowContext context;
        @FXML
        private  JFXToggleButton  MAtoggle;
        @FXML
        private  JFXToggleButton  VOLToggle;
        @FXML
        private  JFXToggleButton  KDJToggle;
        @FXML
        private  JFXToggleButton KLinetoggle;
        @FXML
        private  GridPane gridPane;


        private Parent KLineChild;//KLine node

        private Parent MALineChild;//MALineChild node

        private Parent VOLLineChild; //VOLLineChild node

        private Parent KDJLineChild; //KDJLineChild node

        private FlowHandler KLineHandler;

//        @FXML
//        private void initialize() {
//        try {
//            System.out.println("加载四条信息");
//            //加载四条线的节点信息
//            KLineChild = (Parent) FXMLLoader.load(getClass().getResource("/resources/fxml/ui/KLine.fxml"));
//            MALineChild = (Parent) FXMLLoader.load(getClass().getResource("/resources/fxml/ui/MALine.fxml"));
//            VOLLineChild = (Parent)  FXMLLoader.load(getClass().getResource("/resources/fxml/ui/VOLLine.fxml"));
//            KDJLineChild = (Parent)  FXMLLoader.load(getClass().getResource("/resources/fxml/ui/KDJLine.fxml"));
//
//
//            gridPane.setRowIndex(KLineChild,1);
//            gridPane.setRowIndex(MALineChild,2);
//            gridPane.setRowIndex(VOLLineChild,3);
//            gridPane.setRowIndex(KDJLineChild,4);
//
//            //默认设置可以打开K线
//            KLinetoggle.selectedProperty().set(true);
//            gridPane.getChildren().add(KLineChild);
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @PostConstruct
    public void init() throws FlowException, VetoException {

//        context = new ViewFlowContext();
//        // set the default controller
//        Flow innerFlow = new Flow(KlineController.class);
//
//        KLineHandler = innerFlow.createHandler(context);
//        context.register("KLineHandler", KLineHandler);
//        //drawer.setContent(LineHandler.start(new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.SWIPE_LEFT)));
//        gridPane.getChildren().add(KLineHandler.start());
    }



        @FXML
        private void handleMAtoggle() throws FlowException {
//            if(MAtoggle.isSelected()){
//                gridPane.getChildren().add(MALineChild);
//            }else{
//                gridPane.getChildren().remove(MALineChild);
//            }
            context = new ViewFlowContext();
            // set the default controller
            Flow innerFlow = new Flow(KlineController.class);

            KLineHandler = innerFlow.createHandler(context);
            context.register("KLineHandler", KLineHandler);
            //drawer.setContent(LineHandler.start(new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.SWIPE_LEFT)));
            gridPane.getChildren().add(KLineHandler.start());

        }

        @FXML
        private  void handleKDJtoggle(){
            if(KDJToggle.isSelected()){
                gridPane.getChildren().add(KDJLineChild);
            }else{
                gridPane.getChildren().remove(KDJLineChild);
            }
        }

        @FXML
        private void handleVOLtoggle(){
            if(VOLToggle.isSelected()){
                gridPane.getChildren().add(VOLLineChild);
            }else{
                gridPane.getChildren().remove(VOLLineChild);
            }
        }


        @FXML
        private void handleKLinetoggle(){
            if(KLinetoggle.isSelected()){
                gridPane.getChildren().add(KLineChild);
            }else{
                gridPane.getChildren().remove(KLineChild);
            }
        }

//        private void loadOnTheScreen(){
//            gridPane.c
//        }
}
