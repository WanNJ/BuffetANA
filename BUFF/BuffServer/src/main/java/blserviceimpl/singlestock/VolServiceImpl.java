package blserviceimpl.singlestock;

import blservice.exception.DateIndexException;
import blservice.singlestock.VolService;
import blserviceimpl.util.PO2VOUtil;
import dataservice.singlestock.StockDAO;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;
import po.StockPO;
import util.DateUtil;
import vo.StockVolVO;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slow_time on 2017/3/8.
 */
public enum VolServiceImpl implements VolService {
    VOL_SERVICE;

    private StockDAO stockDAO;
    private List<StockPO> stockPOs;
    private DAOFactoryService factory;
    private String code;

    VolServiceImpl() {
        factory = new DAOFactoryServiceImpl();
        stockDAO = factory.createStockDAO();
        code = "";
    }

    @Override
    public List<StockVolVO> getStockVol(String code, LocalDate beginDate, LocalDate endDate) throws DateIndexException, RemoteException {
        if(!code.equals(this.code)) {
            this.code = code;
            this.stockPOs = stockDAO.getStockInfoByCode(code);
        }
        if(beginDate.isAfter(endDate))
            throw new DateIndexException(beginDate, endDate);

        List<StockVolVO> stockVolVOs = new ArrayList<>();
        stockPOs.forEach(stockPO -> {
            if(DateUtil.isBetween(stockPO.getDate(), beginDate, endDate))
                stockVolVOs.add(PO2VOUtil.stockPO2StockVolVO(stockPO));

        });
        stockVolVOs.sort((stockVolVO1, stockVolVO2) -> {
            if(stockVolVO1.date.isEqual(stockVolVO2.date))
                return 0;
            return stockVolVO1.date.isBefore(stockVolVO2.date) ? -1 : 1;
        });
        return stockVolVOs;
    }
}
