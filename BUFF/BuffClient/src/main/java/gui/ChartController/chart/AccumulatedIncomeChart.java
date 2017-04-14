package gui.ChartController.chart;

import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import vo.DailyClosingPriceVO;
import vo.DayRatePieceVO;

/*
 * Created by Accident on 2017/4/14.
 */


public class AccumulatedIncomeChart extends LineChart<String, Number> {
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
    public AccumulatedIncomeChart(Axis<String> stringAxis, Axis<Number> numberAxis) {
        super(stringAxis, numberAxis);
        setAnimated(false);
        stringAxis.setAnimated(false);
        numberAxis.setAnimated(false);
    }

    /**
     * 根据传过来的数据建立一张累计收益比较图
     * @param strategyDayRatePieceVOs 策略收益率累积
     * @param baseDayRatePieceVOs 基准收益率累积
     * @return ClosePriceChart
     */
    public static AccumulatedIncomeChart createChart(ObservableList<DayRatePieceVO> strategyDayRatePieceVOs,
                                               ObservableList<DayRatePieceVO> baseDayRatePieceVOs){
        //X轴
        final CategoryAxis xAxis = new CategoryAxis ();
        //Y轴
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);
        //TODO 将其设为百分比格式

        final AccumulatedIncomeChart accumulatedIncomeChart = new AccumulatedIncomeChart(xAxis, yAxis);

        xAxis.setLabel("日期");
        yAxis.setLabel("百分比");

        // 加载传过来的数据序列
        XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();

        XYChart.Series strategySeris = accumulatedIncomeChart.getSeries("策略", strategyDayRatePieceVOs);
        XYChart.Series baseSeries = accumulatedIncomeChart.getSeries("基准", baseDayRatePieceVOs);

        accumulatedIncomeChart.getData().addAll(strategySeris, baseSeries);
        accumulatedIncomeChart.setCreateSymbols(false);

        return accumulatedIncomeChart;
    }

    /**
     * 获取图像最低点
     * @param dayRatePieceVOs
     * @return 返回股票收盘价最低值的最小值
     */
    private double getMin(ObservableList<DayRatePieceVO> dayRatePieceVOs) {
        double min = Double.MAX_VALUE;
        for (DayRatePieceVO vo : dayRatePieceVOs) {
            double currentPrice =  vo.profitRate;
            if (currentPrice < min) {
                min = currentPrice;
            }
        }
        return min;
    }

    /**
     * 获取图像最高点
     * @param dayRatePieceVOs
     * @return  返回股票信息的最高值的最大值
     */
    private static double getMax(ObservableList<DayRatePieceVO> dayRatePieceVOs) {
        double max = 0;
        for (DayRatePieceVO vo : dayRatePieceVOs) {
            double currentPrice =  vo.profitRate;
            if (currentPrice > max) {
                max = currentPrice;
            }
        }
        return max;
    }

    private XYChart.Series getSeries(String name , ObservableList<DayRatePieceVO> dayRatePieceVOs ){
        XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();
        series.setName(name);

        for (DayRatePieceVO vo : dayRatePieceVOs) {
            series.getData().add(new Data<String, Number>(vo.date.toString(), vo.profitRate));
        }

        return series;
    }
}
