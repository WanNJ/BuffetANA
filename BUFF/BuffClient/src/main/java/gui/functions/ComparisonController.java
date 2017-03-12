package gui.functions;

import blservice.comparison.ComparisonService;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.sun.xml.internal.bind.v2.TODO;
import factory.BlFactoryService;
import factory.BlFactoryServiceImpl;
import gui.utils.DatePickerUtil;
import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import vo.BasisAnalysisVO;

import java.rmi.RemoteException;
import java.time.LocalDate;

/**
 * Created by Accident on 2017/3/5.
 */
@FXMLController(value = "/resources/fxml/ui/Comparison.fxml" , title = "两股对比")
public class ComparisonController {
    @FXML
    private Label mainMinPriceLabel;
    @FXML
    private Label mainMaxPriceLabel;
    @FXML
    private Label mainChangeRateLabel;
    @FXML
    private Label mainVarianceOfLRLabel;
    @FXML
    private Label deputyMinPriceLabel;
    @FXML
    private Label deputyMaxPriceLabel;
    @FXML
    private Label deputyChangeRateLabel;
    @FXML
    private Label deputyVarianceOfLRLabel;
    @FXML
    private JFXTextField mainStockCodeTextField;
    @FXML
    private JFXTextField deputyStockCodeTextField;
    @FXML
    private JFXDatePicker beginDatePicker;
    @FXML
    private JFXDatePicker endDatePicker;
    @FXML
    private LineChart<LocalDate, Number> mainLineChart;
    @FXML
    private LineChart<LocalDate, Number> deputyLineChart;
    @FXML
    private JFXToggleButton dailyClosePriceToggleButton;
    @FXML
    private JFXToggleButton dailyLRToggleButton;
    @FXML
    private StackPane root;

    private BasisAnalysisVO mainBasisAnalysisVO;
    private BasisAnalysisVO deputyBasisAnalysisVO;
    private ComparisonService comparisonService;
    private BlFactoryService blFactoryService;

    @FXML
    private void initialize() {
        blFactoryService = new BlFactoryServiceImpl();
        comparisonService = blFactoryService.createComparisonService();

        beginDatePicker.setDialogParent(root);
        endDatePicker.setDialogParent(root);
        DatePickerUtil.initDatePicker(beginDatePicker,endDatePicker);
        //setDatePicker();
    }

    @FXML
    private void setMainInfo() throws RemoteException {
        mainBasisAnalysisVO = comparisonService.getBasisAnalysis(mainStockCodeTextField.getText(),beginDatePicker.getValue(), endDatePicker.getValue());
        mainMinPriceLabel.setText(String.valueOf(mainBasisAnalysisVO.lowPrice));
        mainMaxPriceLabel.setText(String.valueOf(mainBasisAnalysisVO.highPrice));
        mainChangeRateLabel.setText(String.valueOf(mainBasisAnalysisVO.changeRate));

        //TODO 设置主对数收益率图表


        //TODO 设置对数收益率方差，注意在BlImpl中防止NullPointer
    }

    @FXML
    private void setDeputyBasicInfo() throws RemoteException {
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
                                try {
                                    if (item.isBefore(comparisonService.getEarliestDate()) || item.isAfter(LocalDate.now())) {
                                        setDisable(true);
                                        setStyle("-fx-background-color: rgb(160,160,160);");
                                    }
                                } catch (RemoteException e) {
                                    e.printStackTrace();
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