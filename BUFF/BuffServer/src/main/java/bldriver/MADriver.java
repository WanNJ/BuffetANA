package bldriver;

import blservice.singlestock.MALineService;
import blserviceimpl.singlestock.MALineServiceImpl;
import dataservicestub.StockDaoImpl_stub;
import vo.MAPieceVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by wshwbluebird on 2017/3/12.
 */
public class MADriver {

    public static void main(String[] args) throws RemoteException {
        MALineServiceImpl maLineService = MALineServiceImpl.MA_LINE_SERVICE;
        maLineService.setDao(new StockDaoImpl_stub());
        List<MAPieceVO>  list = maLineService.getMAInfo("sd", LocalDate.of(2017,1,1),LocalDate.of(2017,1,8));
        System.out.println("size:  "+list.size());
        list.forEach(t-> System.out.println(t.MA5));
    }
}
