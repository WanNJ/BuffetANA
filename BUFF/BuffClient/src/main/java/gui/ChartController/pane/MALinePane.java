package gui.ChartController.pane;

import gui.ChartController.chart.MALineChart;
import gui.ChartController.tooltip.TooltipMAContentStick;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.List;

/**
 * Created by wshwbluebird on 2017/3/13.
 */
public class MALinePane extends StackPane {

    private final AnchorPane detailsWindow;

    private final MALineChart maLineChart;

    private double strokWidth = 0.3;

    Line xLine = new Line();
    Line yLine = new Line();

    public MALinePane(MALineChart maLineChart, Double strokWidth){
        this.detailsWindow  = new AnchorPane();
        this.maLineChart = maLineChart;
        getChildren().add(maLineChart);


        xLine.setStroke(Color.BLACK);;
        yLine.setStroke(Color.BLACK);


        if (strokWidth != null) {
            this.strokWidth = strokWidth;
        }

        bindMouseEvents(maLineChart,this.strokWidth);
    }

    private void bindMouseEvents(MALineChart baseChart, Double strokeWidth) {
        final MALinePane.DetailsPopup detailsPopup = new MALinePane.DetailsPopup();
        getChildren().add(detailsWindow);
        detailsWindow.getChildren().add(detailsPopup);
        detailsWindow.prefHeightProperty().bind(heightProperty());
        detailsWindow.prefWidthProperty().bind(widthProperty());
        detailsWindow.setMouseTransparent(true);

        setOnMouseMoved(null);
        setMouseTransparent(false);

        final Axis xAxis = baseChart.getXAxis();
        final Axis yAxis = baseChart.getYAxis();

        yLine.setStrokeWidth(strokeWidth);
        xLine.setStrokeWidth(strokeWidth);
        xLine.setVisible(false);
        yLine.setVisible(false);
//
        final Node chartBackground = baseChart.lookup(".chart-plot-background");
        for (Node n: chartBackground.getParent().getChildrenUnmodifiable()) {
            if (n != chartBackground && n != xAxis && n != yAxis) {
                n.setMouseTransparent(true);
            }
        }
        chartBackground.setCursor(Cursor.CROSSHAIR);
        chartBackground.setOnMouseEntered((event) -> {
            chartBackground.getOnMouseMoved().handle(event);
            detailsPopup.setVisible(true);
            xLine.setVisible(true);
            yLine.setVisible(true);
            detailsWindow.getChildren().addAll(xLine, yLine);
        });
        chartBackground.setOnMouseExited((event) -> {
            detailsPopup.setVisible(false);
            xLine.setVisible(false);
            yLine.setVisible(false);
            detailsWindow.getChildren().removeAll(xLine, yLine);
        });
        chartBackground.setOnMouseMoved(event -> {
            double x = event.getX() + chartBackground.getLayoutX();
            double y = event.getY() + chartBackground.getLayoutY();

            xLine.setStartX(10);
            xLine.setEndX(detailsWindow.getWidth()-10);
            xLine.setStartY(y+5);
            xLine.setEndY(y+5);

            yLine.setStartX(x+5);
            yLine.setEndX(x+5);
            yLine.setStartY(10);
            yLine.setEndY(detailsWindow.getHeight()-10);

            detailsPopup.showChartDescrpition(event);

            if (y + detailsPopup.getHeight() + 10 < getHeight()) {
                AnchorPane.setTopAnchor(detailsPopup, y+10);
            } else {
                AnchorPane.setTopAnchor(detailsPopup, y-10-detailsPopup.getHeight());
            }

//            if (x + detailsPopup.getWidth() + 10 < getWidth()) {
//                AnchorPane.setLeftAnchor(detailsPopup, x+10);
//            } else {
            AnchorPane.setLeftAnchor(detailsPopup, x-10-detailsPopup.getWidth());
            //}
        });
    }






    private class DetailsPopup extends VBox {

        private DetailsPopup() {
            setStyle("-fx-border-width: 1px; -fx-padding: 5 5 5 5px; -fx-border-color: gray; -fx-background-color: whitesmoke;");
            setVisible(false);
        }

        public void showChartDescrpition(MouseEvent event) {
            getChildren().clear();

            String xValueStr = maLineChart.getXAxis().getValueForDisplay(event.getX());
            double realX = maLineChart.getXAxis().getDisplayPosition(xValueStr);
            if(!isMouseNearLine(realX,event.getX(),5.0)) {
                return ;
            }

            double yValue0 = (double)getYValueForX(xValueStr,0);
            double yValue1 = (double)getYValueForX(xValueStr,1);
            double yValue2 = (double)getYValueForX(xValueStr,2);
            double yValue3 = (double)getYValueForX(xValueStr,3);

            TooltipMAContentStick tooltipMAContentStick = new TooltipMAContentStick();
            tooltipMAContentStick.update(yValue0,yValue1,yValue2,yValue3);

            getChildren().add(tooltipMAContentStick);

        }




        private boolean isMouseNearLine(double realXValue, double CurXValue, Double tolerance) {
            return (Math.abs(realXValue - CurXValue) < tolerance);
        }

        public Object getYValueForX( String xValue,int indexI) {
            List<XYChart.Data> dataList =
                    ((List<XYChart.Data>)((XYChart.Series)maLineChart.getData().get(indexI)).getData());
            for (XYChart.Data data : dataList) {
                if (data.getXValue().equals(xValue)) {
                    return data.getYValue();
                }
            }
            return null;
        }
    }
}

