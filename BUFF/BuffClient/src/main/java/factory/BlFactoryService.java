package factory;

import blservice.singlestock.*;

/**
 * Created by slow_time on 2017/3/8.
 */
public interface BlFactoryService {

    KLineService createKLineService();

    MALineService createMALineService();

    StockDetailService createStockDetailService();

    VolService createVolService();

    AllStockService createAllStockService();
}
