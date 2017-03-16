package gui.functions;

import blservice.comparison.ComparisonService;
import com.jfoenix.controls.*;
import factory.BlFactoryService;
import factory.BlFactoryServiceImpl;
import gui.ChartController.ClosePriceChart;
import gui.ChartController.LRChart;
import gui.utils.CodeComplementUtil;
import gui.utils.DatePickerUtil;
import gui.utils.Dialogs;
import io.datafx.controller.FXMLController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    @FXML private Label mainStockNameLabel;
    @FXML private Label mainMinPriceLabel;
    @FXML private Label mainMaxPriceLabel;
    @FXML private Label mainChangeRateLabel;
    @FXML private Label mainVarianceOfLRLabel;
    @FXML private Label deputyStockNameLabel1;
    @FXML private Label deputyMinPriceLabel;
    @FXML private Label deputyMaxPriceLabel;
    @FXML private Label deputyChangeRateLabel;
    @FXML private Label deputyVarianceOfLRLabel;
    @FXML private JFXTextField mainStockCode;
    @FXML private JFXTextField deputyStockCode;
    @FXML private JFXDatePicker beginDatePicker;
    @FXML private JFXDatePicker endDatePicker;
    @FXML private ClosePriceChart closePriceChart;
    @FXML private LRChart logReturnChart;
    @FXML private JFXToggleButton dailyClosePriceToggleButton;
    @FXML private JFXToggleButton dailyLRToggleButton;
    @FXML private StackPane root;
    @FXML private JFXButton confirmButton;
    @FXML private BorderPane closeBorderPane;
    @FXML private BorderPane rlBorderPane;
    @FXML private StackPane messagePane;
    @FXML private JFXPopup popup;
    @FXML private JFXListView stockList;

    private ComparisonService comparisonService;
    private BlFactoryService blFactoryService;

    private VBox compBoxMain;
    private VBox compBoxDup;


    @FXML
    private void initialize() {
        blFactoryService = new BlFactoryServiceImpl();
        comparisonService = blFactoryService.createComparisonService();

        //设置DatePicker
        beginDatePicker.setDialogParent(root);
        endDatePicker.setDialogParent(root);
        DatePickerUtil.initDatePicker(beginDatePicker,endDatePicker);

        // init Popup
        popup.setPopupContainer(root);
        root.getChildren().remove(popup);
        mainStockCode.setOnMouseClicked((e) -> {
            popup.setSource(mainStockCode);
            popup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 5, 33);
        });
        deputyStockCode.setOnMouseClicked((e) -> {
            popup.setSource(deputyStockCode);
            popup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 5, 33);
        });
        stockList.getItems().addAll(new Label("Item1"),new Label("Item2"));

        //TODO ComboBox 获取值获取不到，待解决
        //解决  add by wsw

        compBoxMain = new VBox();
        compBoxDup = new VBox();
        compBoxMain.setVisible(false);
        compBoxDup.setVisible(false);
        StackPane.setMargin(compBoxMain,new Insets(47,-1,-1,-1));
        StackPane.setMargin(compBoxDup,new Insets(370+47,-1,-1,-1));


        compBoxMain.setStyle("-fx-padding: 0 10 20 10");
        compBoxDup.setStyle("-fx-padding: 0 10 20 10");

        messagePane.getChildren().add(compBoxMain);
        messagePane.getChildren().add(compBoxDup);


        mainStockCode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                compBoxMain.getChildren().clear();
               if(newValue.equals("")){
                   compBoxMain.setVisible(false);
               }else{
                   compBoxMain.setVisible(true);
                   List<String> list = CodeComplementUtil.CODE_COMPLEMENT_UTIL.getComplement(newValue);
                   for (String str:list) {
                       Button button = new Button(str);
                       button.setPrefWidth(270);
                       button.setOnAction(t->handleTextMain(str));
                       compBoxMain.getChildren().add(button);
                   }

               }
            }
        });

        deputyStockCode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                compBoxDup.getChildren().clear();
                if(newValue.equals("")){
                    compBoxDup.setVisible(false);
                }else{
                    compBoxDup.setVisible(true);
                    List<String> list = CodeComplementUtil.CODE_COMPLEMENT_UTIL.getComplement(newValue);
                    for (String str:list) {
                        Button button = new Button(str);
                        button.setPrefWidth(270);
                        button.setOnAction(t->handleTextDup(str));
                        compBoxDup.getChildren().add(button);
                    }

                }
            }
        });


        mainStockCode.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue) {
                    compBoxMain.getChildren().clear();
                    compBoxMain.setVisible(false);
                }
            }
        });


        deputyStockCode.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    compBoxDup.getChildren().clear();
                    compBoxDup.setVisible(false);
                }
            }
        });

        beginDatePicker.setOnAction(event -> {
            try {
                beginCompare();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        endDatePicker.setOnAction(event -> {
            try {
                beginCompare();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }


    private void handleTextMain(String str){
        String[] sep = str.split("\\(");
        mainStockCode.setText(sep[0]);
        int len = sep[1].length()-1;
        mainStockNameLabel.setText(sep[1].substring(0,len));
        compBoxMain.getChildren().clear();
        compBoxMain.setVisible(false);
    }


    private void handleTextDup(String str){
        String[] sep = str.split("\\(");
        deputyStockCode.setText(sep[0]);
        int len = sep[1].length()-1;
        deputyStockNameLabel1.setText(sep[1].substring(0,len));
        compBoxDup.getChildren().clear();
        compBoxDup.setVisible(false);
    }


    @FXML
    private void beginCompare() throws RemoteException {
        if (mainStockCode.getText().trim().equals("")) {
            Dialogs.showMessage("信息不完善", "请填入主股代号！");
            return;
        } else {
            if (deputyStockCode.getText().trim().equals("")) {
                Dialogs.showMessage("信息不完善", "请填入主股代号！");
                return;
            }
        }
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
        mainVarianceOfLRLabel.setText(String.format("%.9f", varianceOfLR));
    }

    @FXML
    private void setDeputyInfo() throws RemoteException {
        BasisAnalysisVO deputyBasisAnalysisVO = comparisonService.getDeputyBasisAnalysis();
        deputyMinPriceLabel.setText(String.valueOf(deputyBasisAnalysisVO.lowPrice));
        deputyMaxPriceLabel.setText(String.valueOf(deputyBasisAnalysisVO.highPrice));
        deputyChangeRateLabel.setText(String.valueOf(String.format("%.9f", deputyBasisAnalysisVO.changeRate * 100) + "%"));

        double varianceOfLR = comparisonService.getDeputyLogReturnVariance();
        deputyVarianceOfLRLabel.setText(String.format("%.9f", varianceOfLR));
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