package gui.functions;

import blservice.comparison.ComparisonService;
import blservice.exception.InvalidDateException;
import blservice.exception.InvalidStockCodeException;
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
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import vo.BasisAnalysisVO;
import vo.DailyClosingPriceVO;
import vo.DailyLogReturnVO;

import javax.swing.*;
import java.awt.*;
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




    /**
     * 自动补全提示框
     */
    @FXML private JFXPopup popup;


    /**
     * 返回的提示列表
     */
    @FXML private JFXListView stockList;

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

        // init Popup
        popup.setPopupContainer(root);
        root.getChildren().remove(popup);
//        mainStockCode.setOnMouseClicked((e) -> {
//            popup.setSource(mainStockCode);
//            popup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 5, 33);
//        });
        deputyStockCode.setOnMouseClicked((e) -> {
            popup.setSource(deputyStockCode);
            popup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 5, 33);
        });
        //stockList.getItems().addAll(new Label("Item1"),new Label("Item2"));

        //TODO ComboBox 获取值获取不到，待解决
        //解决  add by wsw



        mainStockCode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                stockList.getItems().clear();
                List<String> list = CodeComplementUtil.CODE_COMPLEMENT_UTIL.getComplement(newValue);

                if(list==null || list.size()==0)
                    list.add("No Suggestion");


                popup.setSource(mainStockCode)  ;
                popup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 5, 33);

                for (String str:list) {
                    Label label = new Label(str);
                    label.setOnMouseClicked(e->{
                        if(!str.equals("No Suggestion"))
                        handleTextMain(str);
                    });
                    stockList.getItems().add(label);
                }
            }
        });

        deputyStockCode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                stockList.getItems().clear();
                List<String> list = CodeComplementUtil.CODE_COMPLEMENT_UTIL.getComplement(newValue);

                if(list==null || list.size()==0)
                    list.add("No Suggestion");


                popup.setSource(deputyStockCode);
                popup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 5, 33);


                for (String str:list) {
                    Label label = new Label(str);

                    label.setOnMouseClicked(e->{

                        if(!str.equals("No Suggestion"))
                        handleTextDup(str);
                    });
                    stockList.getItems().add(label);
                }

            }
        });



        beginDatePicker.setOnAction(event -> {
            if (beginDatePicker.getValue().isAfter(endDatePicker.getValue())) {
                Dialogs.showMessage("起始日期不应早于结束日期", "请重新填写");
                return;
            }

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
        String temp = sep[0];
        mainStockCode.setText(sep[0]);
        int len = sep[1].length()-1;
        mainStockNameLabel.setText(sep[1].substring(0,len));


        /**
         * not sure work
         */
        root.getChildren().remove(popup);
    }


    private void handleTextDup(String str){
        String[] sep = str.split("\\(");
        deputyStockCode.setText(sep[0]);
        int len = sep[1].length()-1;
        deputyStockNameLabel1.setText(sep[1].substring(0,len));


        root.getChildren().remove(popup);
    }


    @FXML
    private void beginCompare() throws RemoteException{
        //判断是否存在空信息
        if (mainStockCode.getText().trim().equals("")) {
            Dialogs.showMessage("信息不完善", "请填入主股代号！");
            return;
        } else {
            if (deputyStockCode.getText().trim().equals("")) {
                Dialogs.showMessage("信息不完善", "请填入副股代号！");
                return;
            }
        }
        if (beginDatePicker.getValue().isAfter(endDatePicker.getValue())) {
            Dialogs.showMessage("起始日期不应早于结束日期", "请重新填写");
            return;
        }
        //判断数据格式是否合法
        if(!mainStockCode.getText().trim().matches("^[1-9]\\d*|0$")) {
            Dialogs.showMessage("股票代号格式不正确", "请输入有效的主股代号！(以数字构成)");
            return;
        } else {
            if (!deputyStockCode.getText().trim().matches("^[1-9]\\d*|0$")) {
                Dialogs.showMessage("股票代号格式不正确", "请输入格式正确的副股代号！(以数字构成)");
                return;
            }
        }

        try {
            comparisonService.init(mainStockCode.getText().trim(), deputyStockCode.getText().trim(), beginDatePicker.getValue(), endDatePicker.getValue());
        } catch (InvalidStockCodeException e) {
            e.printReason();
            Dialogs.showMessage("股票代码不存在", "请核对后重新输入！");
            return;
        } catch (InvalidDateException e) {
            e.printReason();
            Dialogs.showMessage("该日期范围内无数据", "请更换日期范围！");
            return;
        }

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
        deputyChangeRateLabel.setText(String.valueOf(String.format("%.2f", deputyBasisAnalysisVO.changeRate * 100) + "%"));

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


    class HLabel extends JLabel
    {
        private static final long serialVersionUID = 1L;
        private int start;
        private int end;

        @Override
        public void paint(Graphics g)
        {
            FontMetrics fontMetrics = g.getFontMetrics();

            String startString = getText().substring(0, start);
            String text = getText().substring(start, end);

            int startX = fontMetrics.stringWidth(startString);
            int startY = 0;

            int length = fontMetrics.stringWidth(text);
            int height = fontMetrics.getHeight();

            g.setColor(java.awt.Color.red);
            g.fillRect(startX, startY, length, height);

            super.paint(g);
        }

        public void highlightRegion(int start, int end)
        {
            this.start = start;
            this.end = end;
        }
    }
}