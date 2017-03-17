package singlestock;

import blserviceimpl.singlestock.MALineServiceImpl;
import factroy.DAOFactoryServiceImpl_Stub;
import org.junit.Before;
import org.junit.Test;
import vo.MAPieceVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Accident on 2017/3/17.
 */
public class MALineServiceImplTest {
    MALineServiceImpl maLineService;

    @Before
    public void setup() {
        maLineService = MALineServiceImpl.MA_LINE_SERVICE;
        maLineService.setTest(new DAOFactoryServiceImpl_Stub());
    }

    @Test
    public void getMAInfo() throws RemoteException {
        List<MAPieceVO> list = maLineService.getMAInfo("1", LocalDate.of(1997, 2, 6), LocalDate.of(1997, 2, 7));
        MAPieceVO vOne = list.get(0);
        MAPieceVO vTwo = list.get(1);
        assertEquals(LocalDate.of(1997, 2, 6), vOne.date);
        assertEquals(LocalDate.of(1997, 2, 7), vTwo.date);
        assertEquals(0.0, vOne.MA5, 0.01);
        assertEquals(0.0, vTwo.MA5, 0.01);
    }
}
