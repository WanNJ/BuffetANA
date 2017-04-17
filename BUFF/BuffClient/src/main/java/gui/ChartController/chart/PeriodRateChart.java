package gui.ChartController.chart;

import javafx.beans.NamedArg;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import vo.BetterPieceVO;
import vo.DayRatePieceVO;

/**
 * Created by wshwbluebird on 2017/4/16.
 */
public class PeriodRateChart extends AreaChart<Number ,Number> {
    /**
     * 图形界面
     * @param numberAxis
     * @param numberAxis2
     */
    private PeriodRateChart(@NamedArg("xAxis") Axis<Number> numberAxis, @NamedArg("yAxis") Axis<Number> numberAxis2) {
        super(numberAxis, numberAxis2);
    }

    /**
     * 根据传过来的数据作图
     * @return
     */

    public static PeriodRateChart createChart(ObservableList<BetterPieceVO> betterPieceVOObservableList, String xName , String yName){
        //X轴
        final NumberAxis xAxis = new NumberAxis();
        //Y轴
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);
        //TODO 将其设为百分比格式
        
        final PeriodRateChart PeriodRateChart = new PeriodRateChart(xAxis, yAxis);



        xAxis.setLabel(xName);
        yAxis.setLabel(yName);

        // 加载传过来的数据序列
        XYChart.Series series = PeriodRateChart.getSeries(betterPieceVOObservableList);
        series.setName(yName);

        PeriodRateChart.getData().addAll(series);

        return PeriodRateChart;
    }

    /**
     * copy from zjy
     * @param betterPieceVOObservableList
     * @return
     */
    private XYChart.Series getSeries(ObservableList<BetterPieceVO> betterPieceVOObservableList ){
        XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();

        for (BetterPieceVO vo : betterPieceVOObservableList) {
            series.getData().add(new Data<Number, Number>(vo.days, vo.rate));
        }

        return series;
    }


}
