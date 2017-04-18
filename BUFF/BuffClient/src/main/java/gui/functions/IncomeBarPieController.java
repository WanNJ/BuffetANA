package gui.functions;

import blservice.strategy.StrategyService;
import exceptions.NoValidValueException;
import factory.BLFactorySeviceOnlyImpl;
import factory.BlFactoryService;
import gui.JavaFxHistogram.MyHistogram;
import gui.utils.Updatable;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import vo.ProfitDistributionPieVO;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@FXMLController(value = "/resources/fxml/ui/IncomeBarPie.fxml" , title = "IncomeBarPie")
public class IncomeBarPieController implements Updatable{
    @FXMLViewFlowContext private ViewFlowContext context;

    @FXML private StackPane peiChartPane;
    @FXML private BarChart<?, ?> barChart;
    @FXML private StackPane barChartPane;
    @FXML private PieChart pieChart;

    private BlFactoryService factory=new BLFactorySeviceOnlyImpl();
    private StrategyService strategyService=factory.createStrategyService();


    @PostConstruct
    public void init() {
        context.register(this);

//        factoryService = new BLFactorySeviceOnlyImpl();
//        strategyService = factoryService.createStrategyService();
//        setFakePieChart();
//        setFakeBarChartPane();
    }

//    private void setFakePieChart() {
//        ProfitDistributionPieVO fakeVO = new ProfitDistributionPieVO(20, 20, 25, 55, 10,40);
//        List<String> colorArray = new ArrayList<String>();
//        colorArray.add("-fx-pie-color: #006400;");
//        colorArray.add("-fx-pie-color: #33FF33;");
//        colorArray.add("-fx-pie-color: #99FF99;");
//        colorArray.add("-fx-pie-color: #FFCCCC;");
//        colorArray.add("-fx-pie-color: #FF7777;");
//        colorArray.add("-fx-pie-color: #FF0000;");
//
//        ObservableList<PieChart.Data> pieChartData =
//                FXCollections.observableArrayList(
//                        new PieChart.Data("小于 -7.5%", fakeVO.green75),
//                        new PieChart.Data("-3.5% ~ -7.5%", fakeVO.green35),
//                        new PieChart.Data("-3.5% ~ 0", fakeVO.green0),
//                        new PieChart.Data("0 ~ 3.5%", fakeVO.red0),
//                        new PieChart.Data("3.5% ~ 7.5%", fakeVO.red35),
//                        new PieChart.Data("大于 7.5%", fakeVO.red75));
//        pieChart = new PieChart(pieChartData);
//        pieChart.setTitle("相对收益分布");
//
//        final Label caption = new Label("");
//        caption.setTextFill(Color.BLACK);
//        caption.setStyle("-fx-font: 25 arial;");
//
//
//        for (int i = 0; i < pieChart.getData().size(); i++) {
//            PieChart.Data data = pieChart.getData().get(i);
//            data.getNode().setStyle(colorArray.get(i));
//            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED,
//                    e -> {
//                        caption.setTranslateX(e.getSceneX());
//                        caption.setTranslateY(e.getSceneY());
//                        caption.setText(String.valueOf(data.getPieValue()) + "%");
//                        caption.setVisible(true);
//                    });
//        }
//
//        pieChart.setLegendVisible(false);
//        peiChartPane.getChildren().set(0, pieChart);
//    }

    @FXML
    private void setFakeBarChartPane() throws Exception {
        List<Double> value = new ArrayList<>();
        Random generator = new Random();
        for (int i=1; i < 1000; i++) {
            value.add(generator.nextDouble() * Math.pow(-1, i));
        }
        StackPane pane = MyHistogram.getMySpiderChart(value);
        pane.setMinWidth(800);
        pane.setMinHeight(400);
        barChartPane.getChildren().set(0, pane);
    }

    @FXML
    private void setPieChart() {
        ProfitDistributionPieVO vo = strategyService.getProfitDistributePie();
        List<String> colorArray = new ArrayList<>();
        colorArray.add("-fx-pie-color: #006400;");
        colorArray.add("-fx-pie-color: #33FF33;");
        colorArray.add("-fx-pie-color: #99FF99;");
        colorArray.add("-fx-pie-color: #FFCCCC;");
        colorArray.add("-fx-pie-color: #FF7777;");
        colorArray.add("-fx-pie-color: #FF0000;");

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("小于 -7.5%", vo.green75),
                        new PieChart.Data("-3.5% ~ -7.5%", vo.green35),
                        new PieChart.Data("-3.5% ~ 0", vo.green0),
                        new PieChart.Data("0 ~ 3.5%", vo.red0),
                        new PieChart.Data("3.5% ~ 7.5%", vo.red35),
                        new PieChart.Data("大于 7.5%", vo.red75));

        pieChart = new PieChart(pieChartData);
        pieChart.setTitle("相对收益分布");

        final Label caption = new Label("");
        caption.setTextFill(Color.BLACK);
        caption.setStyle("-fx-font: 24 arial;");

        //TODO 解决显示不了百分比的问题
        for (int i = 0; i < pieChart.getData().size(); i++) {
            //设置颜色
            PieChart.Data data = pieChart.getData().get(i);
            if(data.getPieValue() == 0)
                data.getNode().setVisible(false);
            data.getNode().setStyle(colorArray.get(i));
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED,
                    e -> {
                        caption.setTranslateX(e.getSceneX());
                        caption.setTranslateY(e.getSceneY());
                        caption.setText(String.valueOf(data.getPieValue()) + "%");
                        caption.setVisible(true);
                    });
        }

        pieChart.setLegendVisible(false);
        peiChartPane.getChildren().set(0, pieChart);
    }

    @FXML
    private void setBarChartPane() {
        List<Double> value = strategyService.getProfitDistributeBar();
        StackPane pane = null;
        try {
            pane = MyHistogram.getMySpiderChart(value);
        } catch (NoValidValueException e) {
            //TODO 界面提示没有符合要求的数据
            return;
        }
        pane.setMinWidth(800);
        pane.setMinHeight(400);
        barChartPane.getChildren().set(0, pane);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateData() {
        setPieChart();
        setBarChartPane();
    }
}
