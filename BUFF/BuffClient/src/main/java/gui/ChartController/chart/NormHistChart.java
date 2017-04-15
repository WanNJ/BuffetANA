package gui.ChartController.chart;

import gui.ChartController.graphic.NormBar;
import gui.ChartController.graphic.VolBar;
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
import util.RangeF;
import vo.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/4/12.
 */

/**
 * 价格统计的直方图
 */
public class NormHistChart extends XYChart<String,Number> {


    private static final int  Height=800;
    private static final int  Width=1000;


    private static int length;

    private double candleWith;
    /**
     * Constructs a XYChart given the two axes. The initial content for the chart
     * plot background and plot area that includes vertical and horizontal grid
     * lines and fills, are added.
     *
     * @param stringAxis X Axis for this XY chart
     * @param numberAxis Y Axis for this XY chart
     */
    public NormHistChart(Axis<String> stringAxis, Axis<Number> numberAxis) {
        super(stringAxis, numberAxis);
        setAnimated(false);
        stringAxis.setAnimated(false);
        numberAxis.setAnimated(false);
    }

    /**
     * 通过已经的序列化数据初始化 交易量图
     * @param xAxis
     * @param yAxis
     * @param data
     */
    public NormHistChart(Axis<String> xAxis, Axis<Number> yAxis, ObservableList<Series<String, Number>> data) {
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
        Node normBar = createNormbar(getData().indexOf(series), item, itemIndex);
        if (shouldAnimate()) {
            normBar.setOpacity(0);// 设置不透明
            getPlotChildren().add(normBar);
            // fade in new candle
            FadeTransition ft = new FadeTransition(Duration.millis(500), normBar);
            ft.setToValue(1);
            ft.play();
        } else {
            getPlotChildren().add(normBar);
        }
        // always draw average line on top
        if (series.getNode() != null) {
            series.getNode().toFront();
        }
    }

