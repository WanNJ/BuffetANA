package gui.functions;

import gui.ChartController.ChartController;
import gui.ChartController.controller.KLineChartController;
import gui.ChartController.pane.KLinePane;
import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import java.time.LocalDate;

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
        DayLinePane.getChildren().clear();
        KLineChartController kLineChartController = ChartController.INSTANCE.getKLineChartController();
        kLineChartController.setStockCode(code);

        //TODO delete
        //System.out.println("first:   "+first);

        kLineChartController.setStartDate(first);
        kLineChartController.setEndDate(second);
        kLineChartController.drawChat();
        KLinePane kLinePane = new KLinePane(kLineChartController.getChart(),1.0);
        DayLinePane.centerProperty().setValue(kLinePane);
    }



}
