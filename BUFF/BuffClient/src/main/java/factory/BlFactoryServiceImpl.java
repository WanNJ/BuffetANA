package factory;

import blservice.singlestock.KLineService;
import blservice.singlestock.MALineService;
import blservice.singlestock.StockDetailService;
import blservice.singlestock.VolService;

/**
 * Created by slow_time on 2017/3/8.
 */
public class BlFactoryServiceImpl implements BlFactoryService {
    @Override
    public KLineService createKLineService() {
        return null;
    }

    @Override
    public MALineService createMALineService() {
        return null;
    }

    @Override
    public StockDetailService createStockDetailService() {
        return null;
    }

    @Override
    public VolService createVolService() {
        return null;
    }
}
