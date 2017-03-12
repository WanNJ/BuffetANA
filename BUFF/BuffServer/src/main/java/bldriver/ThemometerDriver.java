package bldriver;

import blservice.exception.RangeException;
import blservice.thermometer.ThermometerService;
import blserviceimpl.singlestock.MALineServiceImpl;
import blserviceimpl.thermometer.ThermometerServiceImpl;
import dataservicestub.StockDaoImpl_stub;
import util.DateRange;
import vo.MAPieceVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wshwbluebird on 2017/3/12.
 */
public class ThemometerDriver {

    public static void main(String[] args) throws RemoteException, RangeException {
        ThermometerServiceImpl thermometerService = ThermometerServiceImpl.THERMOMETER_SERVCE;
        //thermometerService.setDao(new StockDaoImpl_stub());
//      List<Long> list = thermometerService.getTradingVolume(new DateRange(LocalDate.of(1996,11,25),LocalDate.of(1996,11,27)));
//      List<Long> list = thermometerService.getTradingVolume(new DateRange(LocalDate.of(1996,11,24),LocalDate.of(1996,11,28)),"code");
        List<Long> list = thermometerService.getRiseOver5Num(new DateRange(LocalDate.of(2010,11,24),LocalDate.of(2010,11,28)));
        System.out.println(list.size());
        list.forEach(t->System.out.println(t));

        List<Long> list2 = thermometerService.getFallOver5Num(new DateRange(LocalDate.of(2010,11,24),LocalDate.of(2010,11,28)));
        System.out.println(list2.size());
        list2.forEach(t->System.out.println(t));



    }

}
