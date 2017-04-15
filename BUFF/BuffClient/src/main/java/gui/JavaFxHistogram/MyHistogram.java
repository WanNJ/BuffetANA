package gui.JavaFxHistogram;

import gui.RadarChart.ChartCanvas;
import javafx.scene.layout.StackPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
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
        JFreeChart chart = ChartFactory.createHistogram( plotTitle, xaxis, yaxis,
                dataset, orientation, true, false, false);
        chart.getXYPlot().setRenderer(new NagetiveBarRenderer());
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
