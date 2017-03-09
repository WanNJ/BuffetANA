package gui.ChartController;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import vo.KLineExtraVO;
import vo.KLinePieceVO;
import vo.MAPieceVO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/3/7.
 */


/**
 * K 线图
 */
public class MALineChart extends XYChart<String, Number> {

    //TODO  这里参照学长代码进行设计   以后要修改为自己的
    private static final int  Height=600;
    private static final int  Width=550;

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
     *
     * @param series
     * @param itemIndex
     * @param item
     */
    @Override
    protected void dataItemAdded(Series<String, Number> series, int itemIndex, Data<String, Number> item) {

        // always draw average line on top
        if (series.getNode() != null) {
            series.getNode().toFront();
        }
    }

    @Override
    protected void dataItemRemoved(Data<String, Number> item, Series<String, Number> series) {

    }

    @Override
    protected void dataItemChanged(Data<String, Number> item) {
    }

    @Override
    protected void seriesAdded(Series<String, Number> series, int seriesIndex) {

        // create series path
        Path MA5 = new Path();
        MA5.getStyleClass().setAll("MA5-line", "series" + seriesIndex);
        series.setNode(MA5);
        getPlotChildren().add(MA5);
    }

    /**
     * 系统自动调用移除所有序列数据
     * @param series
     */
    @Override
    protected void seriesRemoved(Series<String, Number> series) {

    }

    /**
     *更新轴宽调整数据
     */
    @Override
    protected void updateAxisRange() {
        final Axis<String> xa = getXAxis();
        final Axis<Number> ya = getYAxis();
        List<String> xData = null;
        List<Number> yData = null;
        if (xa.isAutoRanging()) {
            xData = new ArrayList<String>();
        }
        if (ya.isAutoRanging()) {
            yData = new ArrayList<Number>();
        }
        if (xData != null || yData != null) {
            for (Series<String, Number> series : getData()) {
                for (Data<String, Number> data : series.getData()) {
                    if (xData != null) {
                        xData.add(data.getXValue());
                    }
                    if (yData != null) {
                        MAPieceVO extras = (MAPieceVO) data.getExtraValue();
                        if (extras != null) {
                            yData.add(extras.findMax());
                            yData.add(extras.findMin());
                        } else {
                            yData.add(data.getYValue());
                        }
                    }
                }
            }
            if (xData != null) {
                xa.invalidateRange(xData);
            }
            if (yData != null) {
                ya.invalidateRange(yData);
            }
        }
    }



    /**
     * 默认修改图像集成的方法
     */
    @Override
    protected void layoutPlotChildren() {
        // 如果没有数据  直接返回
        if (getData() == null) {
            return;
        }
        // 根据序列数据的数量 在图上画点

        for (int seriesIndex = 0; seriesIndex < getData().size(); seriesIndex++) {
            Series<String, Number> series = getData().get(seriesIndex);
            Iterator<Data<String, Number>> iter = getDisplayedDataIterator(series);

            // 作折线图
            Path MA5 = null;
            if (series.getNode() instanceof Path) {
                // System.out.println("clear path");
                MA5 = (Path) series.getNode();
                MA5.getElements().clear();
            }

            //迭代  获取每一个要画的node
            while (iter.hasNext()) {
                Data<String, Number> item = iter.next();

                // 根据x的值得到绘图中的像素点横坐标
                double x = getXAxis().getDisplayPosition(getCurrentDisplayedXValue(item));
                // 根据y的值得到绘图中的像素点纵坐标
                double y = getYAxis().getDisplayPosition(getCurrentDisplayedYValue(item));

                Node itemNode = item.getNode();
                MAPieceVO extra = (MAPieceVO) item.getExtraValue();

                // 平均值的折线的点在这里添加
                if (MA5 != null) {
                    if (MA5.getElements().isEmpty()) {
                        MA5.getElements().add(new MoveTo(x, getYAxis().getDisplayPosition(extra.MA5)));
                    } else {
                        MA5.getElements().add(new LineTo(x, getYAxis().getDisplayPosition(extra.MA5)));
                    }
                }
            }
        }
    }

    /**
     * 根据传过来的数据建立一张MA 线图
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

        //TODO 未来更改为lambda表达式
        for (int i=0; i< maPieceVOs.size(); i++) {
            MAPieceVO vo = maPieceVOs.get(i);
            //参数：日期、开盘价
            //TODO  日期的字符串化
            series.getData().add(   new XYChart.Data<String,Number>
                    (vo.date.toString(),vo.MA5,
                            new MAPieceVO(vo)));
            new XYChart.Data<String,Number>();
        }
        ObservableList<XYChart.Series<String,Number>> data = maLineChart.getData();


        //默认不会指甲再一次
        //TODO 迭代二可能需要修改， 现在不确定
        if (data == null) {

            data = FXCollections.observableArrayList(series);
            maLineChart.setData(data);
        } else {
            maLineChart.getData().add(series);
        }
        maLineChart.setData(data);

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

    /**
     * 创建一个新的数据的节点
     * @param seriesIndex
     * @param item
     * @param itemIndex
     * @return Node
     */
    private Node createCandle(int seriesIndex, final Data item, int itemIndex) {
        Node candle = item.getNode();
        // check if candle has already been created
        if (candle instanceof Candle) {
            ((Candle) candle).setSeriesAndDataStyleClasses("series" + seriesIndex, "data" + itemIndex);
        } else {
            candle = new Candle("series" + seriesIndex, "data" + itemIndex);
            item.setNode(candle);
        }
        return candle;
    }





}
