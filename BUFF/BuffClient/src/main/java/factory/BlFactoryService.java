package factory;

import blservice.comparison.ComparisonService;
import blservice.market.MarketService;
import blservice.singlestock.*;
import blservice.thermometer.ThermometerService;

/**
 * Created by slow_time on 2017/3/8.
 */
public interface BlFactoryService {

    KLineService createKLineService();

    MALineService createMALineService();

    StockDetailService createStockDetailService();

    VolService createVolService();

    AllStockService createAllStockService();

    ComparisonService createComparisonService();

    MarketService createMarketService();

    ThermometerService createThermometerService();

    BenchStockService createBenchStockService();
}
