package blserviceimpl.singlestock;

import blservice.singlestock.StockDetailService;
import blserviceimpl.util.PO2VOUtil;
import dataservice.singlestock.StockDAO;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import po.StockPO;
import vo.StockBriefInfoVO;
import vo.StockDetailVO;

import java.time.LocalDate;
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

    @Override
    public StockDetailVO getSingleStockDetails(String code, LocalDate date) {
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
    public ObservableList<StockBriefInfoVO> getStockBriefInfo(String code) {
        if(!code.equals(this.code)) {
            this.code = code;
            this.stockPOs = stockDAO.getStockInfoByCode(code);
        }

        if(stockPOs != null && stockPOs.size() > 0) {
            ObservableList<StockBriefInfoVO> stockBriefInfoVOs = FXCollections.observableArrayList();
            StockPO stockPO1 = stockPOs.get(0);
            for(StockPO stockPO2 : stockPOs) {
                StockBriefInfoVO stockBriefInfoVO = PO2VOUtil.stockPO2StockBriefInfoVO(stockPO1, stockPO2);
                if(stockBriefInfoVO != null)
                    stockBriefInfoVOs.add(stockBriefInfoVO);
                stockPO1 = stockPO2;
            }
            return stockBriefInfoVOs;
        }

        return null;
    }
}
