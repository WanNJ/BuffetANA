package gui.ChartController;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;

/**
 * Created by wshwbluebird on 2017/3/8.
 */
public class TooltipContentMutiStick extends GridPane {
    private Label dateValue = new Label();
    private Label volValue = new Label();
    private Label changeVolValue = new Label();


    public TooltipContentMutiStick() {
        Label open = new Label("日期:");
        Label close = new Label("上涨个数:");
        Label high = new Label("下跌个数:");

        open.getStyleClass().add("volstick-tooltip-label");
        close.getStyleClass().add("volstick-tooltip-label");
        high.getStyleClass().add("volstick-tooltip-label");


        setConstraints(open, 0, 0);  //参数：Node  column row
        setConstraints(dateValue, 1, 0);
        setConstraints(close, 0, 1);
        setConstraints(volValue, 1, 1);
        setConstraints(high, 0, 2);
        setConstraints(changeVolValue, 1, 2);



        getChildren().addAll(open, dateValue, close, volValue, high, changeVolValue);
    }


    public void update(String date, long vol, long change) {
        dateValue.setText(date);
        volValue.setText(String.valueOf(vol));
        changeVolValue.setText(String.valueOf(change));

    }
}

