package gui.ChartController.controller;

import blservice.statistics.SingleCodePredictService;
import gui.ChartController.chart.NormHistChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import util.RangeF;
import vo.GuassLineVO;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by wshwbluebird on 2017/3/11.
 *
 * KLine 的controller  用于获取实时的K线图
 * TODO 未来改成集合的单例模式
 */
public class NormHistChartController implements Initializable {
    /**
     * 动态存储图表
     */
    public NormHistChart Chart;


    /**
     * 动态存储传过来的 list
     */
    public ObservableList<RangeF> rangeFObservableList;

    public ObservableList<GuassLineVO> guassLineVOObservableList;


    List<RangeF> rangeFs ;

    List<GuassLineVO> guassLineVOs;


    /**
     * 记录用户选择的时间
     */

    /**
     * 获取传进来的service实现
     */
    private SingleCodePredictService singleCodePredictService;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * 隐藏的初始化方法
     */
    public NormHistChartController(){
        rangeFObservableList = FXCollections.observableArrayList();
        guassLineVOObservableList = FXCollections.observableArrayList();
        rangeFObservableList.clear();
        guassLineVOObservableList.clear();

    }







    public void setRanges(List<RangeF> rangeFs) {
        this.rangeFs = rangeFs;
    }

    public void setGuassLineVOs(List<GuassLineVO> guassLineVOs) {
        this.guassLineVOs = guassLineVOs;
    }

    public void setSingleCodePredictService(SingleCodePredictService singleCodePredictService) {
        this.singleCodePredictService = singleCodePredictService;
    }

    /**
     * 根据已经存储的值获取数据
     */
    private void getData(){
//        if( singleCodePredictService == null ){
//            System.err.println("没有KLineService的实现传入");
//            return ;
//        }
//
//        if(stockCode==null){
//            System.err.println("没有传入stockcode");
//            return ;
//        }

        if(guassLineVOs!=null)
        this.guassLineVOObservableList = getObeservableListLine(this.guassLineVOs);
        if(rangeFs!=null)
        this.rangeFObservableList = getObeservableListHist(this.rangeFs);

    }


    /**
     * 将传过来的list类型进行转化
     * @param tempList
     * @return
     */
    private ObservableList<RangeF> getObeservableListHist( List<RangeF> tempList){
        ObservableList<RangeF> dayList=  FXCollections.observableArrayList();
        for (RangeF temp : tempList) {
            dayList.add(temp);
        }
        return dayList;
    }


    /**
     * 将传过来的list类型进行转化
     * @param tempList
     * @return
     */
    private ObservableList<GuassLineVO> getObeservableListLine( List<GuassLineVO> tempList){
        ObservableList<GuassLineVO> dayList=  FXCollections.observableArrayList();
        for (GuassLineVO temp : tempList) {
            dayList.add(temp);
        }
        return dayList;
    }


    public void drawChat(){
        getData();
        this.Chart = NormHistChart.createChart(rangeFObservableList,guassLineVOObservableList);


    }

    public NormHistChart getChart(){
        return this.Chart;
    }

}
