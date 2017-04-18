package gui.ChartController.chart;

import gui.ChartController.graphic.Candle;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import vo.KLineExtraVO;
import vo.KLinePieceVO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/3/7.
 */


/**
 * K 线图
 */
public class MarketKLineChart extends KLineChart {

    //TODO  这里参照学长代码进行设计   以后要修改为自己的
    private static final int  Height=800;
    private static final int  Width=1000;

    private static  int length = 0;


    private double candleWith = 0;

    /**
     * Constructs a XYChart given the two axes. The initial content for the chart
     * plot background and plot area that includes vertical and horizontal grid
     * lines and fills, are added.
     *
     * @param stringAxis X Axis for this XY chart
     * @param numberAxis Y Axis for this XY chart
     */
    public MarketKLineChart(Axis<String> stringAxis, Axis<Number> numberAxis) {
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
    public MarketKLineChart(Axis<String> xAxis, Axis<Number> yAxis, ObservableList<Series<String, Number>> data) {
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
        Node candle = createCandle(getData().indexOf(series), item, itemIndex);
        if (shouldAnimate()) {
            candle.setOpacity(0);// 设置不透明
            getPlotChildren().add(candle);
            // fade in new candle
            FadeTransition ft = new FadeTransition(Duration.millis(500), candle);
            ft.setToValue(1);
            ft.play();
        } else {
            getPlotChildren().add(candle);
        }
        // always draw average line on top
        if (series.getNode() != null) {
            series.getNode().toFront();
        }
    }

    @Override
    protected void dataItemRemoved(Data<String, Number> item, Series<String, Number> series) {
        final Node candle = item.getNode();
        if (shouldAnimate()) {
            // fade out old candle
            FadeTransition ft = new FadeTransition(Duration.millis(500), candle);
            ft.setToValue(0);
            ft.setOnFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent actionEvent) {
                    getPlotChildren().remove(candle);
                }
            });
            ft.play();
        } else {
            getPlotChildren().remove(candle);
        }
    }

    @Override
    protected void dataItemChanged(Data<String, Number> item) {
        final Node candle = item.getNode();
        if (shouldAnimate()) {
            // fade out old candle
            FadeTransition ft = new FadeTransition(Duration.millis(500), candle);
            ft.setToValue(0);
            ft.setOnFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent actionEvent) {
                    getPlotChildren().remove(candle);
                }
            });
            ft.play();
        } else {
            getPlotChildren().remove(candle);
        }
    }

    @Override
    protected void seriesAdded(Series<String, Number> series, int seriesIndex) {
        // handle any data already in series
        for (int j = 0; j < series.getData().size(); j++) {
            Data item = series.getData().get(j);
            Node candle = createCandle(seriesIndex, item, j);
            if (shouldAnimate()) {
                candle.setOpacity(0);
                getPlotChildren().add(candle);
                // fade in new candle
                FadeTransition ft = new FadeTransition(Duration.millis(500), candle);
                ft.setToValue(1);
                ft.play();
            } else {
                getPlotChildren().add(candle);
            }
        }
        // create series path
        Path seriesPath = new Path();
        seriesPath.getStyleClass().setAll("candlestick-average-line", "series" + seriesIndex);
        series.setNode(seriesPath);
        getPlotChildren().add(seriesPath);
    }

    /**
     * 系统自动调用移除所有序列数据
     * @param series
     */
    @Override
    protected void seriesRemoved(Series<String, Number> series) {
        // remove all candle nodes
        for (Data<String, Number> d : series.getData()) {
            final Node candle = d.getNode();
            if (shouldAnimate()) {
                // fade out old candle
                FadeTransition ft = new FadeTransition(Duration.millis(500), candle);
                ft.setToValue(0);
                ft.setOnFinished(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent actionEvent) {
                        getPlotChildren().remove(candle);
                    }
                });
                ft.play();
            } else {
                getPlotChildren().remove(candle);
            }
        }
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
                        KLineExtraVO extras = (KLineExtraVO) data.getExtraValue();
                        if (extras != null) {
                            yData.add(extras.getHighPrice());
                            yData.add(extras.getLowPrice());
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
        int xx =  getData().size();

        for (int seriesIndex = 0; seriesIndex < getData().size(); seriesIndex++) {
            Series<String, Number> series = getData().get(seriesIndex);
            Iterator<Data<String, Number>> iter = getDisplayedDataIterator(series);

            // 作折线图
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
                KLineExtraVO extra = (KLineExtraVO) item.getExtraValue();


                //  实体化 自定义的组件图像
                if (itemNode instanceof Candle && extra != null) {

                    Candle candle = (Candle) itemNode;

                    double close = getYAxis().getDisplayPosition(extra.getClosePrice());
                    double open = getYAxis().getDisplayPosition(extra.getOpenPrice());
                    // calculate candle width
                    double candleWidth = -1;
                    double width = getXAxis().getWidth();

                    if(length !=0 ){
                        candleWidth= width/(length*1.1)>20? 20: width/(length*1.1);
                    }

                    this.candleWith = candleWidth;
                    // update candle
                    candle.update(close - y, Math.min(close,open) - y - candleWidth ,Math.max(close,open) - y +candleWidth , candleWidth);
//                    candle.updateTooltip(item.getYValue().doubleValue(), extra.getClosePrice(), extra.getHighPrice(),
//                            extra.getLowPrice());

                    // position the candle
                    candle.setLayoutX(x);
                    candle.setLayoutY(y);

                }

                // 平均值的折线的点在这里添加
                if (seriesPath != null) {
                    if (seriesPath.getElements().isEmpty()) {
                        seriesPath.getElements().add(new MoveTo(x, getYAxis().getDisplayPosition
                                ((extra.openPrice+extra.closePrice)/2)));
                    } else {
                        seriesPath.getElements().add(new LineTo(x, getYAxis().getDisplayPosition
                                ((extra.openPrice+extra.closePrice)/2)));
                    }
                }
            }
        }
    }

    /**
     * 根据传过来的数据建立一张K 线图
     * @param kLinePieceVOs
     * @return KLineChart
     */
    public static MarketKLineChart createChart(ObservableList<KLinePieceVO> kLinePieceVOs){
        length = kLinePieceVOs.size();

        //获取图像最大值
        double max=getMax(kLinePieceVOs);
        //获取图像最小值
        double min =getMin(kLinePieceVOs);
        //  参考学长的代码 计算间距
        double gap=(max-min)/10;
        double HpixelPerValue=20;
        //X轴
        final CategoryAxis xAxis = new CategoryAxis ();
        //Y轴
        final NumberAxis yAxis = new NumberAxis(min-gap,max+gap*2,gap);
        // 初始化K线图
        final MarketKLineChart kLineChart= new MarketKLineChart(xAxis,yAxis);

        xAxis.setLabel("Day");
        yAxis.setLabel("大盘K值");

        // 加载传过来的数据序列
        Series<String,Number> series = new Series<String,Number>();

        //TODO 未来更改为lambda表达式
        for (int i=0; i< kLinePieceVOs.size(); i++) {
            KLinePieceVO vo = kLinePieceVOs.get(i);
            //参数：日期、开盘价
            //TODO  日期的字符串化
            series.getData().add(   new Data<String,Number>
                    (vo.date.toString(),vo.openPrice,
                            new KLineExtraVO(vo.highPrice,vo.lowPrice,vo.openPrice,vo.closePrice,
                                    (vo.lowPrice+vo.highPrice)/2)));
            //new XYChart.Data<String,Number>();
        }
        ObservableList<Series<String,Number>> data = kLineChart.getData();


        //默认不会指甲再一次
        //TODO 迭代二可能需要修改， 现在不确定
        if (data == null) {

            data = FXCollections.observableArrayList(series);
            kLineChart.setData(data);
        } else {
            kLineChart.getData().add(series);
        }
        kLineChart.setData(data);


        double curWidth = HpixelPerValue*kLinePieceVOs .size();
        if(curWidth<Width){
            curWidth=Width;
        }
        kLineChart.setPrefSize(curWidth,Height*0.95);


        return kLineChart;
    }

    /**
     * 获取图像最低点
     * @param kLinePieceVOs
     * @return 返回股票信息最低值的最小值
     */
    private static double getMin(ObservableList<KLinePieceVO>  kLinePieceVOs) {
        double min = 100;
        for (KLinePieceVO temp : kLinePieceVOs) {
            double tt = Math.min(temp.closePrice,temp.openPrice);
            if (tt< min) {
                min = tt;
            }
        }
        return min<0? 0:min;
    }

    /**
     * 获取图像最高点
     * @param kLinePieceVOs
     * @return  返回股票信息的最高值的最大值
     */
    private static double getMax(ObservableList<KLinePieceVO>  kLinePieceVOs) {
        double max = 0;
        for (KLinePieceVO temp : kLinePieceVOs) {
            double tt = Math.max(temp.closePrice,temp.openPrice);
            if (tt > max) {
                max = tt;
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



    public double getCandleWidth(){
        return this.candleWith;
    }
}
