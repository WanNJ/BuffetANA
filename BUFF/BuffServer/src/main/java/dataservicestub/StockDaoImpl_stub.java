package dataservicestub;

import dataservice.singlestock.StockDAO;
import po.StockPO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/3/12.
 */
public class StockDaoImpl_stub implements StockDAO {
    @Override
    public List<StockPO> getStockInfoByCode(String code) {
        List<StockPO> stockPOs = new ArrayList<>();
        double close[] = {8.15, 8.07, 8.84, 8.10,8.40,9.10,9.20, 9.10, 8.95, 8.70};

        for (int i = 0 ; i < 10 ; i++){
            LocalDate date = LocalDate.of(2017,1,i+1);
            StockPO stockPO = new StockPO();
            stockPO.setDate(date);
            stockPO.setVolume(10);
            stockPO.setClose_Price(close[i]);
            stockPOs.add(stockPO);
        }
        //System.out.println("in stub listSize:  "+stockPOs.size());
        return stockPOs;
    }

    @Override
    public List<StockPO> getStockInfoByDate(LocalDate date) {
        return null;
    }
}
