package dataservice.singlestock;

import po.StockPO;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by slow_time on 2017/3/5.
 */
public interface StockDAO {

    /**
     * 获得具体某一支股票的每一天的数据
     * @param code 个股的编码
     * @return 如果不存在这支股票，或者code为null则返回null，若存在，则返回每一天数据的列表，该方法返回的List已经是按日期从小到大排好序的
     */
    List<StockPO> getStockInfoByCode(String code);

    /**
     * 获得具体某一天的所有的股票的数据
     * @param date 日期必须在2014年4月29日与2005年2月1日之间
     * @return 若日期不在2014年4月29日与2005年2月1日之间，或日期为null则返回null，该方法返回的List已经是按日期从小到大排好序的
     */
    List<StockPO> getStockInfoByDate(LocalDate date);

    /**
     * 获得每只股票的大盘信息
     * @return
     */
    List<StockPO> getMarketStockInfo();


    /**
     * 获得日期区间范围内的  股票信息
     * 如果没有   则尽可能给
     * 包涵两端
     * @param code
     * @param begin
     * @param end
     * @return
     */
    List<StockPO>  getStockInFoInRangeDate(String code , LocalDate begin , LocalDate end);
}
