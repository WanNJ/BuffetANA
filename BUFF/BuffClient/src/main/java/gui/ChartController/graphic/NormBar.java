package gui.ChartController.graphic;

import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;

import java.time.LocalDate;

/**
 * Created by wshwbluebird on 2017/3/8.
 */
public class NormBar extends Group {

    /**
     * 用于显示的长条
     */
    private Region bar = new Region();




    /**
     * 提供一个不带序列类型参数的方法
     */
    public NormBar(){
        setAutoSizeChildren(false);
        getChildren().addAll(bar);
        updateStyleClasses_Eastern();

    }


    public void setSeriesAndDataStyleClasses(String seriesStyleClass, String dataStyleClass){

    }

    /**
     * 更新每个柱状图节点的数值
     * @param heightOffset
     * @param candleWidth
     */
    public void update(double heightOffset, double candleWidth) {
        //提前设置 bar的宽度  不然会有问题
        if (candleWidth == -1) {
            candleWidth = bar.prefWidth(-1);
        }
        //System.out.println( "vol    "+heightOffset+" "+ candleWidth+"  "+ openAboveClose);
        //更新CSS 参数
        updateStyleClasses_Eastern();

        //确定bar的位置
        bar.resizeRelocate(-candleWidth / 2, 0, candleWidth, heightOffset);
    }

    /**
     * 为画出的bar  添加CSS
     * TODO
     */
    private void updateStyleClasses_Eastern(){
        getStyleClass().setAll("volestick-vol");
        bar.getStyleClass().setAll("volstick-bar"+".close-above-open");
    }
}
