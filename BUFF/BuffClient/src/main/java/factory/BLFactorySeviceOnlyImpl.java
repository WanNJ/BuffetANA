package factory;

import SinglePort.SingleBlService;
import blservice.comparison.ComparisonService;
import blservice.market.MarketService;
import blservice.singlestock.*;
import blservice.thermometer.ThermometerService;

/**
 * Created by wshwbluebird on 2017/3/26.
 */
public class BLFactorySeviceOnlyImpl implements BlFactoryService {
    @Override
    public KLineService createKLineService() {
        return SingleBlService.SINGLE_BL_SERVICE.getKLineService();
    }

    @Override
    public MALineService createMALineService() {
        return SingleBlService.SINGLE_BL_SERVICE.getMALineService();
    }

    @Override
    public StockDetailService createStockDetailService() {
        return SingleBlService.SINGLE_BL_SERVICE.getStockDetailService();
    }

    @Override
    public VolService createVolService() {
        return SingleBlService.SINGLE_BL_SERVICE.getVolService();
    }

    @Override
    public AllStockService createAllStockService() {
        return SingleBlService.SINGLE_BL_SERVICE.getAllStockService();
    }

    @Override
    public ComparisonService createComparisonService() {
        return SingleBlService.SINGLE_BL_SERVICE.getComparisonService();
    }

    @Override
    public MarketService createMarketService() {
        return SingleBlService.SINGLE_BL_SERVICE.getMarketService();
    }

    @Override
    public ThermometerService createThermometerService() {
        return SingleBlService.SINGLE_BL_SERVICE.getThermometerService();
    }
}
