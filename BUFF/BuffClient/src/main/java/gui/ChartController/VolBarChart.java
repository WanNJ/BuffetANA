package gui.ChartController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import vo.KLineExtraVO;
import vo.KLinePieceVO;
import vo.StockVolVO;
import vo.VolExtraVO;

/**
 * Created by wshwbluebird on 2017/3/8.
 */

/**
 * 交易量图
 */
public class VolBarChart extends XYChart<String,Number> {



    private static final int  Height=200;
    private static final int  Width=600;
    /**
     * Constructs a XYChart given the two axes. The initial content for the chart
     * plot background and plot area that includes vertical and horizontal grid
     * lines and fills, are added.
     *
     * @param stringAxis X Axis for this XY chart
     * @param numberAxis Y Axis for this XY chart
     */
    public VolBarChart(Axis<String> stringAxis, Axis<Number> numberAxis) {
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
    public VolBarChart(Axis<String> xAxis, Axis<Number> yAxis, ObservableList<Series<String, Number>> data) {
        this(xAxis, yAxis);
        setData(data);
    }

    @Override
    protected void dataItemAdded(Series<String, Number> series, int itemIndex, Data<String, Number> item) {

    }

    @Override
    protected void dataItemRemoved(Data<String, Number> item, Series<String, Number> series) {

    }

    @Override
    protected void dataItemChanged(Data<String, Number> item) {

    }

    @Override
    protected void seriesAdded(Series<String, Number> series, int seriesIndex) {

    }

    @Override
    protected void seriesRemoved(Series<String, Number> series) {

    }

    @Override
    protected void layoutPlotChildren() {

    }

    /**
     * 用于直接创建vol图的外部方法
     * @param stockVolVOs
     * @return KLineChart  可直接在panel上加载的node
     */
    public static VolBarChart createChart(ObservableList<StockVolVO> stockVolVOs){
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
        final VolBarChart volBarChart= new VolBarChart(xAxis,yAxis);

        xAxis.setLabel("Day");
        yAxis.setLabel("VOL");

        // 加载传过来的数据序列
        XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();

        //TODO 未来更改为lambda表达式
        for (int i=0; i< stockVolVOs.size(); i++) {
            StockVolVO vo = stockVolVOs.get(i);

            //计算额外附加值
            VolExtraVO volExtraVO =  new VolExtraVO();
            if(i!=0){
                StockVolVO last =  stockVolVOs.get(i-1);
                volExtraVO.changeValue = vo.vol - last.vol;

                //为了保险起见还是检验一下吧!!!!
                if(last.vol!=0){
                    volExtraVO.changeRate =  volExtraVO.changeValue/last.vol;
                }
            }


            //TODO  日期的字符串化
            //参数：日期、成交量
            series.getData().add(   new XYChart.Data<String,Number>
                    (vo.date.toString(),vo.vol,volExtraVO));
            new XYChart.Data<String,Number>();
        }
        ObservableList<XYChart.Series<String,Number>> data = volBarChart.getData();


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
        if(curWidth<Width){
            curWidth=Width;
        }
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
}
