package blserviceimpl.strategy;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by slow_time on 2017/3/24.
 */
public class PickleData {
    public LocalDate beginDate;//这阶段的开始日期
    public  LocalDate endDate;
    public  List<BackData> stockCodes;

    /**
     * 持仓期内的基准收益率，待计算
     */
    public double baseProfitRate;


    public PickleData(){

    }

    public PickleData(LocalDate beginDate , LocalDate endDate , List<BackData> stockCodes){
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

    public List<BackData> getStockCodes() {
        return stockCodes;
    }

    public void setStockCodes(List<BackData> stockCodes) {
        this.stockCodes = stockCodes;
    }
}
