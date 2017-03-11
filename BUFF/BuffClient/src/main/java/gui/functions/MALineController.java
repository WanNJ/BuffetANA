package gui.functions;

import blservice.singlestock.MALineService;
import blstub.singlestockstub.MALineServiceImpl_Stub;
import gui.ChartController.ChartController;
import gui.ChartController.MAChartController;
import gui.ChartController.MALineChart;
import io.datafx.controller.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import vo.MAPieceVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/3/4.
 *
 */
@FXMLController(value = "/resources/fxml/ui/MALine.fxml" , title = "test add in Grid Pane")
public class MALineController {
    @FXML
    BorderPane borderPane;

    @FXML
    private void initialize() {
        LocalDate first = LocalDate.of(2015, 10, 1);
        LocalDate second = LocalDate.of(2015, 10, 12);
        MALineService maLineService = new MALineServiceImpl_Stub();
        MAChartController maChartController = ChartController.INSTANCE.getMAChartController();
        maChartController.setMaLineService(maLineService);
        maChartController.drawChat();
        borderPane.centerProperty().setValue(maChartController.getChart());


    }
}
