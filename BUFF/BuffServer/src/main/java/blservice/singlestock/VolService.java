package blservice.singlestock;

import blservice.exception.DateIndexException;
import vo.StockVolVO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by slow_time on 2017/3/5.
 */
public interface VolService extends Remote {

    /**
     *
     * @param code 个股的编码
     * @param beginDate
     * @param endDate
     * @return
     */
    List<StockVolVO> getStockVol(String code, LocalDate beginDate, LocalDate endDate) throws DateIndexException, RemoteException;
}