    @Override
    protected void dataItemRemoved(Data<String, Number> item, Series<String, Number> series) {
        final Node normBar = item.getNode();
        if (shouldAnimate()) {
            // fade out old candle
            FadeTransition ft = new FadeTransition(Duration.millis(500), normBar);
            ft.setToValue(0);
            ft.setOnFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent actionEvent) {
                    getPlotChildren().remove(normBar);
                }
            });
            ft.play();
        } else {
            getPlotChildren().remove(normBar);
        }
    }

    @Override
    protected void dataItemChanged(Data<String, Number> item) {
        final Node volbar = item.getNode();
        if (shouldAnimate()) {
            // fade out old candle
            FadeTransition ft = new FadeTransition(Duration.millis(500), volbar);
            ft.setToValue(0);
            ft.setOnFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent actionEvent) {
                    getPlotChildren().remove(volbar);
                }
            });
            ft.play();
        } else {
            getPlotChildren().remove(volbar);
        }
    }

    @Override
    protected void seriesAdded(Series<String, Number> series, int seriesIndex) {
        for (int j = 0; j < series.getData().size(); j++) {
            Data item = series.getData().get(j);
            Node volbar = createNormbar(seriesIndex, item, j);
            if (shouldAnimate()) {
                volbar.setOpacity(0);
                getPlotChildren().add(volbar);
                // fade in new candle
                FadeTransition ft = new FadeTransition(Duration.millis(500), volbar);
                ft.setToValue(1);
                ft.play();
            } else {
                getPlotChildren().add(volbar);
            }
        }

        Path seriesPath = new Path();
        seriesPath.getStyleClass().setAll("candlestick-average-line", "series" + seriesIndex);
        series.setNode(seriesPath);
        getPlotChildren().add(seriesPath);
    }

    @Override
    protected void seriesRemoved(Series<String, Number> series) {
        for (XYChart.Data<String, Number> d : series.getData()) {
            final Node normBar = d.getNode();
            if (shouldAnimate()) {
                // fade out old candle
                FadeTransition ft = new FadeTransition(Duration.millis(500), normBar);
                ft.setToValue(0);
                ft.setOnFinished(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent actionEvent) {
                        getPlotChildren().remove(normBar);
                    }
                });
                ft.play();
            } else {
                getPlotChildren().remove(normBar);
            }
        }
    }

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


            // 做拟合的正太分布图
            Path seriesPath = null;
            if (series.getNode() instanceof Path) {

                seriesPath = (Path) series.getNode();
                seriesPath.getElements().clear();
            }

            //迭代  获取每一个要画的node
            while (iter.hasNext()) {
                Data<String, Number> item = iter.next();

                // 根据x的值得到绘图中的像素点横坐标
                double x = getXAxis().getDisplayPosition(getCurrentDisplayedXValue(item));
                // 根据y的值得到绘图中的像素点纵坐标
                double y = getYAxis().getDisplayPosition(getCurrentDisplayedYValue(item));

                Node itemNode = item.getNode();
                ExtraNormHistVO extra = (ExtraNormHistVO) item.getExtraValue();


                //  实体化 自定义的组件图像
                if (itemNode instanceof NormBar && extra != null) {
                    //System.out.println("iam a bar");
                    NormBar normBar = (NormBar) itemNode;

                    //System.out.println("extra:  "+extra.volume);
                    double high = getYAxis().getDisplayPosition(0);
                    double low = getYAxis().getDisplayPosition(0);

                    // calculate candle width
                    double candleWidth = -1;
                    double width = getXAxis().getWidth();

                    if(length !=0 ){
                        candleWidth= width/(length*1.1)>20? 20: width/(length*1.1);
                    }
                    this.candleWith = candleWidth;

                    // update volBar
                    normBar.update(high - y, candleWidth);
                    // volBar.updateTooltip(extra.date,extra.volume,extra.changeValue,extra.changeRate);
                    // position the volBar
                    normBar.setLayoutX(x);
                    normBar.setLayoutY(y);
                }

                // 拟合正太分布折线的点在这里添加
                if (seriesPath != null) {
                    if (seriesPath.getElements().isEmpty()) {
                        seriesPath.getElements().add(new MoveTo(x, getYAxis().getDisplayPosition(extra.similar)));
                    } else {
                        seriesPath.getElements().add(new LineTo(x, getYAxis().getDisplayPosition(extra.similar)));
                    }
                }

            }
        }
    }

    /**
     *更新轴宽调整数据
     */
    @Override
    protected void updateAxisRange(){
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
                        VolExtraVO extras = (VolExtraVO) data.getExtraValue();
                        if (extras != null) {
                            yData.add(extras.volume);
                            yData.add(0);
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
     *
     * @param rangeFObservableList
     * @param guassLineVOObservableList
     * @return
     */
    public static NormHistChart createChart(ObservableList<RangeF> rangeFObservableList
            , ObservableList<GuassLineVO> guassLineVOObservableList){

        length = rangeFObservableList.size();
        System.out.println("List of Observe:  "+rangeFObservableList.size());
        //获取柱状图的最高点
        double max=getMax(rangeFObservableList);


        //获取柱状图的最低点
        double min =getMin(rangeFObservableList);


        if(guassLineVOObservableList != null) {
            max = Math.max(max, getMax2(guassLineVOObservableList));
            min = Math.min(min,getMin2(guassLineVOObservableList));
        }


        //  参考学长的代码 计算间距
        double gap=0;
        double HpixelPerValue=20;
        //X轴
        final CategoryAxis xAxis = new CategoryAxis ();
        //Y轴
        final NumberAxis yAxis = new NumberAxis(min-gap,max+gap*2,gap);
        // 初始化K线图
        final NormHistChart normHistChart= new NormHistChart(xAxis,yAxis);

        xAxis.setLabel("Price");
        yAxis.setLabel("Times");

        // 加载传过来的数据序列
        XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();

        //TODO 未来更改为lambda表达式
        for (int i=0; i< rangeFObservableList.size(); i++) {
            RangeF rangeF = rangeFObservableList.get(i);
            GuassLineVO guassLineVO = guassLineVOObservableList.get(i);


            //TODO  日期的字符串化
            //参数：日期、成交量
            series.getData().add(  new XYChart.Data<String,Number>
                    (String.valueOf(guassLineVO.middle),rangeF.cnt,
                            new ExtraNormHistVO(rangeF.big,rangeF.small,rangeF.cnt,guassLineVO.value)));
            new XYChart.Data<String,Number>();
        }
        ObservableList<XYChart.Series<String,Number>> data = normHistChart.getData();


        //默认不会再一次

        if (data == null) {

            data = FXCollections.observableArrayList(series);
            normHistChart.setData(data);
        } else {
            normHistChart.getData().add(series);
        }
        normHistChart.setData(data);

        //仿照  确定显示界面的大小
        //TODO  要把间距宽度调成0
        double curWidth = HpixelPerValue*rangeFObservableList .size();
        System.out.println("Bar:   "+curWidth);

        if(curWidth<Width){
            curWidth=Width;
        }
        System.out.println("Bar:   "+curWidth);
        //normHistChart.setPrefSize(curWidth,Height*0.95);


        return normHistChart;
    }


    /**
     * 获取所有数据的 最大值
     * @param observableList
     * @return double
     */
    private static double getMax(ObservableList<RangeF>  observableList){
        double max = 0;
        for (RangeF temp : observableList) {
            if (temp.cnt > max) {
                max = temp.cnt;
            }
        }
        return max;
    }


    /**
     * 获取所有数据的 最小值
     * @param observableList
     * @return double
     */
    private static double getMin(ObservableList<RangeF>  observableList){
        double min = 100;
        for (RangeF temp : observableList) {
            if (temp.cnt < min) {
                min = temp.cnt;
            }
        }
        return min;
    }


    /**
     * 获取所有数据的 最大值
     * @param observableList
     * @return double
     */
    private static double getMax2(ObservableList<GuassLineVO> observableList ){
        double max = 0;
        for (GuassLineVO temp : observableList) {
            if (temp.value > max) {
                max = temp.value;
            }
        }
        return max;
    }


    /**
     * 获取所有数据的 最小值
     * @param observableList
     * @return double
     */
    private static double getMin2(ObservableList<GuassLineVO>  observableList){
        double min = 100;
        for (GuassLineVO temp : observableList) {
            if (temp.value < min) {
                min = temp.value;
            }
        }
        return min;
    }


    private Node createNormbar(int seriesIndex, final Data item, int itemIndex) {
        Node normBar = item.getNode();
        // check if candle has already been created
        if (normBar instanceof VolBar) {
            ((NormBar) normBar).setSeriesAndDataStyleClasses("series" + seriesIndex, "data" + itemIndex);
        } else {
            normBar = new NormBar();
            item.setNode(normBar);
        }
        return normBar;
    }

    public double getCandleWith(){
        return this.candleWith;
    }
}
