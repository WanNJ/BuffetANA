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
import javafx.util.Duration;
import vo.StockVolVO;
import vo.VolExtraVO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/3/8.
 */

/**
 * 交易量图
 */
public class ThemometerVolBarChart extends XYChart<String,Number> {


    private static final int  Height=800;
    private static final int  Width=1000;


    private static int length;
    /**
     * Constructs a XYChart given the two axes. The initial content for the chart
     * plot background and plot area that includes vertical and horizontal grid
     * lines and fills, are added.
     *
     * @param stringAxis X Axis for this XY chart
     * @param numberAxis Y Axis for this XY chart
     */
    public ThemometerVolBarChart(Axis<String> stringAxis, Axis<Number> numberAxis) {
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
    public ThemometerVolBarChart(Axis<String> xAxis, Axis<Number> yAxis, ObservableList<Series<String, Number>> data) {
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
        Node volbar = createVolbar(getData().indexOf(series), item, itemIndex);
        if (shouldAnimate()) {
            volbar.setOpacity(0);// 设置不透明
            getPlotChildren().add(volbar);
            // fade in new candle
            FadeTransition ft = new FadeTransition(Duration.millis(500), volbar);
            ft.setToValue(1);
            ft.play();
        } else {
            getPlotChildren().add(volbar);
        }
        // always draw average line on top
        if (series.getNode() != null) {
            series.getNode().toFront();
        }
    }

    @Override
    protected void dataItemRemoved(Data<String, Number> item, Series<String, Number> series) {
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
            Node volbar = createVolbar(seriesIndex, item, j);
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
    }

    @Override
    protected void seriesRemoved(Series<String, Number> series) {
        for (Data<String, Number> d : series.getData()) {
            final Node volBar = d.getNode();
            if (shouldAnimate()) {
                // fade out old candle
                FadeTransition ft = new FadeTransition(Duration.millis(500), volBar);
                ft.setToValue(0);
                ft.setOnFinished(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent actionEvent) {
                        getPlotChildren().remove(volBar);
                    }
                });
                ft.play();
            } else {
                getPlotChildren().remove(volBar);
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


            //迭代  获取每一个要画的node
            while (iter.hasNext()) {
                Data<String, Number> item = iter.next();

                // 根据x的值得到绘图中的像素点横坐标
                double x = getXAxis().getDisplayPosition(getCurrentDisplayedXValue(item));
                // 根据y的值得到绘图中的像素点纵坐标
                double y = getYAxis().getDisplayPosition(getCurrentDisplayedYValue(item));

                Node itemNode = item.getNode();
                VolExtraVO extra = (VolExtraVO) item.getExtraValue();


                //  实体化 自定义的组件图像
                if (itemNode instanceof VolBar && extra != null) {
                     //System.out.println("iam a bar");
                    VolBar volBar = (VolBar) itemNode;

                    //System.out.println("extra:  "+extra.volume);
                    double high = getYAxis().getDisplayPosition(0);
                    double low = getYAxis().getDisplayPosition(0);

                    // calculate candle width
                    double candleWidth = -1;
                    double width = getXAxis().getWidth();

                    if(length !=0 ){
                        candleWidth= width/(length*1.1)>20? 20: width/(length*1.1);
                    }

                    // update volBar
                    volBar.update(high - y, candleWidth, extra.openAboveClose);
                    volBar.updateTooltip(extra.date,extra.volume,extra.changeValue,extra.changeRate);
                    // position the volBar
                    volBar.setLayoutX(x);
                    volBar.setLayoutY(y);
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
     * 用于直接创建vol图的外部方法
     * @param stockVolVOs
     * @return KLineChart  可直接在panel上加载的node
     */
    public static ThemometerVolBarChart createChart(ObservableList<StockVolVO> stockVolVOs){

        length = stockVolVOs.size();
        System.out.println("List of Observe:  "+stockVolVOs.size());
        //获取柱状图的最高点
        double max=getMax(stockVolVOs);
        //获取柱状图的最低点
        double min =getMin(stockVolVOs);


        //  参考学长的代码 计算间距
        double gap=(max-min)/10;
        double HpixelPerValue=20;
        //X轴
        final CategoryAxis xAxis = new CategoryAxis ();
        //Y轴
        final NumberAxis yAxis = new NumberAxis(min-gap,max+gap*2,gap);
        // 初始化K线图
        final ThemometerVolBarChart volBarChart= new ThemometerVolBarChart(xAxis,yAxis);

        xAxis.setLabel("Day");
        yAxis.setLabel("VOL");

        // 加载传过来的数据序列
        Series<String,Number> series = new Series<String,Number>();

        //TODO 未来更改为lambda表达式
        for (int i=0; i< stockVolVOs.size(); i++) {
            StockVolVO vo = stockVolVOs.get(i);

            //计算额外附加值
            VolExtraVO volExtraVO =  new VolExtraVO();
            volExtraVO.date = vo.date;

            if(i!=0){
                StockVolVO last =  stockVolVOs.get(i-1);
                volExtraVO.changeValue = vo.vol - last.vol;
                volExtraVO.openAboveClose = vo.openAboveClose;
                volExtraVO.volume = vo.vol;


                //为了保险起见还是检验一下吧!!!!
                //System.out.println("last:  "+last.vol);
                if(last.vol!=0){
                    volExtraVO.changeRate =  (double)volExtraVO.changeValue*100/last.vol;
                }
            }


            //TODO  日期的字符串化
            //参数：日期、成交量
            series.getData().add(   new Data<String,Number>
                    (vo.date.toString(),vo.vol,volExtraVO));
            new Data<String,Number>();
        }
        ObservableList<Series<String,Number>> data = volBarChart.getData();


        //默认不会再一次
        //TODO 迭代二可能需要修改， 现在不确定
        if (data == null) {

            data = FXCollections.observableArrayList(series);
            volBarChart.setData(data);
        } else {
            volBarChart.getData().add(series);
        }
        volBarChart.setData(data);

        //仿照  确定显示界面的大小
        double curWidth = HpixelPerValue*stockVolVOs .size();
        System.out.println("Bar:   "+curWidth);

        if(curWidth<Width){
            curWidth=Width;
        }
        System.out.println("Bar:   "+curWidth);
        volBarChart.setPrefSize(curWidth,Height*0.95);


        return volBarChart;
    }


    /**
     * 获取所有数据的 最大值
     * @param observableList
     * @return double
     */
    private static double getMax(ObservableList<StockVolVO>  observableList){
        double max = 0;
        for (StockVolVO temp : observableList) {
            if (temp.vol > max) {
                max = temp.vol;
            }
        }
        return max;
    }


    /**
     * 获取所有数据的 最小值
     * @param observableList
     * @return double
     */
    private static double getMin(ObservableList<StockVolVO>  observableList){
        double min = 100;
        for (StockVolVO temp : observableList) {
            if (temp.vol < min) {
                min = temp.vol;
            }
        }
        return min;
    }


    private Node createVolbar(int seriesIndex, final Data item, int itemIndex) {
        Node volBar = item.getNode();
        // check if candle has already been created
        if (volBar instanceof VolBar) {
            ((VolBar) volBar).setSeriesAndDataStyleClasses("series" + seriesIndex, "data" + itemIndex);
        } else {
            volBar = new VolBar();
            item.setNode(volBar);
        }
        return volBar;
    }
}
