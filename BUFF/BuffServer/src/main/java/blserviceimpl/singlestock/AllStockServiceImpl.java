package blserviceimpl.singlestock;

import blservice.singlestock.AllStockService;
import dataservice.stockmap.StockNameToCodeDAO;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;
import po.StockNameAndCodePO;
import vo.StockNameAndCodeVO;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slow_time on 2017/3/9.
 */
public enum AllStockServiceImpl implements AllStockService {
    ALL_STOCK_SERVICE;

    private StockNameToCodeDAO stockNameToCodeDAO;
    private List<StockNameAndCodePO> stockNameAndCodePOs;
    private DAOFactoryService factory;

    AllStockServiceImpl() {
        factory = new DAOFactoryServiceImpl();
        stockNameToCodeDAO = factory.createStockNameToCodeDAO();
        stockNameAndCodePOs = stockNameToCodeDAO.getNameToCodeMap();
    }

    @Override
    public List<StockNameAndCodeVO> getAllStock() throws RemoteException {
        List<StockNameAndCodeVO> stockNameAndCodeVOs = new ArrayList<>();
        stockNameAndCodePOs.forEach(stockNameAndCodePO -> {
            stockNameAndCodeVOs.add(new StockNameAndCodeVO(stockNameAndCodePO.getName(), stockNameAndCodePO.getCode()));
        });
        return stockNameAndCodeVOs;
    }
}