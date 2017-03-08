package gui.ChartController;

import com.sun.javafx.binding.StringFormatter;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;

/**
 * Created by wshwbluebird on 2017/3/8.
 */
public class TooltipContentVolStick extends GridPane {
    private Label dateValue = new Label();
    private Label volValue = new Label();
    private Label changeVolValue = new Label();
    private Label changeRateValue = new Label();

    public TooltipContentVolStick() {
        Label open = new Label("日期:");
        Label close = new Label("成交量:");
        Label high = new Label("变化量:");
        Label low = new Label("变化率:");
        open.getStyleClass().add("volstick-tooltip-label");
        close.getStyleClass().add("volstick-tooltip-label");
        high.getStyleClass().add("volstick-tooltip-label");
        low.getStyleClass().add("volstick-tooltip-label");

        setConstraints(open, 0, 0);  //参数：Node  column row
        setConstraints(dateValue, 1, 0);
        setConstraints(close, 0, 1);
        setConstraints(volValue, 1, 1);
        setConstraints(high, 0, 2);
        setConstraints(changeVolValue, 1, 2);
        setConstraints(low, 0, 3);
        setConstraints(changeRateValue, 1, 3);


        getChildren().addAll(open, dateValue, close, volValue, high, changeVolValue, low, changeRateValue);
    }


    public void update(LocalDate date, long vol, long change, double rate) {
        dateValue.setText(date.toString());
        volValue.setText(String.valueOf(vol));
        changeVolValue.setText(String.valueOf(change));
        changeRateValue.setText(String.format("%.5f",rate)+"%");
    }
}

