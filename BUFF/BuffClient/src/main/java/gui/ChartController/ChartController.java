package gui.ChartController;

import factory.BlFactoryService;
import factory.BlFactoryServiceImpl;

/**
 * Created by wshwbluebird on 2017/3/11.
 */
public enum ChartController  {
    INSTANCE;
    private  KLineChartController kLineChartController = null;

    private VOLChartController volChartController  =null;

    private  MAChartController maChartController = null;

    private  TheVOLChartController theVOLChartController = null;

    private BlFactoryService factory;

     ChartController(){
        factory =  new BlFactoryServiceImpl();
        this.kLineChartController = new KLineChartController();
        this.maChartController = new MAChartController();
        this.volChartController = new VOLChartController();
        this.theVOLChartController = new TheVOLChartController();
         plugServiceIn();
    }

    public void setFactory(BlFactoryService factory){
        this.factory = factory;
        plugServiceIn();

    }

    /**
     * 把factory生成的service注入到 特定的controller中
     */
    private void plugServiceIn(){
        this.volChartController.setVolService(factory.createVolService());
        this.kLineChartController.setkLineService(factory.createKLineService());
        this.maChartController.setMaLineService(factory.createMALineService());
        this.theVOLChartController.setThermometerService(factory.createThermometerService());
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

    public TheVOLChartController getTheVOLChartController() {
        return this.theVOLChartController;
    }

}
