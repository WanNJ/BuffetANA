package gui.JavaFxHistogram;

import gui.RadarChart.ChartCanvas;
import javafx.scene.layout.StackPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartTheme;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.urls.StandardXYURLGenerator;
import org.jfree.chart.util.ParamChecks;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

/**
 * Created by Accident on 2017/4/15.
 */

public class MyHistogram {
    private static JFreeChart createChart(HistogramDataset dataset) {
        String plotTitle = "相对收益分布直方图";
        String xaxis = "收益率";
        String yaxis = "次数";
        PlotOrientation orientation = PlotOrientation.VERTICAL;

        ParamChecks.nullNotPermitted(orientation, "orientation");
        NumberAxis xAxis = new NumberAxis(xaxis);
        xAxis.setAutoRangeIncludesZero(false);
        ValueAxis yAxis = new NumberAxis(yaxis);
        XYItemRenderer renderer = new NegativeBarRenderer();

        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        plot.setOrientation(orientation);
        plot.setDomainZeroBaselineVisible(true);
        plot.setRangeZeroBaselineVisible(true);
        JFreeChart chart = new JFreeChart(plotTitle, JFreeChart.DEFAULT_TITLE_FONT, plot, false);
        ChartTheme currentTheme = new StandardChartTheme("JFree");
        currentTheme.apply(chart);
        return chart;

    }

    private static HistogramDataset createDataset(double[] data) {
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.FREQUENCY);
        dataset.addSeries("Histogram", data, 100);

        return dataset;
    }

    public static StackPane getMySpiderChart(double[] data) throws Exception {
        HistogramDataset dataset = createDataset(data);
        JFreeChart chart = createChart(dataset);
        ChartCanvas canvas = new ChartCanvas(chart);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(canvas);
        stackPane.setMinHeight(600);
        stackPane.setMinWidth(800);
        // Bind canvas size to stack pane size.
        canvas.widthProperty().bind( stackPane.widthProperty());
        canvas.heightProperty().bind( stackPane.heightProperty());
        return stackPane;
    }
}
