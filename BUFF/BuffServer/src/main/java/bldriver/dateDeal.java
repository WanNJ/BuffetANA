package bldriver;

import dataserviceimpl.singlestock.StockDAOImpl;
import po.StockPO;
import util.DateUtil;
import util.RunTimeSt;

import java.io.File;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by wshwbluebird on 2017/4/5.
 */
public class dateDeal {
    public static void main(String[] args) {
        RunTimeSt.Start();
        LocalDate start = LocalDate.of(2012,1,1);
        LocalDate end = LocalDate.of(2015,1,1);

        List<LocalDate>  dateList =  new ArrayList<>();

        for(LocalDate date = start ; !date.isAfter(end) ; date  =date.plusDays(1)){
            if(isthere(date))
            dateList.add(date);
        }

        dateList.stream().forEach(t-> System.out.println(t));
        RunTimeSt.getRunTime("end");

    }


    private static boolean isthere(LocalDate date){
        String timeFile = "../Data/Time/" + DateUtil.formatLine(date) + ".csv";
        File file = new File(timeFile);
        if(!file.exists())  return  false;

        StockDAOImpl stockDAO = StockDAOImpl.STOCK_DAO_IMPL;
        List<StockPO> list=  stockDAO.getStockInfoByDate(date);
        list = list.stream().filter(t->t.getVolume()!=0).collect(Collectors.toList());
        return list.size()==0;


    }



}
