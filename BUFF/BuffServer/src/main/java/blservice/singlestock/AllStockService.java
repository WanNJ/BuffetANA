package blservice.singlestock;

import vo.StockNameAndCodeVO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by slow_time on 2017/3/9.
 */
public interface AllStockService extends Remote {

    List<StockNameAndCodeVO> getAllStock() throws RemoteException;
}
