package dataservice.stockmap;

import po.StockNameAndCodePO;

import java.util.List;

/**
 * 将所有股票的名字与它的编码一一对应
 * Created by slow_time on 2017/3/5.
 */
public interface StockNameToCodeDAO {

    List<StockNameAndCodePO> getNameToCodeMap();

    /**
     * 获得主板所有股票的名称以及代码
     * @return
     */
    List<StockNameAndCodePO> getMainBoardStock();

    /**
     * 获得创业板所有股票的名称以及代码
     * @return
     */
    List<StockNameAndCodePO> getSecondBoardStock();

    /**
     * 获得中小板所有股票的名称以及代码
     * @return
     */
    List<StockNameAndCodePO> getSMEBoardStock();

    /**
     * 获得该行业的所有股票的名称以及代码
     * @param industry
     * @return
     */
    List<StockNameAndCodePO> getSameIndustryStocks(String industry);
}
