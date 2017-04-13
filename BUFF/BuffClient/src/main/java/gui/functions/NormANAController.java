package gui.functions;

import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import javax.annotation.PostConstruct;

/**
 * Created by wshwbluebird on 2017/4/13.
 */


@FXMLController(value = "/resources/fxml/ui/NormANA.fxml" , title = "test add in Grid Pane")
public class NormANAController {



    @FXML Label kurtosis; // 峰度

    @FXML Label isNorm;  //可否拟合

    @FXML Label mean;   //可否拟合

    @FXML Label sigma;  //可否拟合

    @FXML Label recIn;  //推荐入手价格

    @FXML Label recOut;  // 推荐出售价格

    @FXML BorderPane normHistPane ;  // 画直方图的图



    @PostConstruct
    public void init(){


    }
}
