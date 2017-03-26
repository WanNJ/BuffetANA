package factroy;

import dataservice.singlestock.StockDAO;
import dataservice.stockmap.StockNameToCodeDAO;
import dataservice.strategy.StrategyDAO;

/**
 * Created by slow_time on 2017/3/7.
 */
public interface DAOFactoryService {

    StockDAO createStockDAO();

    StockNameToCodeDAO createStockNameToCodeDAO();

    StrategyDAO createStrategyDAO();
}
