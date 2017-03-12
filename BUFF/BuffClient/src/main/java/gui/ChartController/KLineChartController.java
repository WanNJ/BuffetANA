package gui.ChartController;

import blservice.exception.DateIndexException;
import blservice.singlestock.KLineService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import vo.KLinePieceVO;

import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by wshwbluebird on 2017/3/11.
 *
 * KLine 的controller  用于获取实时的K线图
 * TODO 未来改成集合的单例模式
 */
public class KLineChartController implements Initializable {
    /**
     * 动态存储图表
     */
    public KLineChart Chart;

    /**
     * 动态存储传过来的 list
     */
    public ObservableList<KLinePieceVO> dataList;

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
    private  KLineService kLineService;

    /**
     * 记录当前控制器要画的K线图类型
     */
    private  KLineType  currentType;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * 隐藏的初始化方法
     */
    public  KLineChartController(){
        stockCode = "code";
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



    public void setkLineService(KLineService kLineService) {
        this.kLineService = kLineService;
    }


    public void setCurrentType(KLineType kLineType){
        this.currentType = kLineType;
    }


    /**
     * 根据已经存储的值获取数据
     */
    private void getData(){
        if(kLineService == null){
            System.err.println("没有KLineService的实现传入");
            return ;
        }
        dataList.clear();
        switch (currentType){

            case Daily:
                List<KLinePieceVO> dayList = new ArrayList<KLinePieceVO>();
                try {
                    //TODO  delete
                    System.out.println(stockCode+" "+startDate+ "  "+endDate);
                    dayList = kLineService.getDailyKLine(stockCode,startDate,endDate);
                } catch (DateIndexException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                dataList = getObeservableList(dayList);
                break;
            case Monthly:
                List<KLinePieceVO> monthList = new ArrayList<KLinePieceVO>();
                try {
                    monthList = kLineService.getMonthlyKLine(stockCode,startDate,endDate);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                dataList = getObeservableList(monthList);
                break;
            case Weekly:
                List<KLinePieceVO> weekList = new ArrayList<KLinePieceVO>();
                try {
                    weekList = kLineService.getMonthlyKLine(stockCode,startDate,endDate);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                dataList = getObeservableList(weekList);
                break;

        }

    }


    /**
     * 将传过来的list类型进行转化
     * @param tempList
     * @return
     */
    private ObservableList<KLinePieceVO> getObeservableList( List<KLinePieceVO> tempList){
        ObservableList<KLinePieceVO> dayList=  FXCollections.observableArrayList();
        for (KLinePieceVO temp : tempList) {
            dayList.add(temp);
        }
        return dayList;
    }


    public void drawChat(){
        getData();
        this.Chart = KLineChart.createChart(this.dataList);
    }

    public KLineChart getChart(){
        return this.Chart;
    }
}
