package blserviceimpl.thermometer;

import blservice.thermometer.ThermometerService;
import dataservice.singlestock.StockDAO;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;
import po.StockPO;
import util.DateRange;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by wshwbluebird on 2017/3/12.
 */
public enum ThermometerServiceImpl implements ThermometerService{
    THERMOMETER_SERVCE ;

    private StockDAO stockDAO;
    private List<StockPO> stockPOs;
    private DAOFactoryService factory;

    ThermometerServiceImpl() {
        factory = new DAOFactoryServiceImpl();
        stockDAO = factory.createStockDAO();
    }


    /**
     * 用于传递stub
     * @param stockDAO
     */
    public void setDao(StockDAO stockDAO){
        this.stockDAO = stockDAO;
    }



    /**
     * 获得日期范围内的整个股市的每日交易量
     * @param dateRange 日期范围
     * @return 按从早到晚，数组的每个元素代表某天的交易量，如查询2006.2.2到2006.2.4，则list.get(1)代表2006.2.3的交易量
     */
    @Override
    public List<Long> getTradingVolume(DateRange dateRange) {
        /**
         * 获取时间
         */
        LocalDate begin =  dateRange.getFrom();

        /**
         * 建立list
         */
        List<Long> list = new ArrayList<>();

        /**
         * 获取日期的流
         */
        Stream<LocalDate> stream = Stream.iterate(begin,date ->date.plusDays(1))
                .limit(dateRange.getRangeDays());

        /**
         *  计算
         *  获取列表
         */
        list =  stream.map((date)->
                (stockDAO.getStockInfoByDate(date).stream().map(t->t.getVolume())
                        .reduce((a,b)->a+b).orElseGet(()->Long.parseLong("0"))))
                .collect(Collectors.toList());


        /**
         * 返回数值
         */
        return list;
    }

    /**
     * 获得日期范围内的目标股票的每日交易量
     * @param dateRange 日期范围
     * @param shareID 股票的代码
     * @return 按从早到晚，数组的每个元素代表某天的交易量，如查询2006.2.2到2006.2.4，则list.get(1)代表2006.2.3的交易量
     * 有严重的问题   脏数据的处理有严重的问题
     */

    @Override
    public List<Long> getTradingVolume(DateRange dateRange, String shareID) {
        /**
         * 获取时间
         */
        LocalDate begin =  dateRange.getFrom();
        LocalDate end =  dateRange.getTo();


        /**
         * 建立list
         */
        List<Long> list = new ArrayList<>();

        /**
         * 获取单个股票数据
         */
        List<StockPO>  stockPOs = stockDAO.getStockInfoByCode(shareID);


        /**
         *  计算
         *  获取列表
         */
        list =  stockPOs.stream().filter(t->!(t.getDate().isAfter(end)||t.getDate().isBefore(begin)))
                .map(t->t.getVolume()).collect(Collectors.toList());


        /**
         * 返回数值
         */
        return list;
    }



    /**
     * 返回日期内每天的涨停股票数
     * @param dateRange 日期范围
     * @return 按从早到晚，数组的每个元素代表某天的交易量，如查询2006.2.2到2006.2.4，则list.get(1)代表2006.2.3的量
     * 暂时先不对 0成交量做顾虑
     */
    @Override
    public List<Long> getLimitUpNum(DateRange dateRange) {
        //不确定 四舍五入的误差
        return CompareWithHistory(dateRange,0.099);
    }


    /**
     * 返回日期内每天的跌停股票数
     * @param dateRange 日期范围
     * @return 按从早到晚，数组的每个元素代表某天的交易量，如查询2006.2.2到2006.2.4，则list.get(1)代表2006.2.3的量
     */
    @Override
    public List<Long> getLimitDownNum(DateRange dateRange) {
       return CompareWithHistory(dateRange,-0.099);
    }


    /**
     * 返回日期内每天的涨幅超过5%的股票数
     * @param dateRange 日期范围
     * @return 按从早到晚，数组的每个元素代表某天的交易量，如查询2006.2.2到2006.2.4，则list.get(1)代表2006.2.3的量
     */
    @Override
    public List<Long> getRiseOver5Num(DateRange dateRange){
        return CompareWithHistory(dateRange,0.05);
}


    /**
     * 返回日期内每天的跌幅超过5%的股票数
     * @param dateRange 日期范围
     * @return 按从早到晚，数组的每个元素代表某天的交易量，如查询2006.2.2到2006.2.4，则list.get(1)代表2006.2.3的量
     */
    @Override
    public List<Long> getFallOver5Num(DateRange dateRange) {
        return CompareWithHistory(dateRange,-0.05);
    }

    /**
     * 返回日期内每天的 (开盘‐收盘)> 5 % *上一个交易日收盘价 的股票个数
     * @param dateRange 日期范围
     * @return 按从早到晚，数组的每个元素代表某天的交易量，如查询2006.2.2到2006.2.4，则list.get(1)代表2006.2.3的量
     */
    @Override
    public List<Long> getRiseOver5ThanLastDayNum(DateRange dateRange) {
        return CompareWithOpenClose(dateRange,0.05);
    }



    /**
     * 返回日期内每天的 (开盘‐收盘)<- 5 % *上一个交易日收盘价 的股票个数
     * @param dateRange 日期范围
     * @return 按从早到晚，数组的每个元素代表某天的交易量，如查询2006.2.2到2006.2.4，则list.get(1)代表2006.2.3的量
     */
    @Override
    public List<Long> getFallOver5ThanLastDayNum(DateRange dateRange) {
        return CompareWithOpenClose(dateRange,-0.05);
    }






