package vo;

import javafx.beans.property.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by slow_time on 2017/3/5.
 */
public class StockBriefInfoVO implements Serializable {
    public String name;
    public LocalDate date;
    public String code;
    public double closePrice;
    public double range;

    public StockBriefInfoVO(String name, LocalDate date, String code, double closePrice, double range) {
        this.name = name;
        this.date = date;
        this.code = code;
        this.closePrice = closePrice;
        this.range = range;
    }

    public StringProperty nameProperty() {
        return new SimpleStringProperty(name);
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return new SimpleObjectProperty<>(date);
    }

    public StringProperty codeProperty() {
        return new SimpleStringProperty(code);
    }

    public DoubleProperty closePriceProperty() {
        return new SimpleDoubleProperty(closePrice);
    }

    public StringProperty rangeProperty() {
        return new SimpleStringProperty(String.format("%.2f", range * 100) + "%");
    }
}
