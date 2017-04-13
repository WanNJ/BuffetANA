package gui.functions;

import blservice.statistics.IndustryCorrelationService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import factory.BLFactorySeviceOnlyImpl;
import factory.BlFactoryService;
import io.datafx.controller.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import vo.IndustryCorrelationVO;

import javax.annotation.PostConstruct;

/**
 * Created by slow_time on 2017/4/13.
 */

@FXMLController(value = "/resources/fxml/ui/IndustryCorrelation.fxml" , title = "IndustryCorrelation")
public class IndustryCorrelationController {

    @FXML JFXTextField holdingPeriodTextField;

    @FXML JFXButton button;

    @FXML Label code; // 最相关股票代码

    @FXML Label name;  //最相关股票名称

    @FXML Label correlation;   //相关系数

    @FXML Label profitRate;  //预测盈利率

    @FXML
    ScatterChart<Number, Number> scatterChart ;  // 散点图

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

        button.setOnAction((ActionEvent event) -> showCorrelationResult(Integer.valueOf(holdingPeriodTextField.getText())));
    }

    private void showCorrelationResult(int holdingPeriod) {
        IndustryCorrelationVO industryCorrelationVO = industryCorrelationService.getInIndustryCorrelationResult(selectedCode, holdingPeriod);
        code.setText(industryCorrelationVO.code);
        name.setText(industryCorrelationVO.code);
        correlation.setText(String.format("%.2f", industryCorrelationVO.correlation));
        profitRate.setText(String.format("%.2f", industryCorrelationVO.profitRate * 100) + "%");

        XYChart.Series series = new XYChart.Series();
        series.setName(selectedCode + " and " + industryCorrelationVO.code + "相关度");
        for(int i = 0; i < industryCorrelationVO.base.size(); i++) {
            series.getData().add(new XYChart.Data(industryCorrelationVO.base.get(i), industryCorrelationVO.compare.get(i)));
        }
        scatterChart.getData().addAll(series);
    }
}
