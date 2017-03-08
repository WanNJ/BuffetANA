package gui.functions;

import blservice.exception.DateIndexException;
import blstub.singlestockstub.KLineServiceImpl_Stub;
import gui.ChartController.KLineChart;
import io.datafx.controller.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import vo.KLinePieceVO;

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
        LocalDate first = LocalDate.of(2015, 10, 1);
        LocalDate second = LocalDate.of(2015, 10, 10);
        KLineServiceImpl_Stub kLineServiceImpl_stub = new KLineServiceImpl_Stub();
        List<KLinePieceVO>  vos  = null;
        try {
            vos = kLineServiceImpl_stub.getDailyKLine("code",first,second);
        } catch (DateIndexException e) {
            e.printStackTrace();
        }
        ObservableList<KLinePieceVO> dayList=  FXCollections.observableArrayList();
        for (KLinePieceVO temp : vos) {
            dayList.add(temp);
        }
        //System.out.println("list size:   "+dayList.size());
        KLineChart candleStickChart = KLineChart.createChart(dayList);
        DayLinePane.centerProperty().setValue(candleStickChart);


    }
}
