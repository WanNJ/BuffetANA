package blstub.marketstub;

import blservice.exception.DateIndexException;
import blservice.market.MarketService;
import vo.KLinePieceVO;
import vo.MarketStockDetailVO;
import vo.StockVolVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by slow_time on 2017/3/12.
 */
public class MarketServiceImpl_Stub implements MarketService {
    @Override
    public List<KLinePieceVO> getMarketDailyKLine(LocalDate beginDate, LocalDate endDate) throws DateIndexException, RemoteException {
        return null;
    }

    @Override
    public List<StockVolVO> getMarketVol(LocalDate beginDate, LocalDate endDate) throws DateIndexException, RemoteException {
        return null;
    }

    @Override
    public List<MarketStockDetailVO> getMarketStockDetailVO() throws RemoteException {
        return null;
    }

    @Override
    public MarketStockDetailVO getHistoryStockDetailVO(String code) throws RemoteException {
        return null;
    }
}
