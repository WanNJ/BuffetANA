package factory;

import blservice.singlestock.*;
import blstub.singlestockstub.AllStockServiceImpl_Stub;
import blstub.singlestockstub.KLineServiceImpl_Stub;
import blstub.singlestockstub.StockDetailServiceImpl_Stub;
import blstub.singlestockstub.VolServiceImpl_Stub;

/**
 * Created by slow_time on 2017/3/8.
 */
public class BlFactoryServiceStub implements BlFactoryService {
    @Override
    public KLineService createKLineService() {
        return new KLineServiceImpl_Stub();
    }

    @Override
    public AllStockService createAllStockService() {
        return new AllStockServiceImpl_Stub();
    }

    @Override
    public MALineService createMALineService() {
        return null;
    }

    @Override
    public StockDetailService createStockDetailService() {
        return new StockDetailServiceImpl_Stub();
    }

    @Override
    public VolService createVolService() {
        return new VolServiceImpl_Stub();
    }
}
