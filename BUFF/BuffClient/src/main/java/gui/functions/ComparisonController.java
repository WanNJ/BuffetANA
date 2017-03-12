package gui.functions;

import blservice.comparison.ComparisonService;
import com.jfoenix.controls.JFXTextField;
import com.sun.xml.internal.bind.v2.TODO;
import factory.BlFactoryService;
import factory.BlFactoryServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.util.Callback;
import javafx.util.StringConverter;
import vo.BasisAnalysisVO;

import java.time.LocalDate;

/**
 * Created by Accident on 2017/3/5.
 */
public class ComparisonController {
    @FXML
    private Label mainMinPriceLabel;
    @FXML
    private Label mainMaxPriceLabel;
    @FXML
    private Label mainChangeRateLabel;
    @FXML
    private Label mainVarianceOfLR;
    @FXML
    private Label deputyMinPriceLabel;
    @FXML
    private Label deputyMaxPriceLabel;
    @FXML
    private Label deputyChangeRateLabel;
    @FXML
    private Label deputyVarianceOfLR;
    @FXML
    private JFXTextField mainStockCodeTextField;
    @FXML
    private JFXTextField deputyStockCodeTextField;
    @FXML
    private DatePicker beginDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private LineChart<LocalDate, Number> mainLineChart;
    @FXML
    private LineChart<LocalDate, Number> deputyLineChart;

    private BasisAnalysisVO mainBasisAnalysisVO;
    private BasisAnalysisVO deputyBasisAnalysisVO;
    private ComparisonService comparisonService;
    private BlFactoryService blFactoryService;

    @FXML
    private void initialize() {
        blFactoryService = new BlFactoryServiceImpl();
        comparisonService = blFactoryService.createComparisonService();
        setDatePicker();
    }

    @FXML
    private void setMainInfo() {
        mainBasisAnalysisVO = comparisonService.getBasisAnalysis(mainStockCodeTextField.getText(),beginDatePicker.getValue(), endDatePicker.getValue());
        mainMinPriceLabel.setText(String.valueOf(mainBasisAnalysisVO.lowPrice));
        mainMaxPriceLabel.setText(String.valueOf(mainBasisAnalysisVO.highPrice));
        mainChangeRateLabel.setText(String.valueOf(mainBasisAnalysisVO.changeRate));

        //TODO 设置主对数收益率图表


        //TODO 设置对数收益率方差，注意在BlImpl中防止NullPointer
    }

    @FXML
    private void setDeputyBasicInfo() {
        deputyBasisAnalysisVO = comparisonService.getBasisAnalysis(deputyMaxPriceLabel.getText(),beginDatePicker.getValue(), endDatePicker.getValue());
        deputyMinPriceLabel.setText(String.valueOf(deputyBasisAnalysisVO.lowPrice));
        deputyMaxPriceLabel.setText(String.valueOf(deputyBasisAnalysisVO.highPrice));
        deputyChangeRateLabel.setText(String.valueOf(deputyBasisAnalysisVO.changeRate));

        //TODO 设置主对数收益率图表


        //TODO 设置对数收益率方差，注意在BlImpl中防止NullPointer
    }

    private void setMainLineChart() {
    }

    private void setDatePicker() {
        beginDatePicker.setValue(LocalDate.now());
        beginDatePicker.setEditable(false);
        endDatePicker.setEditable(false);
        beginDatePicker.setDayCellFactory(
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item.isBefore(comparisonService.getEarliestDate()) || item.isAfter(LocalDate.now())) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: rgb(160,160,160);");
                                }
                            }
                        };
                    }});

        endDateFilter();
    }

    @FXML
    private void endDateFilter() {
        endDatePicker.setDayCellFactory(getEndDateFilter());
        endDatePicker.setValue(beginDatePicker.getValue().plusDays(1));
    }

    private Callback<DatePicker, DateCell> getEndDateFilter() {
        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item.isBefore(beginDatePicker.getValue().plusDays(1)) ||
                                        item.isAfter(LocalDate.now())
                                        ) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: rgb(160,160,160);");
                                }
                            }
                        };
                    }
                };
        return dayCellFactory;
    }
}