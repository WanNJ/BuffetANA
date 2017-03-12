package gui.functions;

import blservice.exception.DateIndexException;
import blstub.singlestockstub.KLineServiceImpl_Stub;
import gui.ChartController.ChartController;
import gui.ChartController.KLineChart;
import gui.ChartController.KLineChartController;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.action.ActionMethod;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import vo.KLinePieceVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/3/4.
 */

@FXMLController(value = "/resources/fxml/ui/KLine.fxml" , title = "test add in Grid Pane")
public class KlineController {
    @FXML
    BorderPane DayLinePane;

    @FXML
    private void initialize() {
//        LocalDate first = LocalDate.of(2015, 10, 1);
//        LocalDate second = LocalDate.of(2015, 10, 10);
//        //KLineServiceImpl_Stub kLineServiceImpl_stub = new KLineServiceImpl_Stub();
//        KLineChartController kLineChartController = ChartController.INSTANCE.getKLineChartController();
//
//
//        //kLineChartController.setkLineService(kLineServiceImpl_stub);
//        kLineChartController.drawChat();
//        DayLinePane.centerProperty().setValue(kLineChartController.getChart());
    }

    /**
     * 监听器改图
     * @param code
     * @param first
     * @param second
     */
    public void upDateGraph( String code ,LocalDate first , LocalDate second ){
        KLineChartController kLineChartController = ChartController.INSTANCE.getKLineChartController();
        kLineChartController.setStockCode(code);

        //TODO delete
        System.out.println("first:   "+first);

        kLineChartController.setStartDate(first);
        kLineChartController.setEndDate(second);
        kLineChartController.drawChat();
        DayLinePane.centerProperty().setValue(kLineChartController.getChart());
    }



}
