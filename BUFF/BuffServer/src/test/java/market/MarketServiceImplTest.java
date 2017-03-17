package market;

import blservice.exception.DateIndexException;
import blserviceimpl.market.MarketServiceImpl;
import factroy.DAOFactoryServiceImpl_Stub;
import org.junit.Before;
import org.junit.Test;
import vo.KLinePieceVO;
import vo.MarketStockDetailVO;
import vo.StockVolVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Accident on 2017/3/17.
 */
public class MarketServiceImplTest {
    MarketServiceImpl marketService;

    @Before
    public void setup() {
        marketService = MarketServiceImpl.MARKET_SERVICE;
        marketService.setTest(new DAOFactoryServiceImpl_Stub());
    }

    @Test
    public void getMarketDailyKLine() throws DateIndexException, RemoteException {
        List<KLinePieceVO> list = marketService.getMarketDailyKLine(LocalDate.of(1997, 2, 6), LocalDate.of(1997, 2, 7));
        assertEquals(LocalDate.of(1997, 2, 6), list.get(0).date);
        assertEquals(11.25, list.get(0).highPrice, 0.01);
        assertEquals(10.92, list.get(0).lowPrice, 0.01);
        assertEquals(11.02, list.get(0).openPrice, 0.01);
        assertEquals(11.16, list.get(0).closePrice, 0.01);
    }

    @Test
    public void getMarketVol() throws DateIndexException, RemoteException {
        List<StockVolVO> list = marketService.getMarketVol(LocalDate.of(1997, 2, 6), LocalDate.of(1997, 2, 7));
        StockVolVO vo = list.get(0);
        assertEquals(LocalDate.of(1997, 2, 6), vo.date);
        assertEquals(41362100, vo.vol);
        assertEquals(false, vo.openAboveClose);
    }


    @Test
    public void getMarketStockDetailVO() throws RemoteException {
        List<MarketStockDetailVO> list = marketService.getMarketStockDetailVO();
        MarketStockDetailVO vo = list.get(0);
        assertEquals("1", vo.code);
        assertEquals(null, vo.name);
        assertEquals(8.7, vo.currentPrice, 0.01);
        assertEquals(0.0, vo.changeValue, 0.01);
    }

    @Test
    public void getHistoryStockDetailVO() throws RemoteException {
        MarketStockDetailVO vo = marketService.getHistoryStockDetailVO("1");
        assertEquals("1", vo.code);
        assertEquals(null, vo.name);
        assertEquals(8.7, vo.currentPrice, 0.01);
    }
}
