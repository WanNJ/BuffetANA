package gui.ChartController;

import javafx.scene.chart.Axis;
import javafx.scene.chart.XYChart;

/**
 * Created by wshwbluebird on 2017/3/8.
 */
public class VolBarChart extends XYChart<String,Number> {
    /**
     * Constructs a XYChart given the two axes. The initial content for the chart
     * plot background and plot area that includes vertical and horizontal grid
     * lines and fills, are added.
     *
     * @param stringAxis X Axis for this XY chart
     * @param numberAxis Y Axis for this XY chart
     */
    public VolBarChart(Axis<String> stringAxis, Axis<Number> numberAxis) {
        super(stringAxis, numberAxis);
    }

    @Override
    protected void dataItemAdded(Series<String, Number> series, int itemIndex, Data<String, Number> item) {

    }

    @Override
    protected void dataItemRemoved(Data<String, Number> item, Series<String, Number> series) {

    }

    @Override
    protected void dataItemChanged(Data<String, Number> item) {

    }

    @Override
    protected void seriesAdded(Series<String, Number> series, int seriesIndex) {

    }

    @Override
    protected void seriesRemoved(Series<String, Number> series) {

    }

    @Override
    protected void layoutPlotChildren() {

    }
}
