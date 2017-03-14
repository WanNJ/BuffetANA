package gui.ChartController;

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
import vo.VolExtraVO;

import java.util.List;

/**
 * Created by wshwbluebird on 2017/3/13.
 */
public class TheVolPane extends StackPane {

    private final AnchorPane detailsWindow;

    private final ThemometerVolBarChart themometerVolBarChart;

    private double strokWidth = 0.3;

    Line xLine = new Line();
    Line yLine = new Line();

    public TheVolPane(ThemometerVolBarChart themometerVolBarChart, Double strokWidth){
        this.detailsWindow  = new AnchorPane();
        this.themometerVolBarChart = themometerVolBarChart;
        getChildren().add(themometerVolBarChart);


        xLine.setStroke(Color.WHITE);;
        yLine.setStroke(Color.WHITE);


        if (strokWidth != null) {
            this.strokWidth = strokWidth;
        }

        bindMouseEvents(themometerVolBarChart,this.strokWidth);
    }

    private void bindMouseEvents(ThemometerVolBarChart baseChart, Double strokeWidth) {
        final DetailsPopup detailsPopup = new DetailsPopup();
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
            double full = themometerVolBarChart.getXAxis().widthProperty().doubleValue();
            //System.out.println("full:  "+full);
            AnchorPane.setRightAnchor(detailsPopup, full-(x-40-detailsPopup.getWidth()));
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

            String xValueStr = themometerVolBarChart.getXAxis().getValueForDisplay(event.getX());
            double realX = themometerVolBarChart.getXAxis().getDisplayPosition(xValueStr);
            if(!isMouseNearLine(realX,event.getX(),themometerVolBarChart.getCandleWidth()/2)) {
                return ;
            }

            Object yValue = getYValueForX(xValueStr);
            TooltipContentVolStick tooltipContentVolStick = new TooltipContentVolStick();
            try {
                VolExtraVO extra = (VolExtraVO) yValue;
                tooltipContentVolStick.update(extra.date, extra.volume, extra.changeValue, extra.changeRate);
                getChildren().add(tooltipContentVolStick);
            }catch (Exception e){

            }


        }




        private boolean isMouseNearLine(double realXValue, double CurXValue, Double tolerance) {
            return (Math.abs(realXValue - CurXValue) < tolerance);
        }

        public Object getYValueForX( String xValue) {
            List<XYChart.Data> dataList =
                    ((List<XYChart.Data>)((XYChart.Series)themometerVolBarChart.getData().get(0)).getData());
            for (XYChart.Data data : dataList) {
                if (data.getXValue().equals(xValue)) {
                    return data.getExtraValue();
                }
            }
            return null;
        }
    }
}
