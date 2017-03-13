package gui.ChartController;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import vo.KLineExtraVO;
import vo.KLinePieceVO;
import vo.MAPieceVO;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/3/7.
 */


/**
 * K 线图
 */
public class MALineChart extends LineChart<String, Number> {

    //TODO  这里参照学长代码进行设计   以后要修改为自己的
    private static final int  Height=800;
    private static final int  Width=1000;

    /**
     * Constructs a XYChart given the two axes. The initial content for the chart
     * plot background and plot area that includes vertical and horizontal grid
     * lines and fills, are added.
     *
     * @param stringAxis X Axis for this XY chart
     * @param numberAxis Y Axis for this XY chart
     */
    public MALineChart(Axis<String> stringAxis, Axis<Number> numberAxis) {
        super(stringAxis, numberAxis);
        setAnimated(false);
        stringAxis.setAnimated(false);
        numberAxis.setAnimated(false);
    }

    /**
     * 直接用序列好的数据初始化
     * @param xAxis
     * @param yAxis
     * @param data
     */
    public MALineChart(Axis<String> xAxis, Axis<Number> yAxis, ObservableList<Series<String, Number>> data) {
        this(xAxis, yAxis);
        setData(data);
    }





    /**
     * 根据传过来的数据建立一张MA图
     * @param maPieceVOs
     * @return KLineChart
     */
    public static MALineChart createChart(ObservableList<MAPieceVO> maPieceVOs){
        System.out.println("List of Observe:  "+maPieceVOs.size());
        //获取图像最大值
        double max=getMax(maPieceVOs);
        //获取图像最小值
        double min =getMin(maPieceVOs);
        //  参考学长的代码 计算间距
        double gap=(max-min)/10;
        double HpixelPerValue=20;
        //X轴
        final CategoryAxis xAxis = new CategoryAxis ();
        //Y轴
        final NumberAxis yAxis = new NumberAxis(min-gap,max+gap*2,gap);
        // 初始化K线图
        final MALineChart maLineChart= new MALineChart(xAxis,yAxis);

        xAxis.setLabel("Day");
        yAxis.setLabel("MA");

        // 加载传过来的数据序列
        XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();

        XYChart.Series MA5 = getSeries("MA5",maPieceVOs);
        XYChart.Series MA10 = getSeries("MA10",maPieceVOs);
        XYChart.Series MA30 = getSeries("MA30",maPieceVOs);
        XYChart.Series MA60 = getSeries("MA60",maPieceVOs);


        maLineChart.getData().addAll(MA5,MA10,MA30,MA60);
        maLineChart.setCreateSymbols(false);

        //System.out.println("datasize:   "+data.size());
        double curWidth = HpixelPerValue*maPieceVOs .size();
        if(curWidth<Width){
            curWidth=Width;
        }
        maLineChart.setPrefSize(curWidth,Height*0.95);
        System.out.println("MA:   "+curWidth);

        return maLineChart;
    }

    /**
     * 获取图像最低点
     * @param maPieceVOs
     * @return 返回股票信息最低值的最小值
     */
    private static double getMin(ObservableList<MAPieceVO>  maPieceVOs) {
        double min = 200;
        for (MAPieceVO temp : maPieceVOs) {
            double tempMin =  temp.findMin();
            if (tempMin < min) {
                min = tempMin;
            }
        }
        return min;
    }

    /**
     * 获取图像最高点
     * @param maPieceVOs
     * @return  返回股票信息的最高值的最大值
     */
    private static double getMax(ObservableList<MAPieceVO>  maPieceVOs) {
        double max = 0;
        for (MAPieceVO temp : maPieceVOs) {
            double tempMax =  temp.findMax();
            if (tempMax > max) {
                max = tempMax;
            }
        }
        return max;
    }


    private static  Series getSeries(String name , ObservableList<MAPieceVO> maPieceVOs ){
        XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();
        series.setName(name);

        for (int i=0; i< maPieceVOs.size(); i++) {
            MAPieceVO vo = maPieceVOs.get(i);
            //参数：日期、开盘价
            //TODO  日期的字符串化

            switch (name){
                case "MA5":  series.getData().add(   new XYChart.Data<String,Number>
                        (vo.date.toString(),vo.MA5));
                        break;
                case "MA10":  series.getData().add(   new XYChart.Data<String,Number>
                        (vo.date.toString(),vo.MA10));
                        break;
                case "MA30":  series.getData().add(   new XYChart.Data<String,Number>
                        (vo.date.toString(),vo.MA30));
                        break;
                case "MA60":  series.getData().add(   new XYChart.Data<String,Number>
                        (vo.date.toString(),vo.MA60));
                        break;
            }

        }

        return series;

    }





}
