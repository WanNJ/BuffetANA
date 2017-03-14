package gui.ChartController;

import javafx.collections.ObservableList;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import vo.DailyLogReturnVO;
import vo.DailyLogReturnVO;

/**
 * Created by Accident on 2017/3/14.
 */
public class LRChart extends LineChart<String, Number> {
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
    public LRChart(Axis<String> stringAxis, Axis<Number> numberAxis) {
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
    public LRChart(Axis<String> xAxis, Axis<Number> yAxis, ObservableList<Series<String, Number>> data) {
        this(xAxis, yAxis);
        setData(data);
    }

    /**
     * 根据传过来的数据建立一张每日对数收益率图
     * @param mainDailyLogReturnVOS 主股
     * @param deputyDailyLogReturnVOS 副股
     * @return ClosePriceChart
     */
    public static LRChart createChart(ObservableList<DailyLogReturnVO> mainDailyLogReturnVOS,
                               ObservableList<DailyLogReturnVO> deputyDailyLogReturnVOS){

//        mainDailyLogReturnVOS.forEach(t-> System.out.println(t.logReturnIndex));
//        deputyDailyLogReturnVOS.forEach(t-> System.out.println(t.logReturnIndex));

        //X轴
        final CategoryAxis xAxis = new CategoryAxis ();
        //Y轴
        final NumberAxis yAxis = new NumberAxis();
        //
        final LRChart closePriceChart= new LRChart(xAxis,yAxis);

        xAxis.setLabel("日期");
        yAxis.setLabel("对数收益率");

        // 加载传过来的数据序列
        Series<String,Number> series = new Series<String,Number>();

        Series mainSeries = closePriceChart.getSeries("主股对数收益率", mainDailyLogReturnVOS);
        Series deputySeries = closePriceChart.getSeries("副股对数收益率", deputyDailyLogReturnVOS);

        closePriceChart.getData().addAll(mainSeries, deputySeries);
        closePriceChart.setCreateSymbols(false);

        return closePriceChart;
    }

    /**
     * 获取图像最低点
     * @param dailyLogReturnVOS
     * @return 返回股票对数收益率最低值的最小值
     */
    private double getMin(ObservableList<DailyLogReturnVO>  dailyLogReturnVOS) {
        double min = Double.MAX_VALUE;
        for (DailyLogReturnVO vo : dailyLogReturnVOS) {
            double currentPrice =  vo.logReturnIndex;
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
    private static double getMax(ObservableList<DailyLogReturnVO>  dailyLogReturnVOS) {
        double max = 0;
        for (DailyLogReturnVO vo : dailyLogReturnVOS) {
            double currentPrice =  vo.logReturnIndex;
            if (currentPrice > max) {
                max = currentPrice;
            }
        }
        return max;
    }

    private Series getSeries(String name , ObservableList<DailyLogReturnVO> dailyLogReturnVOS ){
        Series<String,Number> series = new Series<String,Number>();
        series.setName(name);

        for (int i=0; i< dailyLogReturnVOS.size(); i++) {
            DailyLogReturnVO vo = dailyLogReturnVOS.get(i);
            series.getData().add(new Data<String,Number>(vo.date.toString(),vo.logReturnIndex));
        }

        return series;
    }
}
