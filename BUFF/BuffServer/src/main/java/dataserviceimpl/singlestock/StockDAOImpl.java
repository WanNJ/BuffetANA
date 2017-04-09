package dataserviceimpl.singlestock;

import dataservice.singlestock.StockDAO;
import po.StockPO;
import util.DateUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by slow_time on 2017/3/8.
 */
public enum StockDAOImpl implements StockDAO{
    STOCK_DAO_IMPL ;

    StockDAOImpl(){
        if (noneDate == null)
            noneDate = getNoneTradeDates();
    }
    /**
     * 提前缓存  减少一次读文件的次数
     * add by wsw
     * 程序 从8571 ms  减少到 5431  很明显
     */
    String code = null;  //存储上一次读取的股票代码

    List<StockPO> codeList = null;  //存取上一次的股票列表

    List<LocalDate>  noneDate = null; //存储非交易日

    HashMap<String, Double> changeRate = null;  //换手率

    HashMap<String, Double> circulationMarketValue = null; //流通市值

    @Override
    public List<StockPO> getStockInFoInRangeDate(String code, LocalDate begin, LocalDate end) {
        List<StockPO> list;




        if(code.equals(this.code))
            list = this.codeList;
        else{
            String codeFile = "../Data/Code/" + code + ".csv";
            this.codeList = generateStockPOsAfterStart(codeFile,begin.minusDays(200));
            //this.codeList =getStockInfoByCode(code);
            this.code  =code;
        }


        list = this.codeList;
       //System.out.println(code+"  "+codeList.size());
//
        List<StockPO> stockPOs = noneDate.stream().map(t->new StockPO(code,t)).collect(Collectors.toList());
        list.removeAll(stockPOs);


//        list.removeAll()
        //list = getStockInfoByCode(code);
        return list.stream()
                .filter(t->!(t.getDate().isBefore(begin) || t.getDate().isAfter(end)))
                .collect(Collectors.toList());



    }


    @Override
    public List<StockPO> getStockInfoByCode(String code) {
        String codeFile = "../Data/Code/" + code + ".csv";
        return generateStockPOs(codeFile);
        //return generateStockPOsInJSE8(codeFile);
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
        return Arrays.asList(files).stream().filter(file -> !file.isDirectory() && file.getAbsoluteFile().toString().endsWith(".csv")).map(file -> generateMarketStock(file)).sorted((stockPO1, stockPO2) -> {
            if(stockPO1.getDate().isEqual(stockPO2.getDate()))
                return 0;
            return stockPO1.getDate().isBefore(stockPO2.getDate()) ? -1 : 1;
        }).collect(Collectors.toList());
    }

