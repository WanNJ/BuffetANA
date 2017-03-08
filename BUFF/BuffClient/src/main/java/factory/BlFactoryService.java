package factory;

import blservice.singlestock.KLineService;
import blservice.singlestock.MALineService;
import blservice.singlestock.StockDetailService;
import blservice.singlestock.VolService;

/**
 * Created by slow_time on 2017/3/8.
 */
public interface BlFactoryService {

    KLineService createKLineService();

    MALineService createMALineService();

    StockDetailService createStockDetailService();

    VolService createVolService();
}
