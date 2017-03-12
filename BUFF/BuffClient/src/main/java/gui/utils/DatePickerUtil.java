package gui.utils;

import javafx.event.EventType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Created by zjy on 2017/3/9.
 * @author zjy
 */
public class DatePickerUtil {
    private static final LocalDate lastDate=LocalDate.of(2014, 4, 29);

    /**
     * 初始化日期选择器，使得结束日期一定在开始日期之后，且提示时间段的天数
     * @param from 开始日期的日期选择器
     * @param to 结束日期的日期选择器
     */
    public static void initDatePicker(DatePicker from, DatePicker to) {
        to.setValue(lastDate);

        to.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(from.getValue()) || item.isAfter(lastDate)) {//如果结束日期在开始日期前或者晚于最晚日期
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                        long p = ChronoUnit.DAYS.between(from.getValue(), item)+1;
                        setTooltip(new Tooltip(p + "天内")
                        );
                    }
                };
            }
        });
        from.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isAfter(lastDate)) {//如果开始日期晚于今天
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                        long p = ChronoUnit.DAYS.between(to.getValue(), item);
                        setTooltip(new Tooltip(p<=0? -p+1 + "天内":"该日")
                        );
                    }
                };
            }
        });
        from.setValue(to.getValue().minusDays(1));

        from.addEventHandler(EventType.ROOT,event -> {
            if(from.getValue().isAfter(to.getValue())){
                to.setValue(from.getValue());
            }
        });
    }
}
