package bldriver;

import blserviceimpl.singlestock.MALineServiceImpl;
import dataservicestub.StockDaoImpl_stub;
import vo.MAPieceVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/3/14.
 */
public class HistoryTestDriver {
    public static void main(String[] args) throws RemoteException {
        List<String> list = new ArrayList<>();
        list.add("aa") ;
        list.add("bb") ;
        list.add("bb") ;
        list.add("cc") ;
        list.add("aa") ;
        list.add("dd") ;
        list.add("ee") ;
        list.stream().forEach(t-> System.out.println(t));
        System.out.println();
        Collections.reverse(list);
        list.stream().distinct().forEach(t-> System.out.println(t));
    }
}
