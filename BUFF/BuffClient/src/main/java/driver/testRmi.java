package driver;

import blservice.singlestock.MALineService;
import factory.BlFactoryService;
import factory.BlFactoryServiceImpl;
import runner.ClientRunner;
import vo.MAPieceVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/3/12.
 */
public class testRmi {
    public static void main(String[] args) throws RemoteException {
        ClientRunner cr = new ClientRunner();

        BlFactoryService factory  =new BlFactoryServiceImpl();
        MALineService maLineService = factory.createMALineService();
        List<MAPieceVO> list = maLineService.getMAInfo("1", LocalDate.of(2009,12,1),LocalDate.of(2009,12,10));

       list.forEach(t->System.out.println(t.MA60+"    "+t.date));


    }
}
