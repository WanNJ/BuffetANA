package blserviceimpl.market;

import blservice.exception.DateIndexException;
import blservice.market.MarketService;
import dataservice.singlestock.StockDAO;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;
import po.StockPO;
import vo.KLinePieceVO;
import vo.MarketStockDetailVO;
import vo.StockVolVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by slow_time on 2017/3/12.
 */
public enum MarketServiceImpl implements MarketService {
    MARKET_SERVICE;

    private StockDAO stockDAO;
    private List<StockPO> stockPOs;
    private DAOFactoryService factory;

    MarketServiceImpl() {
        factory = new DAOFactoryServiceImpl();
        stockDAO = factory.createStockDAO();

    }

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
}
