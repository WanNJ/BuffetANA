package factroy;

import dataservice.singlestock.StockDAO;
import dataservice.stockmap.StockNameToCodeDAO;
import dataservice.strategy.StrategyDAO;
import dataservicestub.StockDaoImpl_stub;
import dataservicestub.StockNameToCodeDAOImpl_stub;

/**
 * Created by Accident on 2017/3/14.
 */
public class DAOFactoryServiceImpl_Stub implements DAOFactoryService {
    @Override
    public StockDAO createStockDAO() {
        return new StockDaoImpl_stub();
    }

    @Override
    public StockNameToCodeDAO createStockNameToCodeDAO() {
        return new StockNameToCodeDAOImpl_stub();
    }

    @Override
    public StrategyDAO createStrategyDAO() {
        return null;
    }
}
