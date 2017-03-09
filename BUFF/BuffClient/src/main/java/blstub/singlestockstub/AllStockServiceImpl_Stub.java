package blstub.singlestockstub;

import blservice.singlestock.AllStockService;
import vo.StockNameAndCodeVO;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slow_time on 2017/3/9.
 */
public class AllStockServiceImpl_Stub implements AllStockService {
    @Override
    public List<StockNameAndCodeVO> getAllStock() throws RemoteException {
        List<StockNameAndCodeVO> stockNameAndCodeVOs = new ArrayList<>();
        stockNameAndCodeVOs.add(new StockNameAndCodeVO("深发展Ａ", "1"));
        return stockNameAndCodeVOs;
    }
}
