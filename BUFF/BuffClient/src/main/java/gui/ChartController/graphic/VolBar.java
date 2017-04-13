package gui.ChartController.graphic;

import gui.ChartController.tooltip.TooltipContentVolStick;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;

import java.time.LocalDate;

/**
 * Created by wshwbluebird on 2017/3/8.
 */
public class VolBar extends Group {

    /**
     * 用于显示的长条
     */
    private Region bar = new Region();

    /**
     * 判断是不是 开盘线大于收盘线
     */
    private boolean openAboveClose = true;

    /**
     * 提示工具栏
     */
    private Tooltip tooltip = new Tooltip();

    /**
     * 提供一个不带序列类型参数的方法
     */
    public VolBar(){
        setAutoSizeChildren(false);
        getChildren().addAll(bar);
        updateStyleClasses_Eastern();
        tooltip.setGraphic(new TooltipContentVolStick());
        Tooltip.install(bar, tooltip);
    }


    public void setSeriesAndDataStyleClasses(String seriesStyleClass, String dataStyleClass){

    }

    /**
     * 更新每个柱状图节点的数值
     * @param heightOffset
     * @param candleWidth
     * @param openAboveClose
     */
    public void update(double heightOffset, double candleWidth,boolean openAboveClose ) {
        //提前设置 bar的宽度  不然会有问题
        if (candleWidth == -1) {
            candleWidth = bar.prefWidth(-1);
        }
        this.openAboveClose = openAboveClose;
        //System.out.println( "vol    "+heightOffset+" "+ candleWidth+"  "+ openAboveClose);
        //更新CSS 参数
        updateStyleClasses_Eastern();

        //确定bar的位置
        bar.resizeRelocate(-candleWidth / 2, 0, candleWidth, heightOffset);
    }


    /**
     * 更新工具存储栏的信息
     * @param date
     * @param vol
     * @param change
     * @param rate
     */
    public void updateTooltip(LocalDate date, long vol, long change, double rate) {
        TooltipContentVolStick tooltipContentVolStick = (TooltipContentVolStick) tooltip.getGraphic();
        //System.out.println();
        tooltipContentVolStick.update(date, vol, change, rate);
        // tooltip.setText("Open: "+open+"\nClose: "+close+"\nHigh: "+high+"\nLow: "+low);
    }

    /**
     * 为画出的bar  添加CSS
     */
    private void updateStyleClasses_Eastern(){
        getStyleClass().setAll("volestick-vol");
        bar.getStyleClass().setAll("volstick-bar",
                openAboveClose ?  "open-above-close":"close-above-open");
    }
}
