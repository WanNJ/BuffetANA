package gui.functions;

import blservice.comparison.ComparisonService;
import com.jfoenix.controls.*;
import com.sun.xml.internal.bind.v2.TODO;
import factory.BlFactoryService;
import factory.BlFactoryServiceImpl;
import gui.utils.DatePickerUtil;
import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.StringConverter;
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
    private JFXComboBox<String> mainStockCodeBox;
    @FXML
    private JFXComboBox<String> deputyStockCodeBox;
    @FXML
    private JFXDatePicker beginDatePicker;
    @FXML
    private JFXDatePicker endDatePicker;
    @FXML
    private LineChart<String, Number> mainLineChart;
    @FXML
    private LineChart<String, Number> deputyLineChart;
    @FXML
    private JFXToggleButton dailyClosePriceToggleButton;
    @FXML
    private JFXToggleButton dailyLRToggleButton;
    @FXML
    private StackPane root;
    @FXML
    private JFXButton confirmButton;

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
        mainStockCodeBox.setValue("1");
        deputyStockCodeBox.setValue("2");
    }

    @FXML
    private void search() {
        //TODO 增加股票搜索功能
    }

    @FXML
    private void beginCompare() throws RemoteException {
        setMainInfo();
        setDeputyInfo();
    }

    @FXML
    private void setMainInfo() throws RemoteException {
        //TODO 股票代码为空，或者数据为空时的处理代码
        comparisonService.setDateRange(mainStockCodeBox.getValue(),beginDatePicker.getValue(), endDatePicker.getValue());
        BasisAnalysisVO mainBasisAnalysisVO = comparisonService.getBasisAnalysis(mainStockCodeBox.getValue(),beginDatePicker.getValue(), endDatePicker.getValue());
        mainMinPriceLabel.setText(String.valueOf(mainBasisAnalysisVO.lowPrice));
        mainMaxPriceLabel.setText(String.valueOf(mainBasisAnalysisVO.highPrice));
        mainChangeRateLabel.setText(String.format("%.2f", mainBasisAnalysisVO.changeRate * 100) + "%");

        setMainLine();

        double varianceOfLR = comparisonService.getLogReturnVariance(mainStockCodeBox.getValue(),beginDatePicker.getValue(), endDatePicker.getValue());
        mainVarianceOfLRLabel.setText(String.format("%.2f", varianceOfLR * 100) + "%");
    }

    @FXML
    private void setDeputyInfo() throws RemoteException {
        //TODO 股票代码为空，或者数据为空时的处理代码
        comparisonService.setDateRange(deputyStockCodeBox.getValue(),beginDatePicker.getValue(), endDatePicker.getValue());
        BasisAnalysisVO deputyBasisAnalysisVO = comparisonService.getBasisAnalysis(deputyStockCodeBox.getValue(),beginDatePicker.getValue(), endDatePicker.getValue());
        deputyMinPriceLabel.setText(String.valueOf(deputyBasisAnalysisVO.lowPrice));
        deputyMaxPriceLabel.setText(String.valueOf(deputyBasisAnalysisVO.highPrice));
        deputyChangeRateLabel.setText(String.valueOf(String.format("%.2f", deputyBasisAnalysisVO.changeRate * 100) + "%"));

        setDeputyLine();

        double varianceOfLR = comparisonService.getLogReturnVariance(deputyStockCodeBox.getValue(),beginDatePicker.getValue(), endDatePicker.getValue());
        deputyVarianceOfLRLabel.setText(String.format("%.2f", varianceOfLR * 100) + "%");
    }

    private void setMainLine() {
        final CategoryAxis mainXAxis = new CategoryAxis();
        final NumberAxis mainYAxis = new NumberAxis();
        mainXAxis.setLabel("日期");

        mainLineChart = new LineChart<String,Number>(mainXAxis,mainYAxis);

        //创建每日收盘价Series
        XYChart.Series dailyClosePriceLine = new XYChart.Series();
        dailyClosePriceLine.setName("收盘价");
        //获取每日收盘价List（调用Service）
        List<DailyClosingPriceVO> dailyClosingPriceVOS = null;
        try {
            dailyClosingPriceVOS = comparisonService.getDailyClosingPrice(mainStockCodeBox.getValue(), beginDatePicker.getValue(), endDatePicker.getValue());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //将数据加入图表
        dailyClosingPriceVOS.forEach(closePrice -> {
            dailyClosePriceLine.getData().add(new XYChart.Data(closePrice.date.toString(),
                    closePrice.closePrice));
        });

        //创建每日对数收益率Series
        XYChart.Series dailyLRLine = new XYChart.Series();
        dailyLRLine.setName("对数收益率");
        //获取每日对数收益率List
        List<DailyLogReturnVO> dailyLogReturnVOS = null;
        try {
            dailyLogReturnVOS = comparisonService.getDailyLogReturnAnalysis(mainStockCodeBox.getValue(), beginDatePicker.getValue(), endDatePicker.getValue());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //将数据加入图表
        dailyLogReturnVOS.forEach(LRVo -> {
            dailyLRLine.getData().add(new XYChart.Data(LRVo.date.toString(),
                    LRVo.logReturnIndex));
        });

        mainLineChart.getData().addAll(dailyClosePriceLine, dailyLRLine);
        mainLineChart.setVisible(true);
    }

    private void setDeputyLine() {
        //TODO 复用setMainLineChart
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