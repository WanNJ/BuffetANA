package gui.functions;

import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.StackPane;

import javax.annotation.PostConstruct;

@FXMLController(value = "/resources/fxml/ui/IncomeBarPie.fxml" , title = "IncomeBarPie")
public class IncomeBarPieController {
    @FXML
    private StackPane peiChartPane;

    @FXML
    private BarChart<?, ?> barChart;

    @FXML
    private StackPane barChartPane;

    @FXML
    private PieChart pieChart;

    @PostConstruct
    public void init(){

    }

}
