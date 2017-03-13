package gui.functions;

import blservice.exception.DateIndexException;
import blstub.singlestockstub.VolServiceImpl_Stub;
import gui.ChartController.*;
import io.datafx.controller.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import vo.StockVolVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/3/5.
 */
@FXMLController(value = "/resources/fxml/ui/VOLLine.fxml" , title = "test add in Grid Pane")
public class VOLLineController {
    @FXML
    BorderPane borderPane;

    @FXML
    private void initialize() {
//        LocalDate first = LocalDate.of(2015, 10, 1);
//        LocalDate second = LocalDate.of(2015, 11, 10);
//        VolServiceImpl_Stub volServiceImpl_stub  =new VolServiceImpl_Stub();
//
//        //单例模式的添加方法
//        VOLChartController volChartController  = ChartController.INSTANCE.getVOLChartController();
//        volChartController.setVolService(volServiceImpl_stub);
//
//        volChartController.drawChat();
//        borderPane.centerProperty().setValue(volChartController.getChart());


    }


    /**
     * 监听器改图
     * @param code
     * @param first
     * @param second
     */
    public void upDateGraph( String code ,LocalDate first , LocalDate second ){
        VOLChartController volChartController = ChartController.INSTANCE.getVOLChartController();
        volChartController.setStartDate(first);
        volChartController.setEndDate(second);
        volChartController.setStockCode(code);
        volChartController.drawChat();
        VolBarPane volBarPane = new VolBarPane(volChartController.getChart(),1.0);
        borderPane.centerProperty().setValue(volBarPane);
    }

}
