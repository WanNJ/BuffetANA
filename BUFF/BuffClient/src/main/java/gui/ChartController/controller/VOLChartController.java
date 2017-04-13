package gui.ChartController.controller;

import blservice.exception.DateIndexException;
import blservice.market.MarketService;
import blservice.singlestock.VolService;
import gui.ChartController.chart.KLineType;
import gui.ChartController.chart.VolBarChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import vo.StockVolVO;

import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by wshwbluebird on 2017/3/11.
 * 用于画volchart的控制器
 * 不是fxml的控制器!!!!!!!!
 */
public class VOLChartController implements Initializable {
    /**
     * 动态存储图表
     */
    public VolBarChart Chart;

    /**
     * 动态存储传过来的 list
     */
    public ObservableList<StockVolVO> dataList;

    /**
     * 记录用户选择的股票代码
     */
    private String stockCode;

    /**
     * 记录用户选择的时间
     */
    private LocalDate startDate, endDate;

    /**
     * 获取传进来的service实现
     */
    private VolService volService;

    /**
     * 记录当前控制器要画的vol类型
     */
    private KLineType currentType;

    /**
     * 获取传进来的Marketservice实现
     */
    private MarketService marketService;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * 隐藏的初始化方法
     */
    public  VOLChartController(){
        stockCode = "ALL";
        startDate = LocalDate.of(2014,9,1);
        endDate =   LocalDate.of(2014,9,20);
        dataList = FXCollections.observableArrayList();
        dataList.clear();
        currentType = KLineType.Daily;
    }




    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }



    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }



    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }



    public void setVolService(VolService volService) {
        this.volService = volService;
    }

    public void setMarketService(MarketService marketService){
        this.marketService = marketService;
    }


    public void setCurrentType(KLineType kLineType){
        this.currentType = kLineType;
    }


    /**
     * 根据已经存储的值获取数据
     */
    private void getData(){
        if(volService == null && stockCode !="ALL"){
            System.err.println("没有VOLLineService的实现传入");
            return ;
        }
        dataList.clear();


        if(stockCode == "ALL"){
            dataList.clear();
            List<StockVolVO> dayList = new ArrayList<StockVolVO>();
            try {

                dayList = marketService.getMarketVol(startDate,endDate);
            } catch (DateIndexException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            dataList = getObeservableList(dayList);
            return ;
        }

        //目前不区分周和月
        //TODO  未来可能区分周和月
        List<StockVolVO> dayList = new ArrayList<StockVolVO>();
        try {
            dayList = volService.getStockVol(stockCode,startDate,endDate);
        } catch (DateIndexException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        dataList = getObeservableList(dayList);


    }


    /**
     * 将传过来的list类型进行转化
     * @param tempList
     * @return
     */
    private ObservableList<StockVolVO> getObeservableList( List<StockVolVO> tempList){
        ObservableList<StockVolVO> dayList=  FXCollections.observableArrayList();
        for (StockVolVO temp : tempList) {
            dayList.add(temp);
        }
        return dayList;
    }


    public void drawChat(){
        getData();
        this.Chart = VolBarChart.createChart(this.dataList);
    }

    public VolBarChart getChart(){
        return this.Chart;
    }
}

