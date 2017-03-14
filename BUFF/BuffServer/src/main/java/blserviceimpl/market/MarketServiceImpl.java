package blserviceimpl.market;

import blservice.exception.DateIndexException;
import blservice.market.MarketService;
import blserviceimpl.util.PO2VOUtil;
import dataservice.singlestock.StockDAO;
import dataservice.stockmap.StockNameToCodeDAO;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;
import po.StockNameAndCodePO;
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
    private StockNameToCodeDAO stockNameToCodeDAO;
    private List<StockPO> stockPOs = null;
    private List<MarketStockDetailVO> marketStockDetailVOs = null;
    private DAOFactoryService factory;

    MarketServiceImpl() {
        factory = new DAOFactoryServiceImpl();
        stockDAO = factory.createStockDAO();
        stockNameToCodeDAO = factory.createStockNameToCodeDAO();
    }

    @Override
    public List<KLinePieceVO> getMarketDailyKLine(LocalDate beginDate, LocalDate endDate) throws DateIndexException, RemoteException {
        if(stockPOs == null)
            stockPOs = stockDAO.getMarketStockInfo();
        if(beginDate.isAfter(endDate))
            throw new DateIndexException(beginDate, endDate);
        return stockPOs.stream().filter(stockPO -> DateUtil.isBetween(stockPO.getDate(), beginDate, endDate) && stockPO.getVolume() != 0).map(stockPO -> PO2VOUtil.stockPO2KLinePieceVO(stockPO)).collect(Collectors.toList());
    }

    @Override
    public List<StockVolVO> getMarketVol(LocalDate beginDate, LocalDate endDate) throws DateIndexException, RemoteException {
        if(stockPOs == null)
            stockPOs = stockDAO.getMarketStockInfo();
        if(beginDate.isAfter(endDate))
            throw new DateIndexException(beginDate, endDate);
        return stockPOs.stream().filter(stockPO -> DateUtil.isBetween(stockPO.getDate(), beginDate, endDate) && stockPO.getVolume() != 0).map(stockPO -> PO2VOUtil.stockPO2StockVolVO(stockPO)).collect(Collectors.toList());
    }

    @Override
    public List<MarketStockDetailVO> getMarketStockDetailVO() throws RemoteException {
        if(marketStockDetailVOs == null){
            List<StockNameAndCodePO> stockNameAndCodePOs = stockNameToCodeDAO.getNameToCodeMap();
            marketStockDetailVOs = new ArrayList<>();
            stockNameAndCodePOs.forEach(stockNameAndCodePO -> {
                List<StockPO> singleStockPOS = stockDAO.getStockInfoByCode(stockNameAndCodePO.getCode());
                //按日期从大到小排序
                singleStockPOS.sort((stockPO1, stockPO2) -> {
                    if(stockPO1.getDate().isEqual(stockPO2.getDate()))
                        return 0;
                    return stockPO1.getDate().isBefore(stockPO2.getDate()) ? 1 : -1;
                });
                if(singleStockPOS != null && singleStockPOS.size() >= 1) {
                    StockPO stockPO1 = singleStockPOS.get(0);
                    singleStockPOS.remove(0);
                    for(StockPO stockPO2 : singleStockPOS) {
                        MarketStockDetailVO marketStockDetailVO = PO2VOUtil.stockPO2MarketStockDetailVO(stockPO1, stockPO2);
                        if(marketStockDetailVO != null)
                        {
                            marketStockDetailVOs.add(marketStockDetailVO);
                            break;
                        }
                    }
                }
            });
        }
        return marketStockDetailVOs;
    }

    public static void main(String[] args) {
        MarketService marketService = MARKET_SERVICE;
        try {
            List<StockVolVO> stockVolVOS = marketService.getMarketVol(LocalDate.of(2014, 2, 3), LocalDate.of(2014, 2, 6));
            System.out.println(stockVolVOS.size());
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (DateIndexException e) {
            e.printStackTrace();
        }
    }
}
