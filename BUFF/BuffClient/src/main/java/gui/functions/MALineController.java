package gui.functions;

import blservice.singlestock.MALineService;
import blstub.singlestockstub.MALineServiceImpl_Stub;
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
        try {
            List<MAPieceVO> list  = maLineService.getMAInfo("code",first,second);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        ObservableList<MAPieceVO>   vos =  FXCollections.observableArrayList();
        for (MAPieceVO temp : list) {
            vos.add(temp);
        }

        MALineChart maLineChart = MALineChart.createChart(vos);
        borderPane.centerProperty().setValue(maLineChart);


    }
}
