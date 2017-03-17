package singlestock;

import blservice.exception.DateIndexException;
import blserviceimpl.singlestock.KLineServiceImpl;
import factroy.DAOFactoryServiceImpl_Stub;
import org.junit.Before;
import org.junit.Test;
import vo.KLinePieceVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Accident on 2017/3/17.
 */
public class KLineServiceImplTest {
    KLineServiceImpl kLineService;

    @Before
    public void setup() {
        kLineService = KLineServiceImpl.K_LINE_SERVICE;
        kLineService.setTest(new DAOFactoryServiceImpl_Stub());
    }

    @Test
    public void getDailyKLine() throws DateIndexException, RemoteException {
        List<KLinePieceVO> list = kLineService.getDailyKLine("1", LocalDate.of(1997, 2, 6), LocalDate.of(1997, 2, 7));
        KLinePieceVO vOne = list.get(0);
        KLinePieceVO vTwo = list.get(1);
        assertEquals(LocalDate.of(1997, 2, 6), vOne.date);
        assertEquals(8.15, vOne.closePrice, 0.01);
        assertEquals(LocalDate.of(1997, 2, 7), vTwo.date);
        assertEquals(8.07, vTwo.closePrice, 0.01);
    }
}
