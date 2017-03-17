package singlestock;

import blserviceimpl.singlestock.StockDetailServiceImpl;
import factroy.DAOFactoryServiceImpl_Stub;
import org.junit.Before;
import org.junit.Test;
import vo.StockBriefInfoVO;
import vo.StockDetailVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Accident on 2017/3/17.
 */
public class StockDetailServiceImplTest {
    StockDetailServiceImpl stockDetailService;

    @Before
    public void setup() {
        stockDetailService = StockDetailServiceImpl.STOCK_DETAIL_SERVICE;
        stockDetailService.setTest(new DAOFactoryServiceImpl_Stub());
    }

    @Test
    public void getSingleStockDetails()  throws RemoteException {
        StockDetailVO vo = stockDetailService.getSingleStockDetails("1", LocalDate.of(1997, 2, 6));
        assertEquals(8.15, vo.closePrice, 0.01);
        assertEquals(815, vo.vol, 0.01);
    }

    @Test
    public void getStockBriefInfo() throws RemoteException {
        List<StockBriefInfoVO> list = stockDetailService.getStockBriefInfo("1");
        double close[] = {8.07, 8.84, 8.10, 8.40, 9.10, 9.20, 9.10, 8.95, 8.70};
        for(int i = 0; i < 9; i++) {
            assertEquals(list.get(i).closePrice, close[i], 0.01);
        }
    }
}
