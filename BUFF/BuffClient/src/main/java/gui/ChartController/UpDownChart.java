package gui.ChartController;

import javafx.beans.NamedArg;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import vo.LongPeiceVO;
import vo.MAPieceVO;

/**
 * Created by wshwbluebird on 2017/3/13.
 */
public class UpDownChart extends BarChart<String,Number> {

    private static final int  Height=800;
    private static final int  Width=1000;


    private static int length;


    public UpDownChart(@NamedArg("xAxis") Axis<String> stringAxis, @NamedArg("yAxis") Axis<Number> numberAxis) {
        super(stringAxis, numberAxis);
    }


    /**
     * 根据传过来的数据建立一张比较图
     * @param ups
     * @param downs
     * @return UpDownChart
     */
    public static UpDownChart createChart(ObservableList<LongPeiceVO> ups, ObservableList<LongPeiceVO> downs ){
        length = ups.size();
        //获取图像最大值
        double max= Math.max(getMax(ups),getMax(downs));
        //获取图像最小值
        double min = Math.max(getMin(ups),getMin(downs));
        //  参考学长的代码 计算间距
        double gap=(max-min)/10;
        double HpixelPerValue=20;
        //X轴
        final CategoryAxis xAxis = new CategoryAxis ();
        //Y轴
        final NumberAxis yAxis = new NumberAxis(min-gap,max+gap*2,gap);

        final UpDownChart upDownChart= new UpDownChart(xAxis,yAxis);

        xAxis.setLabel("Day");
        yAxis.setLabel("NUM");

        // 加载传过来的数据序列
        XYChart.Series<String,Number> seriesUp = new XYChart.Series<String,Number>();
        seriesUp.setName("上涨股票");
        for (int i=0; i< ups.size(); i++) {
            LongPeiceVO vo = ups.get(i);
            seriesUp.getData().add(   new XYChart.Data<String,Number>
                    (vo.localDate.toString(),vo.amount));
        }
        //seriesUp.getNode().getStyleClass().add("upBar");
        XYChart.Series<String,Number> seriesDown = new XYChart.Series<String,Number>();
        seriesDown.setName("下跌股票");
        for (int i=0; i< downs.size(); i++) {
            LongPeiceVO vo = downs.get(i);
            seriesDown.getData().add(   new XYChart.Data<String,Number>
                    (vo.localDate.toString(),vo.amount));
        }
        //seriesDown.getNode().getStyleClass().add("downBar");



        upDownChart.getData().addAll(seriesUp,seriesDown);

        //System.out.println("datasize:   "+data.size());
        double curWidth = HpixelPerValue*ups.size();
        if(curWidth<Width){
            curWidth=Width;
        }
        upDownChart.setPrefSize(curWidth,Height*0.95);
        System.out.println("MA:   "+curWidth);

        double bargap = 100/length;
        System.out.println("bargap:  "+bargap);
        upDownChart.setBarGap(0);
        //upDownChart.setB
        upDownChart.setCategoryGap(5*bargap);

        return upDownChart;
    }

    /**
     * 获取图像最低点
     * @param longPeiceVOs
     * @return 返回股票信息最低值的最小值
     */
    private static long getMin(ObservableList<LongPeiceVO>  longPeiceVOs) {
//        long min = 10000;
//        for (LongPeiceVO temp : longPeiceVOs) {
//            long tempMin =  temp.amount;
//            if (tempMin < min) {
//                min = tempMin;
//            }
//        }
        return 0;
    }

    /**
     * 获取图像最高点
     * @param longPeiceVOs
     * @return  返回股票信息的最高值的最大值
     */
    private static long getMax(ObservableList<LongPeiceVO>  longPeiceVOs) {
        long max = 0;
        for (LongPeiceVO temp : longPeiceVOs) {
            long tempMax =  temp.amount;
            if (tempMax > max) {
                max = tempMax;
            }
        }
        return max;
    }



}
