package blserviceimpl.singlestock;

import blservice.singlestock.StockDetailService;
import blserviceimpl.util.PO2VOUtil;
import dataservice.singlestock.StockDAO;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;
import po.StockPO;
import vo.StockBriefInfoVO;
import vo.StockDetailVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slow_time on 2017/3/7.
 */
public enum StockDetailServiceImpl implements StockDetailService {
    STOCK_DETAIL_SERVICE;


    private StockDAO stockDAO;
    private List<StockPO> stockPOs;
    private DAOFactoryService factory;
    private String code;


    StockDetailServiceImpl() {
        factory = new DAOFactoryServiceImpl();
        stockDAO = factory.createStockDAO();
        code = "";
    }

    public void setTest(DAOFactoryService factoryService) {
        factory = factoryService;
        stockDAO = factoryService.createStockDAO();
    }
    @Override
    public StockDetailVO getSingleStockDetails(String code, LocalDate date)  throws RemoteException {
        if(!code.equals(this.code)) {
            this.code = code;
            this.stockPOs = stockDAO.getStockInfoByCode(code);
        }

        if(stockPOs != null) {
            for(StockPO stockPO : stockPOs) {
                if(stockPO == null)
                    continue;
                if(stockPO.getDate().isEqual(date)) {
                    return PO2VOUtil.stockPO2StockDetailVO(stockPO);
                }
            }
        }
        return null;
    }

    @Override
    public List<StockBriefInfoVO> getStockBriefInfo(String code) throws RemoteException {
        if(!code.equals(this.code)) {
            this.code = code;
            this.stockPOs = stockDAO.getStockInfoByCode(code);
        }

        if(stockPOs != null && stockPOs.size() > 0) {
            List<StockBriefInfoVO> stockBriefInfoVOs = new ArrayList<>();
            StockPO stockPO1 = stockPOs.get(0);
            stockPOs.remove(0);
            for(StockPO stockPO2 : stockPOs) {
                StockBriefInfoVO stockBriefInfoVO = PO2VOUtil.stockPO2StockBriefInfoVO(stockPO1, stockPO2);
                if(stockBriefInfoVO != null)
                {
                    stockBriefInfoVOs.add(stockBriefInfoVO);
                    stockPO1 = stockPO2;
                }
            }
            return stockBriefInfoVOs;
        }

        return null;
    }
}
