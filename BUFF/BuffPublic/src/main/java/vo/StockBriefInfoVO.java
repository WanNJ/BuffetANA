package vo;

import javafx.beans.property.*;

import java.time.LocalDate;

/**
 * Created by slow_time on 2017/3/5.
 */
public class StockBriefInfoVO {
    public StringProperty name;
    public ObjectProperty<LocalDate> date;
    public StringProperty code;
    public DoubleProperty closePrice;
    public DoubleProperty range;

    public StockBriefInfoVO(String name, LocalDate date, String code, double closePrice, double range) {
        this.name = new SimpleStringProperty(name);
        this.date = new SimpleObjectProperty<>(date);
        this.code = new SimpleStringProperty(code);
        this.closePrice = new SimpleDoubleProperty(closePrice);
        this.range = new SimpleDoubleProperty(range);
    }
}