    /**
     * 用于计算股票涨跌幅度
     * @param last
     * @param curr
     * @return  double
     */
    private  double getChangeRatio(double last, double curr){
        if(last == 0)  return 0;
        return (curr-last)/last;
    }


    /**
     *
     * @param open
     * @param close
     * @param last
     * @return
     */
    private  double getChangeRatioLast(double open, double close, double last){
        if(last == 0)  return 0;
        return (close-open)/last;
    }



    /**
     * 复用处理比较不同天内所有股票的涨跌幅度
     * @param dateRange
     * @param rate    要达到涨跌幅度的目标
     * @return   List<Long>
     */
    private List<Long> CompareWithHistory(DateRange dateRange ,  double rate){
        LocalDate begin =  dateRange.getFrom();
        LocalDate end =  dateRange.getTo();


        /**
         * 建立list
         */
        List<Long> list = new ArrayList<>();

        /**
         * 获取循环个股票数据
         */
        Map<String,Double> lastClosePriceTable =new HashMap<>();

        List<StockPO>  dayList = stockDAO.getStockInfoByDate(begin.minusDays(5));
        lastClosePriceTable = dayList.stream().collect(Collectors.toMap
                (StockPO::getCode, StockPO::getClose_Price));


        /**
         *  为防止当天股票的值不存在于原来的数据中
         */

        for(LocalDate idate = begin.minusDays(4);idate.isBefore(begin);idate = idate.plusDays(1)){
            dayList = stockDAO.getStockInfoByDate(idate);
            if(dayList!=null && dayList.size()!=0) {
                for (StockPO po : dayList) {
                    String code = po.getCode();
                    lastClosePriceTable.put(code, po.getClose_Price());
                }
            }
        }

        /**
         *  计算
         *  获取列表
         */
        for(LocalDate idate = begin;!idate.isAfter(end);idate = idate.plusDays(1)){
            long count =  0;
            dayList = stockDAO.getStockInfoByDate(idate);
            if(dayList!=null && dayList.size()!=0) {
                for (StockPO po : dayList) {
                    String code = po.getCode();
                    if (lastClosePriceTable.containsKey(code)) {

                        if (getChangeRatio(lastClosePriceTable.get(code),po.getClose_Price())<=rate  && rate<0) {
                            count++;
                        }else if (getChangeRatio(lastClosePriceTable.get(code),po.getClose_Price())>=rate  && rate>0) {
                            count++;
                        }
                    }
                    lastClosePriceTable.put(code, po.getClose_Price());
                }
            }
            list.add(count);
        }

        /**
         * 返回数值
         */
        return list;
    }


    /**
     * 复用 close - open  ／ last  的历史比较接口
     * @param dateRange
     * @param rate
     * @return List<Long>
     */
    private List<Long> CompareWithOpenClose(DateRange dateRange ,  double rate){
        LocalDate begin =  dateRange.getFrom();
        LocalDate end =  dateRange.getTo();


        /**
         * 建立list
         */
        List<Long> list = new ArrayList<>();

        /**
         * 获取循环个股票数据
         */
        Map<String,Double> lastClosePriceTable =new HashMap<>();

        List<StockPO>  dayList = stockDAO.getStockInfoByDate(begin.minusDays(5));
        lastClosePriceTable = dayList.stream().collect(Collectors.toMap
                (StockPO::getCode, StockPO::getClose_Price));


        /**
         *  为防止当天股票的值不存在于原来的数据中
         */

        for(LocalDate idate = begin.minusDays(4);idate.isBefore(begin);idate = idate.plusDays(1)){
            dayList = stockDAO.getStockInfoByDate(idate);
            if(dayList!=null && dayList.size()!=0) {
                for (StockPO po : dayList) {
                    String code = po.getCode();
                    lastClosePriceTable.put(code, po.getClose_Price());
                }
            }
        }

        /**
         *  计算
         *  获取列表
         */
        for(LocalDate idate = begin;!idate.isAfter(end);idate = idate.plusDays(1)){
            long count =  0;
            dayList = stockDAO.getStockInfoByDate(idate);
            if(dayList!=null && dayList.size()!=0) {
                for (StockPO po : dayList) {
                    String code = po.getCode();
                    if (lastClosePriceTable.containsKey(code)) {

                        if (getChangeRatioLast(po.getOpen_Price(),po.getClose_Price(),lastClosePriceTable.get(code))<=rate
                                && rate<0) {
                            count++;
                        }else if (getChangeRatioLast(po.getOpen_Price(),po.getClose_Price(),lastClosePriceTable.get(code))>=rate
                                && rate>0) {
                            count++;
                        }
                    }
                    lastClosePriceTable.put(code, po.getClose_Price());
                }
            }
            list.add(count);
        }

        /**
         * 返回数值
         */
        return list;
    }






    /**
     * 用于计算股票是否涨停
     * @param last
     * @param curr
     * @return  boolean
     */
    private  boolean isUpEnough(double last, double curr){
        if(last == 0)  return false;
        return (curr-last)*10>=last*0.99;
    }


    /**
     * 用于计算股票是否跌停
     * @param last
     * @param curr
     * @return  boolean
     */
    private  boolean isDownEnough(double last, double curr){
        if(last == 0)  return false;
        return (last - curr)*10>=last*0.99;
    }



}
