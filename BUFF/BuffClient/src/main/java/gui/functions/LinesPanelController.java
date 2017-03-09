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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

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


        /**
         *画图的handler
         */
        private FlowHandler KLineHandler;

        private FlowHandler VOLHandler;

        private FlowHandler MaHandler;

        /**
         * 存储加载线程时 生成的容器
         * add by wsw
         */
        private StackPane klinePane;

        private StackPane volPane;

        private StackPane maPane;

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

// context = new ViewFlowContext();
        // set the default controller
        Flow klineFlow = new Flow(KlineController.class);
        Flow volFlow = new Flow(VOLLineController.class);
        Flow maFlow = new Flow(MALineController.class);


        KLineHandler = klineFlow.createHandler(context);
        VOLHandler = volFlow.createHandler(context);
        MaHandler = maFlow.createHandler(context);


        context.register("KLineHandler", KLineHandler);
        context.register("VOLHandler", VOLHandler);
        context.register("MaHandler", MaHandler);
        //
        //drawer.setContent(LineHandler.start(new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.SWIPE_LEFT)));
        maPane = MaHandler.start();
        volPane = VOLHandler.start();
        klinePane = KLineHandler.start();
        gridPane.addRow(1,klinePane);
        //gridPane.addRow(3,MaHandler.start());
       // gridPane.addRow(2,VOLHandler.start());
    }



        @FXML
        private void handleMAtoggle() throws FlowException, VetoException {
            if(MAtoggle.isSelected()){
              gridPane.addRow(3,maPane);
            }else{
               MaHandler.destroy();
               gridPane.getChildren().remove(maPane);

            }

        }

        @FXML
        private  void handleKDJtoggle() throws FlowException {

        }

        @FXML
        private void handleVOLtoggle() throws FlowException {
            if(VOLToggle.isSelected()){
                gridPane.addRow(2,volPane);
            }else{
                VOLHandler.destroy();
                gridPane.getChildren().remove(volPane);
            }
        }


        @FXML
        private void handleKLinetoggle() throws FlowException {
            if(KLinetoggle.isSelected()){
                gridPane.addRow(1,klinePane);
            }else{
                KLineHandler.destroy();
                gridPane.getChildren().remove(klinePane);
            }
        }

}
