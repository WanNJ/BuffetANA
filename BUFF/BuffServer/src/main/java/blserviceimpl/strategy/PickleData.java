package blserviceimpl.strategy;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by slow_time on 2017/3/24.
 */
public class PickleData {
    public LocalDate beginDate;
    public  LocalDate endDate;
    public  List<String> stockCodes;


    public PickleData(){

    }

    public PickleData(LocalDate beginDate , LocalDate endDate , List<String> stockCodes){
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.stockCodes = stockCodes;
    }



    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<String> getStockCodes() {
        return stockCodes;
    }

    public void setStockCodes(List<String> stockCodes) {
        this.stockCodes = stockCodes;
    }
}
