package gui.functions;

import blservice.statistics.IndustryCorrelationService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import factory.BLFactorySeviceOnlyImpl;
import factory.BlFactoryService;
import gui.utils.Dialogs;
import io.datafx.controller.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import vo.IndustryCorrelationVO;

import javax.annotation.PostConstruct;

/**
 * Created by slow_time on 2017/4/13.
 */

@FXMLController(value = "/resources/fxml/ui/IndustryCorrelation.fxml" , title = "IndustryCorrelation")
public class IndustryCorrelationController {

    @FXML
    StackPane correlationPane;

    @FXML JFXTextField holdingPeriodTextField;

    @FXML JFXButton button;

    @FXML Label code; // 最相关股票代码

    @FXML Label name;  //最相关股票名称

    @FXML Label correlation;   //相关系数

    @FXML Label profitRate;  //预测盈利率

    @FXML
    ScatterChart<Number, Number> scatterChart ;  // 散点图

    @FXML
    VBox vBox;

    private IndustryCorrelationService industryCorrelationService;
    private BlFactoryService factory;
    private String selectedCode;


    public void setIndustryCorrelationService(IndustryCorrelationService industryCorrelationService) {
        this.industryCorrelationService = industryCorrelationService;
    }

    public void setSelectedCode(String selectedCode) {
        this.selectedCode = selectedCode;
    }

    @PostConstruct
    public void init(){
        factory = new BLFactorySeviceOnlyImpl();
        setIndustryCorrelationService(factory.createIndustryCorrelationService());

        button.setOnAction((ActionEvent event) -> {
            if (holdingPeriodTextField.getText().length() == 0) {
                Dialogs.showMessage("错误", "持仓期不能为空");
            }
            else {
                if (holdingPeriodTextField.getText().startsWith("-")) {
                    Dialogs.showMessage("错误", "持仓期不能为负");
                }
                else {
                    showCorrelationResult(Integer.valueOf(holdingPeriodTextField.getText()));
                }
            }
        });
    }

    private void showCorrelationResult(int holdingPeriod) {
        IndustryCorrelationVO industryCorrelationVO = industryCorrelationService.getInIndustryCorrelationResult(selectedCode, holdingPeriod);
        code.setText(industryCorrelationVO.code);
        name.setText(industryCorrelationVO.name);
        correlation.setText(String.format("%.2f", industryCorrelationVO.correlation));
        profitRate.setText(String.format("%.2f", industryCorrelationVO.profitRate * 100) + "%");

        XYChart.Series series = new XYChart.Series();
        series.setName(selectedCode + " and " + industryCorrelationVO.code + "相关度");

        double minX = 100;
        double maxX = 0;
        double minY = 100;
        double maxY = 0;
        for(int i = 0; i < industryCorrelationVO.base.size(); i++) {
            if(industryCorrelationVO.base.get(i) > maxX)
                maxX = industryCorrelationVO.base.get(i);
            if(industryCorrelationVO.base.get(i) < minX)
                minX = industryCorrelationVO.base.get(i);
            if(industryCorrelationVO.compare.get(i) > maxY)
                maxY = industryCorrelationVO.compare.get(i);
            if(industryCorrelationVO.compare.get(i) < minY)
                minY = industryCorrelationVO.compare.get(i);
            series.getData().add(new XYChart.Data(industryCorrelationVO.base.get(i), industryCorrelationVO.compare.get(i)));
        }
        final NumberAxis xAxis = new NumberAxis(Math.floor(minX), Math.ceil(maxX), 0.1);
        final NumberAxis yAxis = new NumberAxis(Math.floor(minY), Math.ceil(maxY), 0.1);
        scatterChart = new ScatterChart<Number, Number>(xAxis, yAxis);
        scatterChart.getData().addAll(series);
        vBox.getChildren().set(2, scatterChart);
    }
}
