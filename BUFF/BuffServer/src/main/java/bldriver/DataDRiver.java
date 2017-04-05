package bldriver;

import blserviceimpl.singlestock.MALineServiceImpl;
import blserviceimpl.strategy.PickleData;
import dataservicestub.StockDaoImpl_stub;
import pick.PickStockServiceImpl;
import po.StockPO;
import util.DateUtil;
import vo.MAPieceVO;

import java.io.*;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wshwbluebird on 2017/3/15.
 */
public class DataDRiver {
    public static void main(String[] args) throws RemoteException {
//        String codeFile = "../Data/StockMap.csv";
//        generateStockPOs(codeFile);


        PickStockServiceImpl pickStockService = PickStockServiceImpl.PICK_STOCK_SERVICE;

        List<PickleData> list = pickStockService.seprateDaysByTrade(LocalDate.of(2013,1,1),LocalDate.of(2014,1,5),10);
        list.stream().forEach(t-> System.out.println(t.beginDate+" "+t.endDate));

    }


    private static void generateStockPOs(String fileName) {
        BufferedReader br = null;
        List<String> stringList = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = br.readLine()) != null) {
                String[] stockInfo = line.split(",");
                String temp = stockInfo[1]+"("+stockInfo[0]+")";
                stringList.add(temp);
            }
            //System.out.println(stringList.size());
            List<String> str =stringList.parallelStream().filter(t->t.startsWith("1")).collect(Collectors.toList());
            str.sort((a,b)->a.compareTo(b));
            str.stream().limit(5).forEach(t-> System.out.println(t));

        } catch (FileNotFoundException e) {
            System.out.println(fileName + " is not found");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
