package blservice.singlestock;

import vo.StockNameAndCodeVO;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by slow_time on 2017/3/9.
 */
public interface AllStockService {

    List<StockNameAndCodeVO> getAllStock() throws RemoteException;
}