    @Override
    public double getStockChangeRate(String code) {
        if(changeRate != null)
            return changeRate.get(code);
        else {
            BufferedReader br = null;

            try {
                InputStreamReader reader = new InputStreamReader(new FileInputStream("../Data/ChangeRate.csv"), "UTF-8");
                br = new BufferedReader(reader);
                changeRate = new HashMap<>();
                String line;

                while ((line = br.readLine()) != null) {
                    String[] nameAndCode = line.split(",");
                    changeRate.put(nameAndCode[0], Double.valueOf(nameAndCode[1]));
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
                return changeRate.get(code);
            }
        }
    }

    @Override
    public double getStockCirculationMarketValue(String code) {
        if(circulationMarketValue != null)
            return circulationMarketValue.get(code);
        else {
            BufferedReader br = null;

            try {
                InputStreamReader reader = new InputStreamReader(new FileInputStream("../Data/CirculationMarketValue.csv"), "UTF-8");
                br = new BufferedReader(reader);
                circulationMarketValue = new HashMap<>();
                String line;

                while ((line = br.readLine()) != null) {
                    String[] nameAndCode = line.split(",");
                    circulationMarketValue.put(nameAndCode[0], Double.valueOf(nameAndCode[1]));
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
                return circulationMarketValue.get(code);
            }
        }
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
            InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
            br = new BufferedReader(reader);
            stockPOs = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null) {
                String[] stockInfo = line.split("\t");
                StockPO stockPO = new StockPO(stockInfo[9], stockInfo[10], String.format("%6s", stockInfo[8]).replace(" ", "0"),
                        DateUtil.parseSlash(stockInfo[1]), Double.parseDouble(stockInfo[3]),
                        Double.parseDouble(stockInfo[4]), Double.parseDouble(stockInfo[2]),
                        Double.parseDouble(stockInfo[5]), Long.parseLong(stockInfo[6]), Double.parseDouble(stockInfo[7]));
                stockPOs.add(stockPO);
            }
            stockPOs.sort((stockPO1, stockPO2) -> {
                if(stockPO1.getDate().isEqual(stockPO2.getDate()))
                    return 0;
                return stockPO1.getDate().isBefore(stockPO2.getDate()) ? -1 : 1;
            });
        } catch (FileNotFoundException e) {
            System.out.println(fileName + " is not found");
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


    /**
     * 给定股票的名字和开始的日期
     * 读取从开始日期之后的所有股票数据
     * @param code
     * @param start
     * @return
     */
    private List<StockPO> generateStockPOsAfterStart(String code ,LocalDate start) {
        /**
         * change bby wsw
         * 为了更好的运用lambda表达式
         */
        List<StockPO> stockPOs = new ArrayList<>();
        BufferedReader br = null;
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(code), "UTF-8");
            br = new BufferedReader(reader);
            stockPOs = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null) {
                String[] stockInfo = line.split("\t");
                StockPO stockPO = new StockPO(stockInfo[9], stockInfo[10], String.format("%6s", stockInfo[8]).replace(" ", "0"),
                        DateUtil.parseSlash(stockInfo[1]), Double.parseDouble(stockInfo[3]),
                        Double.parseDouble(stockInfo[4]), Double.parseDouble(stockInfo[2]),
                        Double.parseDouble(stockInfo[5]), Long.parseLong(stockInfo[6]), Double.parseDouble(stockInfo[7]));

                /**
                 * 就不读剩下的东西了
                 */
                if(stockPO.getDate().isBefore(start)){
                    break;
                }

                stockPOs.add(stockPO);
            }
            stockPOs.sort((stockPO1, stockPO2) -> {
                if(stockPO1.getDate().isEqual(stockPO2.getDate()))
                    return 0;
                return stockPO1.getDate().isBefore(stockPO2.getDate()) ? -1 : 1;
            });
        } catch (FileNotFoundException e) {
            System.out.println(code + " is not found");
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
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            br = new BufferedReader(reader);

            //读出第一组数据作为基准数据
            String line = br.readLine();
            String[] stockInfo = line.split("\t");
            String name = stockInfo[9];
            String market = stockInfo[10];
            String code = String.format("%6s", stockInfo[8]).replace(" ", "0");
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
                stockInfo = line.split("\t");
                if(Long.parseLong(stockInfo[6]) != 0) {
                    count++;
                    if(Double.parseDouble(stockInfo[3]) > high_Price)
                        high_Price = Double.parseDouble(stockInfo[3]);
                    if(Double.parseDouble(stockInfo[4]) < low_Price)
                        low_Price = Double.parseDouble(stockInfo[4]);
                    open_Price = open_Price + Double.parseDouble(stockInfo[2]);
                    close_Price = close_Price + Double.parseDouble(stockInfo[5]);
                    volume = volume + Long.parseLong(stockInfo[6]);
                    adjCloseIndex = adjCloseIndex + Double.parseDouble(stockInfo[7]);
                }
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




    /**
     * add by wsw  新的读取文件的方法
     * 执行IO操作
     * 根据所给的文件名，读取出里面所有的股票数据，并转换成PO列表的形式传出去
     * @param fileName 所要查询的文件名
     * @return 若所给文件不存在，则返回null，该方法返回的List已经是按日期从小到大排好序的
     */
    private List<StockPO> generateStockPOsInJSE8(String fileName) {

        List<StockPO> stockPOs = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName), Charset.forName("UTF-8"))) {
            stockPOs = br.lines().map(t->{
                    String[] stockInfo = t.split("\t");
            StockPO stockPO = new StockPO(stockInfo[9], stockInfo[10], String.format("%6s", stockInfo[8]).replace(" ", "0"),
                    DateUtil.parseSlash(stockInfo[1]), Double.parseDouble(stockInfo[3]),
                    Double.parseDouble(stockInfo[4]), Double.parseDouble(stockInfo[2]),
                    Double.parseDouble(stockInfo[5]), Long.parseLong(stockInfo[6]), Double.parseDouble(stockInfo[7]));
                    return stockPO;
            }).collect(Collectors.toList());
            br.close();

//            stockPOs.sort((stockPO1, stockPO2) -> {
//                if(stockPO1.getDate().isEqual(stockPO2.getDate()))
//                    return 0;
//                return stockPO1.getDate().isBefore(stockPO2.getDate()) ? -1 : 1;
//            });

            Collections.reverse(stockPOs);

        } catch (IOException e) {
            System.out.println(fileName + " is not found");
        }finally {
            return stockPOs;
        }
    }



    /**
     * add by wsw
     * 执行IO操作
     * 获取非交易日的日期信息
     * @return 若所给文件不存在，则返回null，该方法返回的List已经是按日期从小到大排好序的
     */
    private List<LocalDate> getNoneTradeDates() {

        String fileName ="../Data/NoneDate.txt";
        List<LocalDate> dateList = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName), Charset.forName("UTF-8"))) {
            System.out.println("none     here");
            dateList = br.lines().map(t->DateUtil.parseLine(t)).collect(Collectors.toList());
            br.close();

        } catch (IOException e) {
            System.out.println(fileName + " is not found");
        }finally {
//            System.out.println(DateUtil.parseLine("1-23-12"));
//            System.out.println("begin   ");
//            dateList.stream().forEach(t-> System.out.println(t));
//            System.out.println("end");
            return dateList;
        }
    }
}
