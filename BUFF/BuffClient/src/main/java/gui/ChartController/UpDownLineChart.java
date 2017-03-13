package gui.ChartController;

import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import vo.LongPeiceVO;
import vo.MAPieceVO;

/**
 * Created by wshwbluebird on 2017/3/7.
 */


/**
 * K 线图
 */
public class UpDownLineChart extends LineChart<String, Number> {

    //TODO  这里参照学长代码进行设计   以后要修改为自己的
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
    public UpDownLineChart(Axis<String> stringAxis, Axis<Number> numberAxis) {
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
    public UpDownLineChart(Axis<String> xAxis, Axis<Number> yAxis, ObservableList<Series<String, Number>> data) {
        this(xAxis, yAxis);
        setData(data);
    }


    /**
     * 建立线性
     * @param ups
     * @param downs
     * @return
     */
    public static UpDownLineChart createChart(ObservableList<LongPeiceVO> ups, ObservableList<LongPeiceVO> downs){

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

        final UpDownLineChart upDownLineChart= new UpDownLineChart(xAxis,yAxis);

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

        XYChart.Series<String,Number> seriesDown = new XYChart.Series<String,Number>();
        seriesDown.setName("下跌股票");
        for (int i=0; i< downs.size(); i++) {
            LongPeiceVO vo = downs.get(i);
            seriesDown.getData().add(   new XYChart.Data<String,Number>
                    (vo.localDate.toString(),vo.amount));
        }




        upDownLineChart.getData().addAll(seriesUp,seriesDown);

        //System.out.println("datasize:   "+data.size());
        double curWidth = HpixelPerValue*ups .size();
        if(curWidth<Width){
            curWidth=Width;
        }
        upDownLineChart.setPrefSize(curWidth,Height*0.95);
        System.out.println("MA:   "+curWidth);

        return upDownLineChart;
    }

    /**
     * 获取图像最低点
     * @param longPeiceVOs
     * @return 返回股票信息最低值的最小值
     */
    private static long getMin(ObservableList<LongPeiceVO>  longPeiceVOs) {
        long min = 10000;
        for (LongPeiceVO temp : longPeiceVOs) {
            long tempMin =  temp.amount;
            if (tempMin < min) {
                min = tempMin;
            }
        }
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
