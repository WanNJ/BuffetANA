//package gui.ChartController;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.chart.Axis;
//import javafx.scene.chart.CategoryAxis;
//import javafx.scene.chart.NumberAxis;
//import javafx.scene.chart.XYChart;
//import vo.KLineExtraVO;
//import vo.KLinePieceVO;
//import vo.StockVolVO;
//
///**
// * Created by wshwbluebird on 2017/3/8.
// */
//public class VolBarChart extends XYChart<String,Number> {
//    /**
//     * Constructs a XYChart given the two axes. The initial content for the chart
//     * plot background and plot area that includes vertical and horizontal grid
//     * lines and fills, are added.
//     *
//     * @param stringAxis X Axis for this XY chart
//     * @param numberAxis Y Axis for this XY chart
//     */
//    public VolBarChart(Axis<String> stringAxis, Axis<Number> numberAxis) {
//        super(stringAxis, numberAxis);
//        setAnimated(false);
//        stringAxis.setAnimated(false);
//        numberAxis.setAnimated(false);
//    }
//
//    @Override
//    protected void dataItemAdded(Series<String, Number> series, int itemIndex, Data<String, Number> item) {
//
//    }
//
//    @Override
//    protected void dataItemRemoved(Data<String, Number> item, Series<String, Number> series) {
//
//    }
//
//    @Override
//    protected void dataItemChanged(Data<String, Number> item) {
//
//    }
//
//    @Override
//    protected void seriesAdded(Series<String, Number> series, int seriesIndex) {
//
//    }
//
//    @Override
//    protected void seriesRemoved(Series<String, Number> series) {
//
//    }
//
//    @Override
//    protected void layoutPlotChildren() {
//
//    }
//
//
//    public static KLineChart createChart(ObservableList<StockVolVO> stockVolVOs){
//        System.out.println("List of Observe:  "+kLinePieceVOs.size());
//        //获取图像最大值
//        double max=getMax(kLinePieceVOs);
//        //获取图像最小值
//        double min =getMin(kLinePieceVOs);
//        //  参考学长的代码 计算间距
//        double gap=(max-min)/10;
//        double HpixelPerValue=20;
//        //X轴
//        final CategoryAxis xAxis = new CategoryAxis ();
//        //Y轴
//        final NumberAxis yAxis = new NumberAxis(min-gap,max+gap*2,gap);
//        // 初始化K线图
//        final KLineChart kLineChart= new KLineChart(xAxis,yAxis);
//
//        xAxis.setLabel("Day");
//        yAxis.setLabel("Price");
//
//        // 加载传过来的数据序列
//        XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();
//
//        //TODO 未来更改为lambda表达式
//        for (int i=0; i< kLinePieceVOs.size(); i++) {
//            KLinePieceVO vo = kLinePieceVOs.get(i);
//            //参数：日期、开盘价
//            //TODO  日期的字符串化
//            series.getData().add(   new XYChart.Data<String,Number>
//                    (vo.date.toString(),vo.openPrice,
//                            new KLineExtraVO(vo.highPrice,vo.lowPrice,vo.openPrice,vo.closePrice,
//                                    (vo.lowPrice+vo.highPrice)/2)));
//            new XYChart.Data<String,Number>();
//        }
//        ObservableList<XYChart.Series<String,Number>> data = kLineChart.getData();
//
//
//        //默认不会指甲再一次
//        //TODO 迭代二可能需要修改， 现在不确定
//        if (data == null) {
//
//            data = FXCollections.observableArrayList(series);
//            kLineChart.setData(data);
//        } else {
//            kLineChart.getData().add(series);
//        }
//        kLineChart.setData(data);
//
//        //System.out.println("datasize:   "+data.size());
//        double curWidth = HpixelPerValue*kLinePieceVOs .size();
//        if(curWidth<Width){
//            curWidth=Width;
//        }
//        kLineChart.setPrefSize(curWidth,Height*0.95);
//
//
//        return kLineChart;
//    }
//}
