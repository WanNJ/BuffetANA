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

    /**
     * 股票的信息
     * @return 转换成String的股票信息
     */
    public String getStockCodes_String(){
        String result="";
        for(BackData backData:stockCodes){
            result+=backData.code+"   买入价:"+String.format("%.2f",backData.firstDayOpen)+"   卖出价:"+String.format("%.2f",backData.lastDayClose)+"\n";
        }
        return  result;
    }

    /**
     * 获取收益率
     * @return
     */
    public String getProfitPercent(){
        return getProfit(100)+"%";
    }

    /**
     * 获取1000元收益
     * @return
     */
    public String getProfit_1000(){
        return getProfit(1000);
    }

    private String getProfit(double principal){
        if(stockCodes==null || stockCodes.size()==0){
            return "/";
        }

        double profit=0;
        double principal_each=principal/stockCodes.size();
        for(BackData backData:stockCodes){
            double quantity=principal_each/backData.firstDayOpen;//所得到的股票数量
            profit+=quantity*backData.lastDayClose-principal_each;//减去本金
        }
        return (profit>=0?"+":"-")+String.format("%.2f",profit);
    }

}
