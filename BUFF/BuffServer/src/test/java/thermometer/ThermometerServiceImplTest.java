package thermometer;

import blservice.exception.RangeException;
import blserviceimpl.thermometer.ThermometerServiceImpl;
import factroy.DAOFactoryServiceImpl_Stub;
import org.junit.Before;
import org.junit.Test;
import util.DateRange;
import vo.LongPeiceVO;
import vo.StockVolVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Accident on 2017/3/17.
 */
public class ThermometerServiceImplTest {
    ThermometerServiceImpl thermometerService;
    DateRange range = new DateRange(LocalDate.of(1997, 2, 6), LocalDate.of(1997, 2, 6));

    public ThermometerServiceImplTest() throws RangeException {
    }

    @Before
    public void setup() {
        thermometerService = ThermometerServiceImpl.THERMOMETER_SERVCE;
        thermometerService.setTest(new DAOFactoryServiceImpl_Stub());
    }

    @Test
    public void getTradingVolume() throws RemoteException, RangeException {
        List<StockVolVO> list = thermometerService.getTradingVolume(range);
        StockVolVO vo = list.get(0);
        assertEquals(415, vo.vol);
    }

    @Test
    public void getLimitUpNum() throws RemoteException {
        List<LongPeiceVO> list = thermometerService.getLimitUpNum(range);
        LongPeiceVO vo = list.get(0);
        assertEquals(LocalDate.of(1997, 2, 6), vo.localDate);
        assertEquals(0, vo.amount);
    }

    @Test
    public void getLimitDownNum()throws RemoteException {
        List<LongPeiceVO> list = thermometerService.getLimitUpNum(range);
        LongPeiceVO vo = list.get(0);
        assertEquals(LocalDate.of(1997, 2, 6), vo.localDate);
        assertEquals(0, vo.amount);
    }

    @Test
    public void getRiseOver5Num()throws RemoteException {
        List<LongPeiceVO> list = thermometerService.getRiseOver5Num(range);
        LongPeiceVO vo = list.get(0);
        assertEquals(LocalDate.of(1997, 2, 6), vo.localDate);
        assertEquals(0, vo.amount);
    }

    @Test
    public void getFallOver5Num()throws RemoteException {
        List<LongPeiceVO> list = thermometerService.getFallOver5Num(range);
        LongPeiceVO vo = list.get(0);
        assertEquals(LocalDate.of(1997, 2, 6), vo.localDate);
        assertEquals(0, vo.amount);
    }

    @Test
    public void getRiseOver5ThanLastDayNum()throws RemoteException {
        List<LongPeiceVO> list = thermometerService.getRiseOver5ThanLastDayNum(range);
        LongPeiceVO vo = list.get(0);
        assertEquals(LocalDate.of(1997, 2, 6), vo.localDate);
        assertEquals(0, vo.amount);
    }

    @Test
    public void getFallOver5ThanLastDayNum()throws RemoteException {
        List<LongPeiceVO> list = thermometerService.getRiseOver5ThanLastDayNum(range);
        LongPeiceVO vo = list.get(0);
        assertEquals(LocalDate.of(1997, 2, 6), vo.localDate);
        assertEquals(0, vo.amount);
    }

}
