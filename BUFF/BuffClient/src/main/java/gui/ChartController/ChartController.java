package gui.ChartController;

/**
 * Created by wshwbluebird on 2017/3/11.
 */
public enum ChartController  {
    INSTANCE;
    private  KLineChartController kLineChartController = null;

    private VOLChartController volChartController  =null;

    private  MAChartController maChartController = null;

     ChartController(){
        this.kLineChartController = new KLineChartController();
        this.maChartController = new MAChartController();
        this.volChartController = new VOLChartController();
    }

    public KLineChartController getKLineChartController() {
        return this.kLineChartController;
    }

    public VOLChartController getVOLChartController() {
        return this.volChartController;
    }

    public MAChartController getMAChartController() {
        return this.maChartController;
    }

}
