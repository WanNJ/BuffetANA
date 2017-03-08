package gui.ChartController;

import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;


/**
 * 画出K线的candle
 */
class Candle extends Group {
    /**
     * 从最低点到最高点
     */
    private Line highLowLine = new Line();

    /**
     *从收盘到开盘的一整块区域
     */
    private Region bar = new Region();
    private String seriesStyleClass;
    private String dataStyleClass;

    /**
     * 开盘线受否大于收盘线
     */
    private boolean openAboveClose = true;

    /**
     * 提示工具栏
     */
    private Tooltip tooltip = new Tooltip();

    public Candle(String seriesStyleClass, String dataStyleClass) {
        //System.out.println(seriesStyleClass+"  "+dataStyleClass);
        setAutoSizeChildren(false);
        getChildren().addAll(highLowLine, bar);
        this.seriesStyleClass = seriesStyleClass;
        this.dataStyleClass = dataStyleClass;
        updateStyleClasses_Eastern();
        tooltip.setGraphic(new TooltipContentCandleStick());
        Tooltip.install(bar, tooltip);
    }


    public void setSeriesAndDataStyleClasses(String seriesStyleClass, String dataStyleClass) {
        this.seriesStyleClass = seriesStyleClass;
        this.dataStyleClass = dataStyleClass;
        updateStyleClasses_Eastern();
    }

    // candle.update(close - y, high - y, low - y, candleWidth);   y=open

    /**
     * 画candle
     * @param closeOffset
     * @param highOffset
     * @param lowOffset
     * @param candleWidth
     */
    public void update(double closeOffset, double highOffset, double lowOffset, double candleWidth) {
        //提前设置 bar的宽度  不然会有问题
        if (candleWidth == -1) {
            candleWidth = bar.prefWidth(-1);
        }
       // System.out.println( closeOffset+"  "+candleWidth+"   "+closeOffset );
        openAboveClose = closeOffset > 0;
        updateStyleClasses_Eastern();
//        highLowLine.setStartY(highOffset);
//        highLowLine.setEndY(lowOffset);
        highLowLine.setStartY(highOffset);
        highLowLine.setEndY(lowOffset);




       // System.out.println("after if:  "+ closeOffset+"  "+candleWidth+"   "+closeOffset );
        if (openAboveClose) {
            bar.resizeRelocate(-candleWidth / 2, 0, candleWidth, closeOffset);
            //System.out.println( closeOffset+"  "+candleWidth+"   "+closeOffset );

        } else {
            bar.resizeRelocate(-candleWidth / 2, closeOffset, candleWidth, closeOffset * -1);
           // System.out.println( closeOffset+"  "+candleWidth+"   "+closeOffset * -1);
        }
    }

    /**
     * 更新存储工具栏的值
     * @param open
     * @param close
     * @param high
     * @param low
     */
    public void updateTooltip(double open, double close, double high, double low) {
        TooltipContentCandleStick tooltipContent = (TooltipContentCandleStick) tooltip.getGraphic();
        //System.out.println();
        tooltipContent.update(open, close, high, low);
              // tooltip.setText("Open: "+open+"\nClose: "+close+"\nHigh: "+high+"\nLow: "+low);
    }

    /**
     * 为每个参数加载 CSS
     *
     */
    private void updateStyleClasses_Eastern() {

//        getStyleClass().setAll("candlestick-candle", seriesStyleClass, dataStyleClass);
//        highLowLine.getStyleClass().setAll("candlestick-line", seriesStyleClass, dataStyleClass,
//                openAboveClose ?   "open-above-close":"close-above-open");
//        bar.getStyleClass().setAll("candlestick-bar", seriesStyleClass, dataStyleClass,
//                openAboveClose ?  "open-above-close":"close-above-open");

        getStyleClass().setAll("candlestick-candle");
        highLowLine.getStyleClass().setAll("candlestick-line",
                openAboveClose ?   "open-above-close":"close-above-open");
        bar.getStyleClass().setAll("candlestick-bar",
                openAboveClose ?  "open-above-close":"close-above-open");
    }


}