package gui.ChartController.chart;

import javafx.beans.NamedArg;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import vo.KLinePieceVO;
import vo.PriceIncomeVO;

import java.util.stream.Collectors;

/**
 * Created by wshwbluebird on 2017/4/15.
 */
public class DotChart extends ScatterChart<String , Number> {

    private static final int  Height=800;
    private static final int  Width=1000;

    public DotChart(@NamedArg("xAxis") Axis<String> stringAxis, @NamedArg("yAxis") Axis<Number> numberAxis) {
        super(stringAxis, numberAxis);
    }

    /**
     * 返回散点图
     * @param priceIncomeVOs
     * @return
     */
    public static DotChart createChart(ObservableList<PriceIncomeVO> priceIncomeVOs){


        double max=getMax(priceIncomeVOs);
        //获取图像最小值
        double min =getMin(priceIncomeVOs);
        //  参考学长的代码 计算间距
        double gap=(max-min)/10;
        double HpixelPerValue=20;


        final CategoryAxis xAxis = new CategoryAxis ();
        //Y轴
        final NumberAxis yAxis = new NumberAxis(min-gap,max+gap*2,gap);
        // 初始化K线图
        final  DotChart dotChart = new DotChart(xAxis,yAxis);

        xAxis.setLabel("Price");
        yAxis.setLabel("IncomeRate");

        XYChart.Series series1 = new XYChart.Series();

        series1.getData().addAll(priceIncomeVOs.stream()
                        .map(t->new XYChart.Data<>
                                (String.valueOf(t.price).length()>5
                                        ?String.valueOf(t.price).substring(0,5):String.valueOf(t.price),t.incomeRate))
                        .collect(Collectors.toList()));

        dotChart.getData().addAll(series1);

        double curWidth = HpixelPerValue*priceIncomeVOs .size();
        if(curWidth<Width){
            curWidth=Width;
        }
        //dotChart.setPrefSize(curWidth,Height*0.95);


        return dotChart;


    }



    /**
     * 获取图像最低点
     * @param priceIncomeVOs
     * @return 返回股票信息最低值的最小值
     */
    private static double getMin(ObservableList<PriceIncomeVO>  priceIncomeVOs) {
        double min = 100;
        for (PriceIncomeVO temp : priceIncomeVOs) {
            if (temp.incomeRate < min) {
                min = temp.incomeRate;
            }
        }
        return min<0? 0:min;
    }

    /**
     * 获取图像最高点
     * @param priceIncomeVOs
     * @return  返回股票信息的最高值的最大值
     */
    private static double getMax(ObservableList<PriceIncomeVO>  priceIncomeVOs) {
        double max = -100;
        for (PriceIncomeVO temp : priceIncomeVOs) {
            if (temp.incomeRate > max) {
                max = temp.incomeRate;
            }
        }
        return max;
    }
}
