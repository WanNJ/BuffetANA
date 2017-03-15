package gui.functions;

import blservice.comparison.ComparisonService;
import com.jfoenix.controls.*;
import factory.BlFactoryService;
import factory.BlFactoryServiceImpl;
import gui.ChartController.ClosePriceChart;
import gui.ChartController.LRChart;
import gui.utils.DatePickerUtil;
import io.datafx.controller.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import vo.BasisAnalysisVO;
import vo.DailyClosingPriceVO;
import vo.DailyLogReturnVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Accident on 2017/3/5.
 */
@FXMLController(value = "/resources/fxml/ui/Comparison.fxml" , title = "两股对比")
public class ComparisonController {
    @FXML
    private Label mainStockNameLabel;
    @FXML
    private Label mainMinPriceLabel;
    @FXML
    private Label mainMaxPriceLabel;
    @FXML
    private Label mainChangeRateLabel;
    @FXML
    private Label mainVarianceOfLRLabel;
    @FXML
    private Label deputyStockNameLabel1;
    @FXML
    private Label deputyMinPriceLabel;
    @FXML
    private Label deputyMaxPriceLabel;
    @FXML
    private Label deputyChangeRateLabel;
    @FXML
    private Label deputyVarianceOfLRLabel;
    @FXML
    private JFXTextField mainStockCode;
    @FXML
    private JFXTextField deputyStockCode;
    @FXML
    private JFXDatePicker beginDatePicker;
    @FXML
    private JFXDatePicker endDatePicker;
    @FXML
    private ClosePriceChart closePriceChart;
    @FXML
    private LRChart logReturnChart;
    @FXML
    private JFXToggleButton dailyClosePriceToggleButton;
    @FXML
    private JFXToggleButton dailyLRToggleButton;
    @FXML
    private StackPane root;
    @FXML
    private JFXButton confirmButton;
    @FXML
    private BorderPane closeBorderPane;
    @FXML
    private BorderPane rlBorderPane;
    @FXML
    private StackPane messagePane;

    private ComparisonService comparisonService;
    private BlFactoryService blFactoryService;

    @FXML
    private void initialize() {
        blFactoryService = new BlFactoryServiceImpl();
        comparisonService = blFactoryService.createComparisonService();

        //设置DatePicker
        beginDatePicker.setDialogParent(root);
        endDatePicker.setDialogParent(root);
        DatePickerUtil.initDatePicker(beginDatePicker,endDatePicker);

        //TODO ComboBox 获取值获取不到，待解决
        mainStockCode.setText("1");
        deputyStockCode.setText("2");
    }

    @FXML
    private void search() {
        //TODO 增加股票搜索功能
    }

    @FXML
    private void beginCompare() throws RemoteException {
        //TODO 股票代码为空或无效，或者数据为空时的处理代码
        comparisonService.init(mainStockCode.getText(), deputyStockCode.getText(), beginDatePicker.getValue(), endDatePicker.getValue());
        setMainInfo();
        setDeputyInfo();
        setClosePriceChart();
        setLogReturnChart();
    }

    @FXML
    private void setMainInfo() throws RemoteException {
        BasisAnalysisVO mainBasisAnalysisVO = comparisonService.getMainBasisAnalysis();
        mainMinPriceLabel.setText(String.valueOf(mainBasisAnalysisVO.lowPrice));
        mainMaxPriceLabel.setText(String.valueOf(mainBasisAnalysisVO.highPrice));
        mainChangeRateLabel.setText(String.format("%.2f", mainBasisAnalysisVO.changeRate * 100) + "%");

        double varianceOfLR = comparisonService.getMainLogReturnVariance();
        mainVarianceOfLRLabel.setText(String.format("%.2f", varianceOfLR));
    }

    @FXML
    private void setDeputyInfo() throws RemoteException {
        BasisAnalysisVO deputyBasisAnalysisVO = comparisonService.getDeputyBasisAnalysis();
        deputyMinPriceLabel.setText(String.valueOf(deputyBasisAnalysisVO.lowPrice));
        deputyMaxPriceLabel.setText(String.valueOf(deputyBasisAnalysisVO.highPrice));
        deputyChangeRateLabel.setText(String.valueOf(String.format("%.2f", deputyBasisAnalysisVO.changeRate * 100) + "%"));

        double varianceOfLR = comparisonService.getDeputyLogReturnVariance();
        deputyVarianceOfLRLabel.setText(String.format("%.2f", varianceOfLR));
    }

    private void setClosePriceChart() throws RemoteException {
        closeBorderPane.getChildren().clear();
        ClosePriceChart closePriceChart = ClosePriceChart.createChart(
                getCloseObserable(comparisonService.getMainDailyClosingPrice()),
                getCloseObserable(comparisonService.getDeputyDailyClosingPrice()));
        closeBorderPane.centerProperty().setValue(closePriceChart);
    }

    private void setLogReturnChart() throws RemoteException {
        rlBorderPane.getChildren().clear();
        LRChart lrChart = LRChart.createChart(
                getrlObserable(comparisonService.getMainDailyLogReturnAnalysis()),
                getrlObserable(comparisonService.getDeputyDailyLogReturnAnalysis()));
        comparisonService.getMainDailyLogReturnAnalysis().forEach(vo -> System.out.println(vo.logReturnIndex));
        comparisonService.getDeputyDailyLogReturnAnalysis().forEach(vo -> System.out.println(vo.logReturnIndex));

        rlBorderPane.centerProperty().setValue(lrChart);
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


    /**
     *
     * @param list
     * @return ObservableList
     */
    private ObservableList<DailyClosingPriceVO> getCloseObserable(List<DailyClosingPriceVO> list){
        ObservableList<DailyClosingPriceVO> observableList = FXCollections.observableArrayList();
        for (DailyClosingPriceVO temp : list) {
            observableList.add(temp);
        }
        return observableList;

    }

    /**
     *
     * @param list
     * @return ObservableList
     */
    private ObservableList<DailyLogReturnVO> getrlObserable(List<DailyLogReturnVO> list){
        ObservableList<DailyLogReturnVO> observableList = FXCollections.observableArrayList();
        for (DailyLogReturnVO temp : list) {
            observableList.add(temp);
        }
        return observableList;

    }
}