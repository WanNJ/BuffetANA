package dataserviceimpl.singlestock;

import dataservice.singlestock.StockDAO;
import po.StockPO;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by slow_time on 2017/3/5.
 */
public class StockDAOImpl implements StockDAO {
    @Override
    public List<StockPO> getStockInfoByCode(String code) {
        return null;
    }

    @Override
    public List<StockPO> getStockInfoByDate(LocalDate date) {
        return null;
    }
}
