package SinglePort;

import blservice.comparison.ComparisonService;
import blservice.market.MarketService;
import blservice.singlestock.*;
import blservice.thermometer.ThermometerService;
import blserviceimpl.comparison.ComparisonImpl;
import blserviceimpl.market.MarketServiceImpl;
import blserviceimpl.singlestock.*;
import blserviceimpl.thermometer.ThermometerServiceImpl;

/**
 * Created by wshwbluebird on 2017/3/26.
 */
public enum SingleBlService {
    SINGLE_BL_SERVICE;

    public KLineService getKLineService() {
        return KLineServiceImpl.K_LINE_SERVICE;
    }

    public MALineService getMALineService() {
        return MALineServiceImpl.MA_LINE_SERVICE;
    }

    public StockDetailService getStockDetailService() {
        return StockDetailServiceImpl.STOCK_DETAIL_SERVICE;
    }

    public VolService getVolService() {
        return VolServiceImpl.VOL_SERVICE;
    }

    public AllStockService getAllStockService() {
        return AllStockServiceImpl.ALL_STOCK_SERVICE;
    }

    public ComparisonService getComparisonService() { return ComparisonImpl.COMPARISON_SERVICE; }

    public MarketService getMarketService() {
        return  MarketServiceImpl.MARKET_SERVICE;
    }

    public ThermometerService getThermometerService(){return ThermometerServiceImpl.THERMOMETER_SERVCE;}

    public BenchStockService getBenchStockService() { return BenchStockServiceImpl.BENCH_STOCK_SERVICE; }

}
