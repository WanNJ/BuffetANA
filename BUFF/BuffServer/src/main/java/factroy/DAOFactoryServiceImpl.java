package factroy;

import dataservice.singlestock.StockDAO;
import dataservice.stockmap.StockNameToCodeDAO;
import dataserviceimpl.singlestock.StockDAOImpl;
import dataserviceimpl.stockmap.StockNameToCodeDAOImpl;

/**
 * Created by slow_time on 2017/3/7.
 */
public class DAOFactoryServiceImpl implements DAOFactoryService {
    @Override
    public StockDAO createStockDAO() {
        return new StockDAOImpl();
    }

    @Override
    public StockNameToCodeDAO createStockNameToCodeDAO() {
        return new StockNameToCodeDAOImpl();
    }
}
