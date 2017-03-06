package dataserviceimpl.singlestock;

import dataservice.singlestock.StockDAO;
import po.StockPO;
import util.DateUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slow_time on 2017/3/5.
 */
public class StockDAOImpl implements StockDAO {
    @Override
    public List<StockPO> getStockInfoByCode(String code) {
        String codeFile = "/Users/slow_time/BuffettANA/Data/Code/" + code + ".csv";
        return null;
    }

    @Override
    public List<StockPO> getStockInfoByDate(LocalDate date) {
        String timeFile = "/Users/slow_time/BuffettANA/Data/Time/" + DateUtil.formatLine(date) + ".csv";
        List<StockPO> stockPOs = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(timeFile));
            stockPOs = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null) {
                stockPOs.add(generateStockPO(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return stockPOs;
        }
    }


    private StockPO generateStockPO(String fileLine) {
        String[] stockInfo = fileLine.split("\t");
        StockPO stockPO = new StockPO(stockInfo[9], stockInfo[10], stockInfo[8], DateUtil.parseSlash(stockInfo[1]),
                Double.parseDouble(stockInfo[3]), Double.parseDouble(stockInfo[4]), Double.parseDouble(stockInfo[2]),
                Double.parseDouble(stockInfo[5]), Long.parseLong(stockInfo[6]), Double.parseDouble(stockInfo[7]));
        return stockPO;
    }
}
