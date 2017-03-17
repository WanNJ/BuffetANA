package singlestock;

import blservice.exception.DateIndexException;
import blserviceimpl.singlestock.VolServiceImpl;
import factroy.DAOFactoryServiceImpl_Stub;
import org.junit.Before;
import org.junit.Test;
import vo.StockVolVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Accident on 2017/3/17.
 */
public class VolServiceImplTest {
    VolServiceImpl volService;

    @Before
    public void setup() {
        volService = VolServiceImpl.VOL_SERVICE;
        volService.setTest(new DAOFactoryServiceImpl_Stub());
    }

    @Test
    public void getStockVol() throws DateIndexException, RemoteException {
        List<StockVolVO> list = volService.getStockVol("1" , LocalDate.of(1997, 2, 6), LocalDate.of(1997, 2, 7));
        StockVolVO vOne = list.get(0);
        StockVolVO vTwo = list.get(1);
        assertEquals(815, vOne.vol);
        assertEquals(807, vTwo.vol);
    }
}
