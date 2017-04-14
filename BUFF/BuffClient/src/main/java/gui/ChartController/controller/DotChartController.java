package gui.ChartController.controller;

import blservice.statistics.SingleCodePredictService;
import gui.ChartController.chart.DotChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import vo.PriceIncomeVO;

import java.util.List;

/**
 * Created by wshwbluebird on 2017/4/15.
 */
public class DotChartController {

    private  DotChart dotChart;

    private  ObservableList<PriceIncomeVO> dataList;

    private String stockCode;

    private int holdPeriod;

    private SingleCodePredictService singleCodePredictService;

    /**
     *
     */
    public DotChartController(){
        dataList = FXCollections.observableArrayList();
        dataList.clear();
        holdPeriod = 30;
    }


    public void setDataList(ObservableList<PriceIncomeVO> dataList) {
        this.dataList = dataList;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public void setSingleCodePredictService(SingleCodePredictService singleCodePredictService) {
        this.singleCodePredictService = singleCodePredictService;
    }

    public void setHoldPeriod(int holdPeriod) {
        this.holdPeriod = holdPeriod;
    }


    /**
     * 获取数据
     */
    private void getData() {
        if (singleCodePredictService == null && stockCode != "ALL") {
            System.err.println("没有KLineService的实现传入");
            return;
        }

        if(stockCode == null) {
            System.err.println("股票代码没有传入");
        }


        List<PriceIncomeVO>  priceIncomeVOs = singleCodePredictService.getDotByPeriod(stockCode,holdPeriod);

        this.dataList = getObeservableList(priceIncomeVOs);


    }



    /**
     * 将传过来的list类型进行转化
     * @param tempList
     * @return
     */
    private ObservableList<PriceIncomeVO> getObeservableList( List<PriceIncomeVO> tempList){
        ObservableList<PriceIncomeVO> dayList=  FXCollections.observableArrayList();
        for (PriceIncomeVO temp : tempList) {
            dayList.add(temp);
        }
        return dayList;
    }


    public void drawChat(){
        getData();
        this.dotChart =  DotChart.createChart(dataList);

    }

    public DotChart getChart(){
        return this.dotChart;
    }



}
