package gui.functions;

import gui.ChartController.ChartController;
import gui.ChartController.controller.MAChartController;
import gui.ChartController.pane.MALinePane;
import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import java.time.LocalDate;

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
//        LocalDate first = LocalDate.of(2015, 10, 1);
//        LocalDate second = LocalDate.of(2015, 10, 12);
//        MALineService maLineService = new MALineServiceImpl_Stub();
//        MAChartController maChartController = ChartController.INSTANCE.getMAChartController();
//        maChartController.setMaLineService(maLineService);
//        maChartController.drawChat();
//        borderPane.centerProperty().setValue(maChartController.getChart());


    }


    /**
     * 监听器改图
     * @param code
     * @param first
     * @param second
     */
    public void upDateGraph( String code ,LocalDate first , LocalDate second ){
        MAChartController maChartController = ChartController.INSTANCE.getMAChartController();
        maChartController.setStartDate(first);
        maChartController.setEndDate(second);
        maChartController.setStockCode(code);
        maChartController.drawChat();
        MALinePane maLinePane = new MALinePane(maChartController.getChart(),1.0);
        borderPane.centerProperty().setValue(maLinePane);
    }
}
