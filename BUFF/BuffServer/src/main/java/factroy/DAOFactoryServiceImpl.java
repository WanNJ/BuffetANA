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
        return StockDAOImpl.STOCK_DAO_IMPL;
    }

    @Override
    public StockNameToCodeDAO createStockNameToCodeDAO() {
        return StockNameToCodeDAOImpl.STOCK_NAME_TO_CODE_DAO_IMPL;
    }
}
