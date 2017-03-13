package gui.ChartController;

import blservice.exception.DateIndexException;
import blservice.exception.RangeException;
import blservice.singlestock.KLineService;
import blservice.thermometer.ThermometerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import util.DateRange;
import vo.KLinePieceVO;
import vo.LongPeiceVO;

import java.net.URL;
import java.rmi.RemoteException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by wshwbluebird on 2017/3/11.
 *
 *  比较涨停跌停股票的 柱状图
 * TODO 未来改成集合的单例模式
 */
public class UpDownChartController implements Initializable {
    /**
     * 动态存储图表
     */
    public UpDownChart BarChart;
    public UpDownLineChart LineChart;

    /**
     * 动态存储传过来的 list
     */
    public ObservableList<LongPeiceVO> upList;

    public ObservableList<LongPeiceVO> downList;



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
    public UpDownChartController(){
        startDate = LocalDate.of(2014,9,1);
        endDate =   LocalDate.of(2014,9,20);
        upList = FXCollections.observableArrayList();
        downList = FXCollections.observableArrayList();
        upList.clear();
        downList.clear();

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
        List<LongPeiceVO> ups = new ArrayList<LongPeiceVO>();
        List<LongPeiceVO> downs = new ArrayList<LongPeiceVO>();
        try {
            downs = thermometerService.getLimitDownNum(new DateRange(startDate,endDate));
            ups = thermometerService.getLimitUpNum(new DateRange(startDate,endDate));
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (RangeException e) {
            e.printStackTrace();
        }

        upList = getObeservableList(ups);
        downList = getObeservableList(downs);

    }


    /**
     * 将传过来的list类型进行转化
     * @param tempList
     * @return
     */
    private ObservableList<LongPeiceVO> getObeservableList( List<LongPeiceVO> tempList){
        ObservableList<LongPeiceVO> dayList=  FXCollections.observableArrayList();
        for (LongPeiceVO temp : tempList) {
            if(temp.localDate.getDayOfWeek() != DayOfWeek.SATURDAY
                    && temp.localDate.getDayOfWeek()!=DayOfWeek.SUNDAY)
            dayList.add(temp);
        }
        return dayList;
    }


    public void drawChat(){
        getData();
        this.BarChart = UpDownChart.createChart(this.upList,this.downList);
        this.LineChart =  UpDownLineChart.createChart(this.upList,this.downList);
    }


    public UpDownChart getBarChart(){
        return this.BarChart;
    }
    public UpDownLineChart getLineChart() {return  this.LineChart; }

}
