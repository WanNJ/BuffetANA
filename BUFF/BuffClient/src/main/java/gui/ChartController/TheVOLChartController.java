package gui.ChartController;

import blservice.exception.DateIndexException;
import blservice.exception.RangeException;
import blservice.singlestock.VolService;
import blservice.thermometer.ThermometerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import util.DateRange;
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
public class TheVOLChartController implements Initializable {
    /**
     * 动态存储图表
     */
    public ThemometerVolBarChart Chart;

    /**
     * 动态存储传过来的 list
     */
    public ObservableList<StockVolVO> dataList;

    /**
     * 记录用户选择的时间
     */
    private LocalDate startDate, endDate;

    /**
     * 获取传进来的service实现
     */
    private ThermometerService thermometerService;





    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * 隐藏的初始化方法
     */
    public TheVOLChartController(){

        startDate = LocalDate.of(2014,9,1);
        endDate =   LocalDate.of(2014,9,20);
        dataList = FXCollections.observableArrayList();
        dataList.clear();
    }




    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }



    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }



    public void setThermometerService(ThermometerService thermometerService) {
        this.thermometerService = thermometerService;
    }



    /**
     * 根据已经存储的值获取数据
     */
    private void getData(){
        if(thermometerService == null){
            System.err.println("没有thermometerService的实现传入");
            return ;
        }
        dataList.clear();


        //目前不区分周和月
        //TODO  未来可能区分周和月
        List<StockVolVO> dayList = new ArrayList<StockVolVO>();
        try {
            dayList = thermometerService.getTradingVolume(new DateRange(startDate,endDate));
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (RangeException e) {
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
        this.Chart = ThemometerVolBarChart.createChart(dataList);
    }

    public ThemometerVolBarChart getChart(){
        return this.Chart;
    }
}

