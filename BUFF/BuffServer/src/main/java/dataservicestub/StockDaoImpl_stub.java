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
            LocalDate date = LocalDate.of(1997,2,i+6);
            StockPO stockPO = new StockPO();
            stockPO.setCode("1");
            stockPO.setDate(date);
            stockPO.setVolume((int)(close[i]*100));
            stockPO.setClose_Price(close[i]);
            stockPOs.add(stockPO);
        }

        return stockPOs;
    }

    @Override
    public List<StockPO> getStockInfoByDate(LocalDate date) {
        List<StockPO> stockPOs = new ArrayList<>();
        for (int i = 0 ; i < 10 ; i++){
            StockPO stockPO = new StockPO();
            stockPO.setCode("code"+String.valueOf(i));
            stockPO.setDate(date);
            stockPO.setClose_Price(date.getDayOfYear()%10+5);
            stockPO.setVolume(date.getDayOfYear()+i);
            stockPOs.add(stockPO);
        };
        return stockPOs;
    }

    @Override
    public List<StockPO> getMarketStockInfo() {
        List<StockPO> list = new ArrayList<StockPO>();
        StockPO stockOne = new StockPO("深发展A", "SZ", "1", LocalDate.of(1997, 2, 6), 11.25, 10.92,
                11.02, 11.16, 41362100, 11.16);
        list.add(stockOne);
        return list;
    }
}
