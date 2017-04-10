package gui.RadarChart;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;

public class MySpiderDemo extends Application {

    public static JFreeChart createChart() {
        CustomizeSpiderWebPlot spiderWebPlot = new CustomizeSpiderWebPlot(createDataset());
        JFreeChart jfreechart = new JFreeChart("前三个季度水果销售报告",
                TextTitle.DEFAULT_FONT, spiderWebPlot, false);
        LegendTitle legendtitle = new LegendTitle(spiderWebPlot);
        legendtitle.setPosition(RectangleEdge.BOTTOM);
        jfreechart.addSubtitle(legendtitle);
        return jfreechart;
    }

    public static DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String group1 = "苹果 ";

        dataset.addValue(5, group1, "一月份");
        dataset.addValue(6, group1, "二月份");
        dataset.addValue(4, group1, "三月份");
        dataset.addValue(2, group1, "四月份");
        dataset.addValue(5, group1, "五月份");
        dataset.addValue(5, group1, "六月份");
        dataset.addValue(5, group1, "七月份");
        dataset.addValue(8, group1, "八月份");

        String group2 = "橙子";
        dataset.addValue(3, group2, "一月份");
        dataset.addValue(3, group2, "二月份");
        dataset.addValue(4, group2, "三月份");
        dataset.addValue(7, group2, "四月份");
        dataset.addValue(4, group2, "五月份");
        dataset.addValue(5, group2, "六月份");
        dataset.addValue(3, group2, "七月份");
        dataset.addValue(3, group2, "八月份");

        String group3 = "香蕉";
        dataset.addValue(4, group3, "一月份");
        dataset.addValue(5, group3, "二月份");
        dataset.addValue(2, group3, "三月份");
        dataset.addValue(5, group3, "四月份");
        dataset.addValue(6, group3, "五月份");
        dataset.addValue(6, group3, "六月份");
        dataset.addValue(4, group3, "七月份");
        dataset.addValue(4, group3, "八月份");
        return dataset;
    }

    @Override
    public void start(Stage stage) throws Exception {
        JFreeChart chart = createChart();
        ChartCanvas canvas = new ChartCanvas(chart);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(canvas);
        // Bind canvas size to stack pane size.
        canvas.widthProperty().bind( stackPane.widthProperty());
        canvas.heightProperty().bind( stackPane.heightProperty());
        stage.setScene(new Scene(stackPane));
        stage.setTitle("WhatEver");
        stage.setWidth(700);
        stage.setHeight(390);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}