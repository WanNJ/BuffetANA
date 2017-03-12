package blservice.thermometer;

import blservice.exception.DateIndexException;
import util.DateRange;
import vo.LongPeiceVO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by zjy on 2017/3/5.
 * @author zjy
 */
public interface ThermometerService extends Remote{
    /**
     * 获得日期范围内的整个股市的每日交易量
     * @param dateRange 日期范围
     * @return 按从早到晚，数组的每个元素代表某天的交易量，如查询2006.2.2到2006.2.4，则list.get(1)代表2006.2.3的交易量
     */
    List<LongPeiceVO> getTradingVolume (DateRange dateRange) throws RemoteException;

    /**
     * 获得日期范围内的目标股票的每日交易量
     * @param dateRange 日期范围
     * @param shareID 股票的代码
     * @return 按从早到晚，数组的每个元素代表某天的交易量，如查询2006.2.2到2006.2.4，则list.get(1)代表2006.2.3的交易量
     */
    List<LongPeiceVO> getTradingVolume (DateRange dateRange,String shareID) throws RemoteException ;

    /**
     * 返回日期内每天的涨停股票数
     * @param dateRange 日期范围
     * @return 按从早到晚，数组的每个元素代表某天的交易量，如查询2006.2.2到2006.2.4，则list.get(1)代表2006.2.3的量
     */
    List<LongPeiceVO> getLimitUpNum (DateRange dateRange)throws RemoteException ;

    /**
     * 返回日期内每天的跌停股票数
     * @param dateRange 日期范围
     * @return 按从早到晚，数组的每个元素代表某天的交易量，如查询2006.2.2到2006.2.4，则list.get(1)代表2006.2.3的量
     */
    List<LongPeiceVO> getLimitDownNum (DateRange dateRange)throws RemoteException ;

    /**
     * 返回日期内每天的涨幅超过5%的股票数
     * @param dateRange 日期范围
     * @return 按从早到晚，数组的每个元素代表某天的交易量，如查询2006.2.2到2006.2.4，则list.get(1)代表2006.2.3的量
     */
    List<LongPeiceVO> getRiseOver5Num (DateRange dateRange) throws RemoteException;

    /**
     * 返回日期内每天的跌幅超过5%的股票数
     * @param dateRange 日期范围
     * @return 按从早到晚，数组的每个元素代表某天的交易量，如查询2006.2.2到2006.2.4，则list.get(1)代表2006.2.3的量
     */
    List<LongPeiceVO> getFallOver5Num (DateRange dateRange)throws RemoteException;

    /**
     * 返回日期内每天的 (开盘‐收盘)> 5 % *上一个交易日收盘价 的股票个数
     * @param dateRange 日期范围
     * @return 按从早到晚，数组的每个元素代表某天的交易量，如查询2006.2.2到2006.2.4，则list.get(1)代表2006.2.3的量
     */
    List<LongPeiceVO> getRiseOver5ThanLastDayNum (DateRange dateRange) throws RemoteException;

    /**
     * 返回日期内每天的 (开盘‐收盘)<- 5 % *上一个交易日收盘价 的股票个数
     * @param dateRange 日期范围
     * @return 按从早到晚，数组的每个元素代表某天的交易量，如查询2006.2.2到2006.2.4，则list.get(1)代表2006.2.3的量
     */
    List<LongPeiceVO> getFallOver5ThanLastDayNum (DateRange dateRange) throws RemoteException;
}
