package gui.functions;

import blservice.strategy.StrategyService;
import blstub.strategy.StrategyServiceImpl_Stub;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import gui.RadarChart.MySpiderChart;
import gui.utils.TreeTableViewUtil;
import gui.utils.TreeTableViewValue;
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
public class EstimateResultController {

    @FXMLViewFlowContext private ViewFlowContext context;

    @FXML JFXTreeTableView<Record> treeTableView; //持仓历史
    @FXML VBox vBox;
    @FXML Label scoreLabel;

    private ObservableList<Record> records;//所有持仓记录列表项的集合，动态绑定JFXTreeTableView的显示
    private int recordIndex=1;

    @PostConstruct
    public void init() throws FlowException {
        StrategyScoreVO fakeVo = new StrategyScoreVO(20, 21, 22, 23, 16, 92);
        StackPane spiderChartScene = null;
        try {
            spiderChartScene = MySpiderChart.getMySpiderChart(fakeVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        scoreLabel.setText(String.valueOf(fakeVo.strategyScore));
        vBox.getChildren().add(2, spiderChartScene);
        System.out.println("Bingo!");

        StrategyService strategyService=new StrategyServiceImpl_Stub();
        records = FXCollections.observableArrayList();
        records.addAll(strategyService.getPickleData().stream().map(pickleData -> new Record(getRecordIndex(),
                pickleData.beginDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                pickleData.endDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                pickleData.baseProfitRate+"",
                pickleData.getStockCodes_String()))
                .collect(Collectors.toList()));  //添加行

        String titles[]={"时间段编号","开始日期","结束日期","持仓期内的基准收益率"};
        TreeTableViewUtil.initTreeTableView(treeTableView,records,titles);
        treeTableView.getColumns().get(0).setPrefWidth(100);
        treeTableView.getColumns().get(1).setPrefWidth(150);
        treeTableView.getColumns().get(2).setPrefWidth(150);
        treeTableView.getColumns().get(3).setPrefWidth(250);

        JFXTreeTableColumn<Record, ScrollPane> stockColum = new JFXTreeTableColumn<>("持仓股票");
        stockColum.setMinWidth(450);
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
                    ScrollPane scrollPane = new ScrollPane(new Label(param.getValue().getValue().values.get(4).getValue()));
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
     * 每一行的值
     */
    private  class Record extends TreeTableViewValue<Record> {
        StringProperty ID;
        StringProperty beginDate;
        StringProperty endDate;
        StringProperty baseProfitRate;
        StringProperty stocks;

        /**
         *
         * @param ID 时间段编号
         * @param beginDate 开始日期
         * @param endDate 结束日期
         * @param baseProfitRate 持仓期内的基准收益率
         */
        public Record(String ID, String beginDate, String endDate, String baseProfitRate, String stocks) {
            this.ID = new SimpleStringProperty(ID);
            this.beginDate = new SimpleStringProperty(beginDate);
            this.endDate = new SimpleStringProperty(endDate);
            this.baseProfitRate = new SimpleStringProperty(baseProfitRate);
            this.stocks = new SimpleStringProperty(stocks);

            values.addAll(Arrays.asList(this.ID,this.beginDate,this.endDate,this.baseProfitRate,this.stocks));
        }
    }
}
