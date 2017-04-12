package gui.functions;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import gui.RadarChart.MySpiderChart;
import gui.utils.TreeTableViewUtil;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import util.StrategyScoreVO;

import javax.annotation.PostConstruct;

/**
 * Created by wshwbluebird on 2017/3/24.
 */

@FXMLController(value = "/resources/fxml/ui/EstimateResult.fxml" , title = "EstimateResult")
public class EstimateResultController {

    @FXMLViewFlowContext private ViewFlowContext context;

    @FXML JFXTreeTableView treeTableView; //持仓历史
    @FXML VBox vBox;
    @FXML Label scoreLabel;

    private ObservableList<Record> records;//所有持仓记录列表项的集合，动态绑定JFXTreeTableView的显示

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

        records = FXCollections.observableArrayList();
        records.addAll(new Record());  //添加行

        String titles[]={};
        StringProperty propertys[]={};
        TreeTableViewUtil.initTreeTableView(treeTableView,records,titles,propertys);

    }

    @FXML
    private void setResult () {

    }

    private  class Record extends RecursiveTreeObject<Record> {
        StringProperty ID;
        StringProperty stockCode;
        StringProperty stockName;
        StringProperty buyDate;
        StringProperty sellDate;
        StringProperty buyPrice;
        StringProperty sellPrice;
        StringProperty earnedPercent;
    }
}
