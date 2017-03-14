package gui.functions;

import blservice.comparison.ComparisonService;
import com.jfoenix.controls.*;
import com.sun.xml.internal.bind.v2.TODO;
import factory.BlFactoryService;
import factory.BlFactoryServiceImpl;
import gui.ChartController.ClosePriceChart;
import gui.ChartController.LRChart;
import gui.utils.DatePickerUtil;
import io.datafx.controller.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
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
        mainStockCodeBox.setValue("1");
        deputyStockCodeBox.setValue("2");
    }

    @FXML
    private void search() {
        //TODO 增加股票搜索功能
    }

    @FXML
    private void beginCompare() throws RemoteException {
        //TODO 股票代码为空或无效，或者数据为空时的处理代码
        comparisonService.init(mainStockCodeBox.getValue(), deputyStockCodeBox.getValue(), beginDatePicker.getValue(), endDatePicker.getValue());
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

    private void setClosePriceChart() {
        List<DailyClosingPriceVO> mainClosePriceVOS = null;
        List<DailyClosingPriceVO> deputyClosePriceVOs = null;
        try {
            //获取主每日收盘价List
            mainClosePriceVOS = comparisonService.getMainDailyClosingPrice();
            //获取副每日收盘价List
            deputyClosePriceVOs = comparisonService.getDeputyDailyClosingPrice();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        ObservableList<DailyClosingPriceVO> mainList =  FXCollections.observableArrayList(mainClosePriceVOS);
        ObservableList<DailyClosingPriceVO> deputyList =  FXCollections.observableArrayList(deputyClosePriceVOs);


        //TODO 将数据加入图表

        //TODO 创建每日对数收益率Series
    }

    private void setLogReturnChart() {
        //TODO 与之前类似
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