package gui.ChartController;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.List;

/**
 * Created by wshwbluebird on 2017/3/13.
 */
public class MutiChartPane extends StackPane {

    private final AnchorPane detailsWindow;

    private final UpDownChart upDownChart;

    private final UpDownLineChart upDownLineChart;

    private double strokWidth = 0.3;

    Line xLine = new Line();
    Line yLine = new Line();

    public MutiChartPane(UpDownChart upDownChart,  UpDownLineChart upDownLineChart, Double strokWidth){
        this.detailsWindow  = new AnchorPane();

        this.upDownChart = upDownChart;
        this.upDownLineChart = upDownLineChart;

        styleBackgroundChart(this.upDownLineChart);
        this.upDownLineChart.setAnimated(false);
        this.upDownLineChart.setLegendVisible(false);
        this.upDownChart.setLegendVisible(false);

        this.upDownChart.getStylesheets()
                .add(getClass().getResource("/resources/css/mutiChart.css").toExternalForm());
        this.upDownLineChart.getStylesheets()
                .add(getClass().getResource("/resources/css/mutiForeChart.css").toExternalForm());
        this.upDownLineChart.setBackground(Background.EMPTY);
        getChildren().addAll(this.upDownChart,this.upDownLineChart);


        xLine.setStroke(Color.WHITE);
        yLine.setStroke(Color.WHITE);


        if (strokWidth != null) {
            this.strokWidth = strokWidth;
        }

        bindMouseEvents(this.upDownLineChart,this.strokWidth);
    }



    private void styleBackgroundChart(LineChart lineChart) {

        Node contentBackground = lineChart.lookup(".chart-content").lookup(".chart-plot-background");
        contentBackground.setStyle("-fx-background-color: transparent;");

        lineChart.setVerticalZeroLineVisible(false);
        lineChart.setHorizontalZeroLineVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setCreateSymbols(false);
    }

    private void bindMouseEvents(UpDownLineChart baseChart, Double strokeWidth) {
        final MutiChartPane.DetailsPopup detailsPopup = new MutiChartPane.DetailsPopup();
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

//            if (x + detailsPopup.getWidth() + 10 < getWidth()/2) {
//                AnchorPane.setLeftAnchor(detailsPopup, x+10);
//            } else {
            double full = upDownLineChart.getXAxis().widthProperty().doubleValue();
            //System.out.println("full:  "+full);
            AnchorPane.setRightAnchor(detailsPopup, full-(x+10-detailsPopup.getWidth()));
            //}
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

            String xValueStr = upDownChart.getXAxis().getValueForDisplay(event.getX());
            double realX = upDownChart.getXAxis().getDisplayPosition(xValueStr);
            if(!isMouseNearLine(realX,event.getX(),20.0)) {
                return ;
            }

            long yValue0 = (long)getYValueForX(xValueStr,0);
            long yValue1 = (long)getYValueForX(xValueStr,1);


            TooltipContentMutiStick tooltipContentMutiStick = new TooltipContentMutiStick();
            tooltipContentMutiStick.update(xValueStr,yValue0,yValue1);

            getChildren().add(tooltipContentMutiStick);

        }




        private boolean isMouseNearLine(double realXValue, double CurXValue, Double tolerance) {
            return (Math.abs(realXValue - CurXValue) < tolerance);
        }

        public Object getYValueForX( String xValue,int indexI) {
            List<XYChart.Data> dataList =
                    ((List<XYChart.Data>)((XYChart.Series) upDownChart.getData().get(indexI)).getData());
            for (XYChart.Data data : dataList) {
                if (data.getXValue().equals(xValue)) {
                    return data.getYValue();
                }
            }
            return null;
        }
    }
}

