package blserviceimpl.singlestock;

import blservice.singlestock.BenchStockService;
import blserviceimpl.util.PO2VOUtil;
import dataservice.singlestock.StockDAO;
import dataservice.stockmap.StockNameToCodeDAO;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;
import po.StockNameAndCodePO;
import po.StockPO;
import vo.MarketStockDetailVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by slow_time on 2017/4/9.
 */
public enum BenchStockServiceImpl implements BenchStockService {
    BENCH_STOCK_SERVICE;

    private StockNameToCodeDAO stockNameToCodeDAO;
    private StockDAO stockDAO;
    private List<StockNameAndCodePO> mainBoardStockPOs;
    private List<StockNameAndCodePO> secondBoardStockPOs;
    private List<StockNameAndCodePO> SMEBoardStockPOs;
    private DAOFactoryService factory;

    BenchStockServiceImpl() {
        factory = new DAOFactoryServiceImpl();
        stockNameToCodeDAO = factory.createStockNameToCodeDAO();
        stockDAO = factory.createStockDAO();
        mainBoardStockPOs = stockNameToCodeDAO.getMainBoardStock();
        secondBoardStockPOs = stockNameToCodeDAO.getSecondBoardStock();
        SMEBoardStockPOs = stockNameToCodeDAO.getSMEBoardStock();
    }

    @Override
    public List<MarketStockDetailVO> getMainBoardStock() {
        List<MarketStockDetailVO> marketStockDetailVOS = new ArrayList<>();
        mainBoardStockPOs.forEach(stockNameAndCodePO -> {
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
                        marketStockDetailVOS.add(marketStockDetailVO);
                        break;
                    }
                }
            }
        });
        return marketStockDetailVOS;
    }

    @Override
    public List<MarketStockDetailVO> getSecondBoardStock() {
        List<MarketStockDetailVO> marketStockDetailVOS = new ArrayList<>();
        secondBoardStockPOs.forEach(stockNameAndCodePO -> {
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
                        marketStockDetailVOS.add(marketStockDetailVO);
                        break;
                    }
                }
            }
        });
        return marketStockDetailVOS;

    }

    @Override
    public List<MarketStockDetailVO> getSMEBoardStock() {
        List<MarketStockDetailVO> marketStockDetailVOS = new ArrayList<>();
        SMEBoardStockPOs.forEach(stockNameAndCodePO -> {
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
                        marketStockDetailVOS.add(marketStockDetailVO);
                        break;
                    }
                }
            }
        });
        return marketStockDetailVOS;
    }
}
