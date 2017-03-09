package blstub.singlestockstub;

import blservice.exception.DateIndexException;
import blservice.singlestock.VolService;
import vo.StockVolVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wshwbluebird on 2017/3/8.
 */

/**
 * TODO 我不确定这个包是不是应该放在这里!!!!
 * 用来测试vol 图的 stub
 */
public class VolServiceImpl_Stub implements VolService{
    @Override
    public List<StockVolVO> getStockVol(String code, LocalDate beginDate, LocalDate endDate) throws DateIndexException, RemoteException {
        List<StockVolVO> stockVolVOs = new ArrayList<>();
        Random random = new Random(10);
        while(!beginDate.isAfter(endDate)) {
            StockVolVO stockVolVO = new StockVolVO(beginDate,1000+random.nextInt(100),random.nextBoolean());
            stockVolVOs.add(stockVolVO);
            beginDate = beginDate.plusDays(1);
        }

        return stockVolVOs;
    }
}
