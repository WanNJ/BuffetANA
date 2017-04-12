package gui.functions;

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
import javafx.beans.property.SimpleStringProperty;
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
import java.util.Arrays;
import java.util.Collections;

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
        records.addAll(new Record("时间段编号","开始日期","结束日期","持仓期内的基准收益率"));  //添加行

        String titles[]={"时间段编号","开始日期","结束日期","持仓期内的基准收益率"};
        TreeTableViewUtil.initTreeTableView(treeTableView,records,titles);

    }

    @FXML
    private void setResult () {

    }

    /**
     * 每一行的值
     */
    private  class Record extends TreeTableViewValue<Record> {
        StringProperty ID;
        StringProperty beginDate;
        StringProperty endDate;
        StringProperty baseProfitRate;

        /**
         *
         * @param ID 时间段编号
         * @param beginDate 开始日期
         * @param endDate 结束日期
         * @param baseProfitRate 持仓期内的基准收益率
         */
        public Record(String ID, String beginDate, String endDate, String baseProfitRate) {
            this.ID = new SimpleStringProperty(ID);
            this.beginDate = new SimpleStringProperty(beginDate);
            this.endDate = new SimpleStringProperty(endDate);
            this.baseProfitRate = new SimpleStringProperty(baseProfitRate);

            values.addAll(Arrays.asList(this.ID,this.beginDate,this.endDate,this.baseProfitRate));
        }
    }
}
