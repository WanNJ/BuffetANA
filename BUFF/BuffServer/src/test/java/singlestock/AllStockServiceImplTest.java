package singlestock;

import blserviceimpl.singlestock.AllStockServiceImpl;
import factroy.DAOFactoryServiceImpl_Stub;
import org.junit.Before;
import org.junit.Test;
import po.StockNameAndCodePO;
import vo.StockNameAndCodeVO;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Accident on 2017/3/17.
 */
public class AllStockServiceImplTest {
    AllStockServiceImpl allStockService;

    @Before
    public void setup() {
        allStockService = AllStockServiceImpl.ALL_STOCK_SERVICE;
        allStockService.setTest(new DAOFactoryServiceImpl_Stub());
    }

    @Test
    public void getAllStock() throws RemoteException {
        List<StockNameAndCodeVO> stockNameAndCodeVOs = allStockService.getAllStock();
        StockNameAndCodeVO vo = stockNameAndCodeVOs.get(0);
        assertEquals("深发展A", vo.name);
        assertEquals("1", vo.code);
    }
}
