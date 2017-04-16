package blservice.singlestock;

import vo.MarketStockDetailVO;

import java.util.List;

/**
 * Created by slow_time on 2017/4/9.
 */
public interface BenchStockService {

    /**
     * 获得主板所有股票的名称以及代码
     * @return
     */
    List<MarketStockDetailVO> getMainBoardStock();

    /**
     * 获得创业板所有股票的名称以及代码
     * @return
     */
    List<MarketStockDetailVO> getSecondBoardStock();

    /**
     * 获得中小板所有股票的名称以及代码
     * @return
     */
    List<MarketStockDetailVO> getSMEBoardStock();
}
