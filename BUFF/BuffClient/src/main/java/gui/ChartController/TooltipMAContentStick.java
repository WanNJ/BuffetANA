package gui.ChartController;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 *
 * @author ss
 * @date 2016/3/24
 */

class TooltipMAContentStick extends GridPane {
    private Label MA5Value = new Label();
    private Label MA10Value = new Label();
    private Label MA30Value = new Label();
    private Label MA60Value = new Label();

    public TooltipMAContentStick() {
        Label open = new Label("MA05:");
        Label close = new Label("MA10:");
        Label high = new Label("MA30:");
        Label low = new Label("MA60:");

        open.getStyleClass().add("candlestick-tooltip-label");
        close.getStyleClass().add("candlestick-tooltip-label");
        high.getStyleClass().add("candlestick-tooltip-label");
        low.getStyleClass().add("candlestick-tooltip-label");

        setConstraints(open, 0, 0);  //参数：Node  column row
        setConstraints(MA5Value, 1, 0);
        setConstraints(close, 0, 1);
        setConstraints(MA10Value, 1, 1);
        setConstraints(high, 0, 2);
        setConstraints(MA30Value, 1, 2);
        setConstraints(low, 0, 3);
        setConstraints(MA60Value, 1, 3);
        getChildren().addAll(open, MA5Value, close, MA10Value, high, MA30Value, low, MA60Value);
    }

    public void update(double open, double close, double high, double low) {
        MA5Value.setText(Double.toString(open).substring(0,5));
        MA10Value.setText(Double.toString(close).substring(0,5));
        MA30Value.setText(Double.toString(high).substring(0,5));
        MA60Value.setText(Double.toString(low).substring(0,5));
    }
}

