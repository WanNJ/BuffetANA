package blservice.singlestock;

import blservice.exception.DateIndexException;
import vo.KLinePieceVO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by slow_time on 2017/3/5.
 */
public interface KLineService extends Remote {

    /**
     * 用户进行具体个股日K线图搜索时，调用的方法
     * @param code 个股的编码
     * @param beginDate 日K线图的开始日期
     * @param endDate 日K线图的结束日期
     * @return
     */
    List<KLinePieceVO> getDailyKLine(String code, LocalDate beginDate, LocalDate endDate) throws DateIndexException, RemoteException;

    /**
     * 用户进行具体个股周K线图搜索时，调用的方法
     * @param code 个股的编码
     * @param beginDate 周K线图的开始日期
     * @param endDate 周K线图的结束日期
     * @return
     */
    List<KLinePieceVO> getWeeklyKLine(String code, LocalDate beginDate, LocalDate endDate) throws RemoteException;

    /**
     * 用户进行具体个股月K线图搜索时，调用的方法
     * @param code 个股的编码
     * @param beginDate 月K线图的开始日期
     * @param endDate 月K线图的结束日期
     * @return
     */
    List<KLinePieceVO> getMonthlyKLine(String code, LocalDate beginDate, LocalDate endDate) throws RemoteException;
}
