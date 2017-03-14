package gui.ChartController;

import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import vo.DailyClosingPriceVO;

/**
 * Created by Accident on 2017/3/14.
 */
public class ClosePriceChart extends LineChart<String, Number> {
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
    public ClosePriceChart(Axis<String> stringAxis, Axis<Number> numberAxis) {
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
    public ClosePriceChart(Axis<String> xAxis, Axis<Number> yAxis, ObservableList<XYChart.Series<String, Number>> data) {
        this(xAxis, yAxis);
        setData(data);
    }

    /**
     * 根据传过来的数据建立一张每日收盘价图
     * @param mainDailyClosingPriceVOS 主股
     * @param deputyDailyClosingPriceVOS 副股
     * @return ClosePriceChart
     */
    public static  ClosePriceChart createChart(ObservableList<DailyClosingPriceVO> mainDailyClosingPriceVOS,
                                       ObservableList<DailyClosingPriceVO> deputyDailyClosingPriceVOS){
        //X轴
        final CategoryAxis xAxis = new CategoryAxis ();
        //Y轴
        final NumberAxis yAxis = new NumberAxis();
        //
        final ClosePriceChart closePriceChart= new ClosePriceChart(xAxis,yAxis);

        xAxis.setLabel("日期");
        yAxis.setLabel("收盘价");

        // 加载传过来的数据序列
        XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();

        XYChart.Series mainSeries = closePriceChart.getSeries("主股收盘价", mainDailyClosingPriceVOS);
        XYChart.Series deputySeries = closePriceChart.getSeries("副股收盘价", deputyDailyClosingPriceVOS);

        closePriceChart.getData().addAll(mainSeries, deputySeries);
        closePriceChart.setCreateSymbols(false);

        return closePriceChart;
    }

    /**
     * 获取图像最低点
     * @param dailyClosingPriceVOS
     * @return 返回股票收盘价最低值的最小值
     */
    private double getMin(ObservableList<DailyClosingPriceVO>  dailyClosingPriceVOS) {
        double min = Double.MAX_VALUE;
        for (DailyClosingPriceVO vo : dailyClosingPriceVOS) {
            double currentPrice =  vo.closePrice;
            if (currentPrice < min) {
                min = currentPrice;
            }
        }
        return min;
    }

    /**
     * 获取图像最高点
     * @param
     * @return  返回股票信息的最高值的最大值
     */
    private static double getMax(ObservableList<DailyClosingPriceVO>  dailyClosingPriceVOS) {
        double max = 0;
        for (DailyClosingPriceVO vo : dailyClosingPriceVOS) {
            double currentPrice =  vo.closePrice;
            if (currentPrice > max) {
                max = currentPrice;
            }
        }
        return max;
    }

    private XYChart.Series getSeries(String name , ObservableList<DailyClosingPriceVO> dailyClosingPriceVOS ){
        XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();
        series.setName(name);

        for (int i=0; i< dailyClosingPriceVOS.size(); i++) {
            DailyClosingPriceVO vo = dailyClosingPriceVOS.get(i);
            series.getData().add(new XYChart.Data<String,Number>(vo.date.toString(),vo.closePrice));
        }

        return series;
    }
}
