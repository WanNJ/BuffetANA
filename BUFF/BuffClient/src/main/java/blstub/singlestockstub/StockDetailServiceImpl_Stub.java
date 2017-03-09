package blstub.singlestockstub;

import blservice.singlestock.StockDetailService;
import vo.StockBriefInfoVO;
import vo.StockDetailVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slow_time on 2017/3/8.
 */
public class StockDetailServiceImpl_Stub implements StockDetailService {
    @Override
    public StockDetailVO getSingleStockDetails(String code, LocalDate date) {
        return new StockDetailVO(11.25, 10.92, 11.02, 11.16, 41362100, 11.16);
    }

    @Override
    public List<StockBriefInfoVO> getStockBriefInfo(String code) throws RemoteException {
        List<StockBriefInfoVO> stockBriefInfoVOs = new ArrayList<>();
        StockBriefInfoVO stockBriefInfoVO1 = new StockBriefInfoVO("深发展Ａ", LocalDate.of(2014,4,29), "1", 11.16, 2);
        StockBriefInfoVO stockBriefInfoVO2 = new StockBriefInfoVO("深发展Ａ", LocalDate.of(2014,4,28), "1", 11.03, -1);
        StockBriefInfoVO stockBriefInfoVO3 = new StockBriefInfoVO("深发展Ａ", LocalDate.of(2014,4,27), "1", 11.25, 3);
        StockBriefInfoVO stockBriefInfoVO4 = new StockBriefInfoVO("深发展Ａ", LocalDate.of(2014,4,26), "1", 11.23, -1);
        StockBriefInfoVO stockBriefInfoVO5 = new StockBriefInfoVO("深发展Ａ", LocalDate.of(2014,4,25), "1", 11.3, 1);
        StockBriefInfoVO stockBriefInfoVO6 = new StockBriefInfoVO("深发展Ａ", LocalDate.of(2014,4,24), "1", 11.06, -3);
        StockBriefInfoVO stockBriefInfoVO7 = new StockBriefInfoVO("深发展Ａ", LocalDate.of(2014,4,23), "1", 10.69, -4);
        StockBriefInfoVO stockBriefInfoVO8 = new StockBriefInfoVO("深发展Ａ", LocalDate.of(2014,4,22), "1", 10.8, 1);
        StockBriefInfoVO stockBriefInfoVO9 = new StockBriefInfoVO("深发展Ａ", LocalDate.of(2014,4,21), "1", 10.9, 1);
        stockBriefInfoVOs.add(stockBriefInfoVO1);
        stockBriefInfoVOs.add(stockBriefInfoVO2);
        stockBriefInfoVOs.add(stockBriefInfoVO3);
        stockBriefInfoVOs.add(stockBriefInfoVO4);
        stockBriefInfoVOs.add(stockBriefInfoVO5);
        stockBriefInfoVOs.add(stockBriefInfoVO6);
        stockBriefInfoVOs.add(stockBriefInfoVO7);
        stockBriefInfoVOs.add(stockBriefInfoVO8);
        stockBriefInfoVOs.add(stockBriefInfoVO9);

        return stockBriefInfoVOs;
    }
}
