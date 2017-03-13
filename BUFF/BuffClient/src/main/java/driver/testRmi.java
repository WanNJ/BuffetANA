package driver;

import blservice.exception.RangeException;
import blservice.singlestock.MALineService;
import blservice.thermometer.ThermometerService;
import factory.BlFactoryService;
import factory.BlFactoryServiceImpl;
import runner.ClientRunner;
import util.DateRange;
import vo.LongPeiceVO;
import vo.MAPieceVO;
import vo.StockVolVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/3/12.
 */
public class testRmi {
    public static void main(String[] args) throws RemoteException, RangeException {
        ClientRunner cr = new ClientRunner();

        BlFactoryService factory  =new BlFactoryServiceImpl();



//        MALineService maLineService = factory.createMALineService();
//        List<MAPieceVO> list = maLineService.getMAInfo("1", LocalDate.of(2009,12,1),LocalDate.of(2009,12,10));
//
//       list.forEach(t->System.out.println(t.MA60+"    "+t.date));
        ThermometerService thermometerService = factory.createThermometerService();

        //List<StockVolVO> list = thermometerService.getTradingVolume(new DateRange(LocalDate.of(2010,11,24),LocalDate.of(2010,11,28)),"1");
//      List<Long> list = thermometerService.getRiseOver5Num(new DateRange(LocalDate.of(2010,11,24),LocalDate.of(2010,11,28)));
//      List<LongPeiceVO> list = thermometerService.getRiseOver5ThanLastDayNum(new DateRange(LocalDate.of(2010,11,24),LocalDate.of(2010,12,1)));
        //System.out.println(list.size());
       // list.forEach(t->System.out.println(t.localDate+"  "+t.amount));


    }
}
