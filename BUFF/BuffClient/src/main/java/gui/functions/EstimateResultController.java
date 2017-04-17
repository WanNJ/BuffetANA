package gui.functions;

import blservice.strategy.StrategyService;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import factory.BLFactorySeviceOnlyImpl;
import factory.BlFactoryService;
import gui.RadarChart.MySpiderChart;
import gui.utils.TreeTableViewUtil;
import gui.utils.TreeTableViewValue;
import gui.utils.Updatable;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import util.StrategyScoreVO;

import javax.annotation.PostConstruct;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by wshwbluebird on 2017/3/24.
 */

@FXMLController(value = "/resources/fxml/ui/EstimateResult.fxml" , title = "EstimateResult")
public class EstimateResultController implements Updatable{
    @FXMLViewFlowContext private ViewFlowContext context;

    @FXML private StackPane root;
    @FXML private JFXButton showStocks;//显示股票池
    @FXML private JFXDialog stockDialog;//显示股票池的Dialog
    @FXML private JFXListView<String> stockList;//显示股票池的listview
    @FXML private JFXButton acceptButton;//显示股票池的Dialog的确认Button
    @FXML JFXTreeTableView<Record> treeTableView; //持仓历史
    @FXML VBox vBox;
    @FXML HBox hBox;
    @FXML Label scoreLabel;

    private StackPane spiderChartScene;//雷达图的面板
    private ObservableList<Record> records;//所有持仓记录列表项的集合，动态绑定JFXTreeTableView的显示
    private int recordIndex=1;

    private BlFactoryService factory=new BLFactorySeviceOnlyImpl();
    private StrategyService strategyService=factory.createStrategyService();

    @PostConstruct
    public void init() throws FlowException {
        context.register(this);

        acceptButton.setOnAction(e->stockDialog.close());
        //显示目前选择的股票
        showStocks.setOnAction(event -> {
            try {
                stockList.getItems().setAll(strategyService.getAllStocksInPool().stream()
                        .map(stockNameAndCodeVO -> (stockNameAndCodeVO.code+"    "+stockNameAndCodeVO.name))
                        .collect(Collectors.toList()));
            }catch (Exception e){
                //return;
            }
            stockDialog.show(context.getRegisteredObject(StrategyBackTestingController.class).getRoot());
        });

        //初始化TreeTableView
        records = FXCollections.observableArrayList();
        String titles[]={"时间段编号","开始日期","结束日期","持仓期内的基准收益率","收益率","1000元收益"};
        TreeTableViewUtil.initTreeTableView(treeTableView,records,titles);
        treeTableView.getColumns().get(0).setPrefWidth(100);
        treeTableView.getColumns().get(1).setPrefWidth(150);
        treeTableView.getColumns().get(2).setPrefWidth(150);
        treeTableView.getColumns().get(3).setPrefWidth(200);
        treeTableView.getColumns().get(4).setPrefWidth(100);
        treeTableView.getColumns().get(5).setPrefWidth(100);
        //单独设定持仓股票列
        JFXTreeTableColumn<Record, ScrollPane> stockColum = new JFXTreeTableColumn<>("持仓股票");
        stockColum.setMinWidth(350);
        stockColum.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record, ScrollPane> param) ->{
            if(stockColum.validateValue(param)) return new ObservableObjectValue<ScrollPane>() {
                @Override
                public ScrollPane get() {
                    return getValue();
                }
                @Override
                public void addListener(ChangeListener<? super ScrollPane> listener) {}
                @Override
                public void removeListener(ChangeListener<? super ScrollPane> listener) {}
                @Override
                public ScrollPane getValue() {
                    ScrollPane scrollPane = new ScrollPane(new Label(param.getValue().getValue().values.get(6).getValue()));
                    scrollPane.setPrefHeight(100);
                    return  scrollPane;
                }
                @Override
                public void addListener(InvalidationListener listener) {}
                @Override
                public void removeListener(InvalidationListener listener) {}
            };
            else return stockColum.getComputedValue(param);
        });
        treeTableView.getColumns().add(stockColum);
    }

    @FXML
    private void setResult () {

    }

    private String getRecordIndex(){
        return (recordIndex++)+"";
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateData() {

        StrategyScoreVO strategyScoreVO = strategyService.getStrategyEstimateResult();
        try {
            hBox.getChildren().remove(spiderChartScene);
            spiderChartScene = MySpiderChart.getMySpiderChart(strategyScoreVO);
            spiderChartScene.setStyle("-fx-background-color: WHITE;");
            hBox.getChildren().add(spiderChartScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
        scoreLabel.setText(String.valueOf(strategyScoreVO.strategyScore));

        records.clear();
        records.addAll(strategyService.getPickleData().stream().map(pickleData -> new Record(getRecordIndex(),
                pickleData.beginDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                pickleData.endDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                pickleData.baseProfitRate+"",
                pickleData.getProfitPercent(),
                pickleData.getProfit_1000(),
                pickleData.getStockCodes_String()))
                .collect(Collectors.toList()));  //添加TreeTableView的行
    }

    /**
     * 每一行的值
     */
    private  class Record extends TreeTableViewValue<Record> {
        StringProperty ID;
        StringProperty beginDate;
        StringProperty endDate;
        StringProperty baseProfitRate;
        StringProperty profitPercent;
        StringProperty profit_1000;
        StringProperty stocks;

        /**
         *
         * @param ID 时间段编号
         * @param beginDate 开始日期
         * @param endDate 结束日期
         * @param baseProfitRate 持仓期内的基准收益率
         * @param profitPercent 收益率
         * @param profit_1000 1000元收益
         * @param stocks 持仓的股票
         */
        public Record(String ID, String beginDate, String endDate, String baseProfitRate, String profitPercent, String profit_1000, String stocks) {
            this.ID = new SimpleStringProperty(ID);
            this.beginDate = new SimpleStringProperty(beginDate);
            this.endDate = new SimpleStringProperty(endDate);
            this.baseProfitRate = new SimpleStringProperty(baseProfitRate);
            this.profitPercent = new SimpleStringProperty(profitPercent);
            this.profit_1000 = new SimpleStringProperty(profit_1000);
            this.stocks = new SimpleStringProperty(stocks);

            values.addAll(Arrays.asList(this.ID,this.beginDate,this.endDate,this.baseProfitRate,this.profitPercent,this.profit_1000,this.stocks));
        }
    }
}
