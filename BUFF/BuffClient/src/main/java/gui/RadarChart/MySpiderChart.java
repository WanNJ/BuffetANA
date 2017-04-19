package gui.RadarChart;

import javafx.scene.layout.StackPane;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import util.StrategyScoreVO;

import java.awt.*;

public class MySpiderChart {

    private static JFreeChart createChart(StrategyScoreVO vo) {
        CustomizeSpiderWebPlot spiderWebPlot = new CustomizeSpiderWebPlot(createDataset(vo));
        spiderWebPlot.setOutlineVisible(false);
        spiderWebPlot.setWebFilled(true);
        JFreeChart jfreechart = new JFreeChart("",
                TextTitle.DEFAULT_FONT, spiderWebPlot, false);
        jfreechart.setBorderVisible(false);
        jfreechart.setBackgroundPaint(Color.WHITE);
        return jfreechart;
    }

    private static DefaultCategoryDataset createDataset(StrategyScoreVO vo) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String  property = "五维参数";

        dataset.addValue(vo.profitAbility, property, "盈利能力");
        dataset.addValue(vo.stability, property, "稳定性");
        dataset.addValue(vo.chooseStockAbility, property, "选股能力");
        dataset.addValue(vo.absoluteProfit, property, "绝对收益");
        dataset.addValue(vo.antiRiskAbility, property, "抗风险能力");

        return dataset;
    }

    public static StackPane getMySpiderChart(StrategyScoreVO vo) throws Exception {
        JFreeChart chart = createChart(vo);
        ChartCanvas canvas = new ChartCanvas(chart);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(canvas);
        stackPane.setMinHeight(300);
        stackPane.setMinWidth(400);
        // Bind canvas size to stack pane size.
        canvas.widthProperty().bind( stackPane.widthProperty());
        canvas.heightProperty().bind( stackPane.heightProperty());
        return stackPane;
    }
}