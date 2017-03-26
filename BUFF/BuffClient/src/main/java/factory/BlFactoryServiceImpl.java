//package factory;
//
//import blservice.comparison.ComparisonService;
//import blservice.market.MarketService;
//import blservice.singlestock.*;
//import blservice.thermometer.ThermometerService;
//import rmi.RemoteHelper;
//
///**
// * Created by slow_time on 2017/3/8.
// */
//public class BlFactoryServiceImpl implements BlFactoryService {
//    @Override
//    public KLineService createKLineService() {
//        return RemoteHelper.getInstance().getKLineService();
//    }
//
//    @Override
//    public MALineService createMALineService() {
//        return RemoteHelper.getInstance().getMALineService();
//    }
//
//    @Override
//    public StockDetailService createStockDetailService() {
//        return RemoteHelper.getInstance().getStockDetailService();
//    }
//
//    @Override
//    public VolService createVolService() {
//        return RemoteHelper.getInstance().getVolService();
//    }
//
//    @Override
//    public AllStockService createAllStockService() {
//        return RemoteHelper.getInstance().getAllStockService();
//    }
//
//    @Override
//    public ComparisonService createComparisonService() { return RemoteHelper.getInstance().getComparisonService(); }
//
//    @Override
//    public MarketService createMarketService() {
//        return RemoteHelper.getInstance().getMarketService();
//    }
//
//    @Override
//    public ThermometerService createThermometerService() {return RemoteHelper.getInstance().getThermometerService(); }
//}
