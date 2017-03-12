package gui.ChartController;

import blservice.exception.DateIndexException;
import blservice.singlestock.MALineService;
import blservice.singlestock.VolService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import vo.MAPieceVO;
import vo.StockVolVO;

import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by wshwbluebird on 2017/3/11.
 */
public class MAChartController implements Initializable {
    /**
     * 动态存储图表
     */
    public MALineChart Chart;

    /**
     * 动态存储传过来的 list
     */
    public ObservableList<MAPieceVO> dataList;

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
    private MALineService maLineService;




    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * 隐藏的初始化方法
     */
    public  MAChartController(){
        stockCode = "code";
        startDate = LocalDate.of(2014,9,1);
        endDate =   LocalDate.of(2014,9,20);
        dataList = FXCollections.observableArrayList();
        dataList.clear();
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


    /**
     * 添加bl服务
     * @param maLineService
     */
    public void setMaLineService(MALineService maLineService) {
        this.maLineService = maLineService;
    }


    /**
     * 根据已经存储的值获取数据
     */
    private void getData(){
        if(maLineService == null){
            System.err.println("没有MALineService的实现传入");
            return ;
        }
        dataList.clear();


        List<MAPieceVO> dayList = new ArrayList<MAPieceVO>();
        try {
            dayList = maLineService.getMAInfo(stockCode,startDate,endDate);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //TODO delete

        System.out.println(dayList==null);
        dataList = getObeservableList(dayList);


    }


    /**
     * 将传过来的list类型进行转化
     * @param tempList
     * @return
     */
    private ObservableList<MAPieceVO> getObeservableList( List<MAPieceVO> tempList){
        ObservableList<MAPieceVO> dayList=  FXCollections.observableArrayList();
        for (MAPieceVO temp : tempList) {
            dayList.add(temp);
        }
        return dayList;
    }

    /**
     * 画出图形
     */
    public void drawChat(){
        getData();
        this.Chart = MALineChart.createChart(this.dataList);
    }

    public MALineChart getChart(){
        return this.Chart;
    }
}

