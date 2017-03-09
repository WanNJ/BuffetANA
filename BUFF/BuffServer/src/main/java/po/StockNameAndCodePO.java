package po;

/**
 * Created by slow_time on 2017/3/9.
 */
public class StockNameAndCodePO {
    private String name;
    private String code;

    public StockNameAndCodePO(String name, String code) {

        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
