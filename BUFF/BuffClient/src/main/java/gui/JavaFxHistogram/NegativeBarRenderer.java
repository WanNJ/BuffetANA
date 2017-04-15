package gui.JavaFxHistogram;

import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.XYDataset;

import java.awt.*;

public class NegativeBarRenderer extends XYBarRenderer {
    public NegativeBarRenderer() {
        super();
    }
    public Paint getItemPaint(int x_row, int x_col) {
        XYDataset dataset = getPlot().getDataset();
        double l_value  = (double) dataset.getX(x_row, x_col);
        if (l_value < 0.0) {
            return Color.green;
        }
        else {
            return Color.red;
        }
    }
}