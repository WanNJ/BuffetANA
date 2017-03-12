package dataserviceimpl.singlestock;

import dataservice.singlestock.StockDAO;
import po.StockPO;
import util.DateUtil;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<StockPO> getMarketStockInfo() {
        File root = new File("../Data/Time");
        File[] files =root.listFiles();
        return Arrays.asList(files).stream().map(file -> generateMarketStock(file)).sorted((stockPO1, stockPO2) -> {
            if(stockPO1.getDate().isEqual(stockPO2.getDate()))
                return 0;
            return stockPO1.getDate().isBefore(stockPO2.getDate()) ? -1 : 1;
        }).collect(Collectors.toList());
    }


    /**
     * 执行IO操作
     * 根据所给的文件名，读取出里面所有的股票数据，并转换成PO列表的形式传出去
     * @param fileName 所要查询的文件名
     * @return 若所给文件不存在，则返回null，该方法返回的List已经是按日期从小到大排好序的
     */
    private List<StockPO> generateStockPOs(String fileName) {
        /**
         * change bby wsw
         * 为了更好的运用lambda表达式
         */
        List<StockPO> stockPOs = new ArrayList<>();
        BufferedReader br = null;
        try {
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

    private StockPO generateMarketStock(File file) {
        StockPO stockPO = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));

            //读出第一组数据作为基准数据
            String line = br.readLine();
            String[] stockInfo = line.split("\t");
            String name = stockInfo[9];
            String market = stockInfo[10];
            String code = stockInfo[8];
            LocalDate date = DateUtil.parseSlash(stockInfo[1]);
            double high_Price = Double.parseDouble(stockInfo[3]);
            double low_Price = Double.parseDouble(stockInfo[4]);
            double open_Price = Double.parseDouble(stockInfo[2]);
            double close_Price = Double.parseDouble(stockInfo[5]);
            long volume = Long.parseLong(stockInfo[6]);
            double adjCloseIndex = Double.parseDouble(stockInfo[7]);

            //计数器，用于作为计算平均值时的除数
            int count = 1;

            //读出所有数据，并根据读出的数据更新上述数据
            while ((line = br.readLine()) != null) {
                count++;
                stockInfo = line.split("\t");
                if(Double.parseDouble(stockInfo[3]) > high_Price)
                    high_Price = Double.parseDouble(stockInfo[3]);
                if(Double.parseDouble(stockInfo[4]) < low_Price)
                    low_Price = Double.parseDouble(stockInfo[4]);
                open_Price = open_Price + Double.parseDouble(stockInfo[2]);
                close_Price = close_Price + Double.parseDouble(stockInfo[5]);
                volume = volume + Long.parseLong(stockInfo[6]);
                adjCloseIndex = adjCloseIndex + Double.parseDouble(stockInfo[7]);
            }
            open_Price /= count;
            close_Price /= count;
            adjCloseIndex /= count;
            stockPO = new StockPO(name, market, code, date, high_Price, low_Price,
                    open_Price, close_Price, volume, adjCloseIndex);
        } catch (FileNotFoundException e) {
            System.out.println("no data found");
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return stockPO;
        }
    }
}
