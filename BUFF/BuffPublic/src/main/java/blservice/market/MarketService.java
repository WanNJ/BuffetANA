package blservice.market;

import blservice.exception.DateIndexException;
import vo.KLinePieceVO;
import vo.MarketStockDetailVO;
import vo.StockVolVO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by slow_time on 2017/3/12.
 */
public interface MarketService extends Remote {

    /**
     * 获得大盘的日K线图
     * @param beginDate
     * @param endDate
     * @return
     * @throws DateIndexException
     * @throws RemoteException
     */
    List<KLinePieceVO> getMarketDailyKLine(LocalDate beginDate, LocalDate endDate) throws DateIndexException, RemoteException;

    /**
     * 获得大盘的日交易量
     * @param beginDate
     * @param endDate
     * @return
     * @throws DateIndexException
     * @throws RemoteException
     */
    List<StockVolVO> getMarketVol(LocalDate beginDate, LocalDate endDate) throws DateIndexException, RemoteException;

    /**
     * 获得所有股票的大盘显示信息
     * @return
     * @throws RemoteException
     */
    List<MarketStockDetailVO> getMarketStockDetailVO() throws RemoteException;

    /**
     * 历史查询中根据股票编号获得股票详细信息
     * @param code 股票的编号
     * @return
     * @throws RemoteException
     */
    MarketStockDetailVO  getHistoryStockDetailVO(String code) throws RemoteException;
}
