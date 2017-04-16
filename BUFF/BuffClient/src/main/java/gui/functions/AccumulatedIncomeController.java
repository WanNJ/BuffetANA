package gui.functions;

import blservice.strategy.StrategyService;
import blstub.strategy.StrategyServiceImpl_Stub;
import com.jfoenix.controls.JFXTreeTableView;
import factory.BLFactorySeviceOnlyImpl;
import factory.BlFactoryService;
import gui.ChartController.chart.AccumulatedIncomeChart;
import gui.ChartController.chart.ClosePriceChart;
import gui.utils.Updatable;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import vo.BackDetailVO;
import vo.DailyClosingPriceVO;
import vo.DayRatePieceVO;

import javax.annotation.PostConstruct;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/3/24.
 */

@FXMLController(value = "/resources/fxml/ui/AccumulatedIncome.fxml" , title = "AccumulatedIncome")
public class AccumulatedIncomeController implements Updatable{
    @FXMLViewFlowContext private ViewFlowContext context;

    @FXML private VBox vBox;
    @FXML private VBox chartBox;
    @FXML private Label annualizedReturn;//年化收益率
    @FXML private Label baseAnnualizedReturn;//基准年化收益率
    @FXML private Label alpha;//阿尔法
    @FXML private Label beta;//贝塔
    @FXML private Label sharpeRatio;//夏普比率
    @FXML private Label returnVolatility;//收益波动率
    @FXML private Label informationRatio;//信息比率
    @FXML private Label maximumRetracement;//最大回撤
    @FXML private Label turnoverRate;//换手率
    @FXML private LineChart chart;

    private BlFactoryService factory;
    private StrategyService strategyService;
    private BackDetailVO backDetailVO;

    @PostConstruct
    public void init() throws FlowException {
        context.register(this);

//        factory = new BLFactorySeviceOnlyImpl();
//        strategyService = factory.createStrategyService();
//        backDetailVO = strategyService.getBackDetailVO();
//        setFakeChart();
//        setFakeBackDetailVO();
    }

    private  void setFakeChart() {
        List<DayRatePieceVO> list_1 = new ArrayList<>();
        list_1.add(new DayRatePieceVO(LocalDate.of(2000, 1, 1), -0.1));
        list_1.add(new DayRatePieceVO(LocalDate.of(2000, 2, 1), 0.2));
        list_1.add(new DayRatePieceVO(LocalDate.of(2000, 3, 1), 0.25));
        list_1.add(new DayRatePieceVO(LocalDate.of(2000, 4, 1), 0.7));

        List<DayRatePieceVO> list_2 = new ArrayList<>();
        list_2.add(new DayRatePieceVO(LocalDate.of(2000, 1, 1), -0.4));
        list_2.add(new DayRatePieceVO(LocalDate.of(2000, 2, 1), 0.1));
        list_2.add(new DayRatePieceVO(LocalDate.of(2000, 3, 1), 0.2));
        list_2.add(new DayRatePieceVO(LocalDate.of(2000, 4, 1), 0.5));

        chartBox.getChildren().remove(chart);
        chart = AccumulatedIncomeChart.createChart(getDayRatePieceObserverable(list_1), getDayRatePieceObserverable(list_2));
        chartBox.getChildren().add(chart);
    }

    private void setFakeBackDetailVO() {
        backDetailVO = new BackDetailVO(0.357, 0.124, 1.29, 0.238, 0.146, 0.97);
        annualizedReturn.setText(String.format("%.2f", backDetailVO.yearProfitRate * 100) + '%');
        baseAnnualizedReturn.setText(String.format("%.2f", backDetailVO.baseYearProfitRate * 100) + '%');
        alpha.setText(String.format("%.2f", backDetailVO.alpha * 100) + '%');
        beta.setText(String.format("%.2f", backDetailVO.beta * 100) + '%');
        sharpeRatio.setText(String.format("%.2f", backDetailVO.sharpRate));
        maximumRetracement.setText(String.format("%.2f", backDetailVO.largestBackRate * 100) + '%');
        returnVolatility.setText("--");
        informationRatio.setText("--");
        turnoverRate.setText("--");
    }

    private void setChart() {
        chartBox.getChildren().remove(chart);
        chart = AccumulatedIncomeChart.createChart(
                getDayRatePieceObserverable(strategyService.getStrategyDayRatePieceVO()),
                getDayRatePieceObserverable(strategyService.getBaseDayRatePieceVO()));
        chartBox.getChildren().add(chart);
    }

    private void setBackDetailVO() {
        annualizedReturn.setText(String.format("%.2f", backDetailVO.yearProfitRate * 100) + '%');
        baseAnnualizedReturn.setText(String.format("%.2f", backDetailVO.baseYearProfitRate * 100) + '%');
        alpha.setText(String.format("%.2f", backDetailVO.alpha * 100) + '%');
        beta.setText(String.format("%.2f", backDetailVO.beta * 100) + '%');
        sharpeRatio.setText(String.format("%.2f", backDetailVO.sharpRate));
        maximumRetracement.setText(String.format("%.2f", backDetailVO.largestBackRate * 100) + '%');
        returnVolatility.setText("--");
        informationRatio.setText("--");
        turnoverRate.setText("--");
    }

    private ObservableList<DayRatePieceVO> getDayRatePieceObserverable(List<DayRatePieceVO> list){
        ObservableList<DayRatePieceVO> observableList = FXCollections.observableArrayList();
        observableList.addAll(list);
        return observableList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateData() {
        strategyService=new StrategyServiceImpl_Stub();//TODO:待将stub换成真正的实现
        backDetailVO = strategyService.getBackDetailVO();
        setBackDetailVO();
        setChart();
    }
}
