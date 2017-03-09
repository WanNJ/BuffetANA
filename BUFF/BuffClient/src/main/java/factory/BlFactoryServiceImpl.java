package factory;

import blservice.singlestock.KLineService;
import blservice.singlestock.MALineService;
import blservice.singlestock.StockDetailService;
import blservice.singlestock.VolService;
import rmi.RemoteHelper;

/**
 * Created by slow_time on 2017/3/8.
 */
public class BlFactoryServiceImpl implements BlFactoryService {
    @Override
    public KLineService createKLineService() {
        return RemoteHelper.getInstance().getKLineService();
    }

    @Override
    public MALineService createMALineService() {
        return RemoteHelper.getInstance().getMALineService();
    }

    @Override
    public StockDetailService createStockDetailService() {
        return RemoteHelper.getInstance().getStockDetailService();
    }

    @Override
    public VolService createVolService() {
        return RemoteHelper.getInstance().getVolService();
    }
}
