package blserviceimpl.market;

import blservice.exception.DateIndexException;
import blservice.market.MarketService;
import blserviceimpl.util.PO2VOUtil;
import dataservice.singlestock.StockDAO;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;
import po.StockPO;
import util.DateUtil;
import vo.KLinePieceVO;
import vo.MarketStockDetailVO;
import vo.StockVolVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        stockPOs = stockDAO.getMarketStockInfo();
    }

    @Override
    public List<KLinePieceVO> getMarketDailyKLine(LocalDate beginDate, LocalDate endDate) throws DateIndexException, RemoteException {
        if(beginDate.isAfter(endDate))
            throw new DateIndexException(beginDate, endDate);
        return stockPOs.stream().filter(stockPO -> DateUtil.isBetween(stockPO.getDate(), beginDate, endDate)).map(stockPO -> PO2VOUtil.stockPO2KLinePieceVO(stockPO)).collect(Collectors.toList());
    }

    @Override
    public List<StockVolVO> getMarketVol(LocalDate beginDate, LocalDate endDate) throws DateIndexException, RemoteException {
        if(beginDate.isAfter(endDate))
            throw new DateIndexException(beginDate, endDate);
        return stockPOs.stream().filter(stockPO -> DateUtil.isBetween(stockPO.getDate(), beginDate, endDate)).map(stockPO -> PO2VOUtil.stockPO2StockVolVO(stockPO)).collect(Collectors.toList());
    }

    @Override
    public List<MarketStockDetailVO> getMarketStockDetailVO() throws RemoteException {
        if(stockPOs != null && stockPOs.size() >= 1) {
            List<MarketStockDetailVO> marketStockDetailVOs = new ArrayList<>();
            StockPO stockPO1 = stockPOs.get(0);
            for(StockPO stockPO2 : stockPOs) {
                MarketStockDetailVO marketStockDetailVO = PO2VOUtil.stockPO2MarketStockDetailVO(stockPO1, stockPO2);
                if(marketStockDetailVO != null)
                {
                    marketStockDetailVOs.add(marketStockDetailVO);
                    stockPO1 = stockPO2;
                }
            }
            return marketStockDetailVOs;
        }
        return null;
    }
}
