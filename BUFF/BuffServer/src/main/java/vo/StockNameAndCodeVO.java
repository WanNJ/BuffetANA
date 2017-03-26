package vo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

/**
 * Created by slow_time on 2017/3/9.
 */
public class StockNameAndCodeVO implements Serializable {

    public String name;
    public String code;

    public StockNameAndCodeVO() {

    }

    public StockNameAndCodeVO(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public StringProperty nameProperty() {
        return new SimpleStringProperty(name);
    }

    public StringProperty codeProperty() {
        return new SimpleStringProperty(code);
    }
}
