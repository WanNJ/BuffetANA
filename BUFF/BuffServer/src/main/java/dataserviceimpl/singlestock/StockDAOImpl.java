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
 * Created by slow_time on 2017/3/8.
 */
public enum StockDAOImpl implements StockDAO{
    STOCK_DAO_IMPL;

    @Override
    public List<StockPO> getStockInfoByCode(String code) {
        String codeFile = "../Data/Code/" + code + ".csv";
        return generateStockPOs(codeFile);
    }

    @Override
    public List<StockPO> getStockInfoByDate(LocalDate date) {
        String timeFile = "../Data/Time/" + DateUtil.formatLine(date) + ".csv";
        return generateStockPOs(timeFile);
    }


    /**
     * 执行IO操作
     * 根据所给的文件名，读取出里面所有的股票数据，并转换成PO列表的形式传出去
     * @param fileName 所要查询的文件名
     * @return 若所给文件不存在，则返回null，该方法返回的List已经是按日期从小到大排好序的
     */
    private List<StockPO> generateStockPOs(String fileName) {
        List<StockPO> stockPOs = null;
        BufferedReader br = null;
        try {
            //TODO
            System.out.println(fileName);
            br = new BufferedReader(new FileReader(fileName));
            stockPOs = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null) {
                String[] stockInfo = line.split("\t");
                StockPO stockPO = new StockPO(stockInfo[9], stockInfo[10], stockInfo[8], DateUtil.parseSlash(stockInfo[1]),
                        Double.parseDouble(stockInfo[3]), Double.parseDouble(stockInfo[4]), Double.parseDouble(stockInfo[2]),
                        Double.parseDouble(stockInfo[5]), Long.parseLong(stockInfo[6]), Double.parseDouble(stockInfo[7]));
                stockPOs.add(stockPO);
            }
            stockPOs.sort((stockPO1, stockPO2) -> {
                if(stockPO1.getDate().isEqual(stockPO2.getDate()))
                    return 0;
                return stockPO1.getDate().isBefore(stockPO2.getDate()) ? -1 : 1;
            });
        } catch (FileNotFoundException e) {
            System.out.println("no data found");
            //
            // e.printStackTrace();
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
}
