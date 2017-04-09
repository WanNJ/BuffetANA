package blserviceimpl.singlestock;

import blservice.singlestock.BenchStockService;
import dataservice.stockmap.StockNameToCodeDAO;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;
import po.StockNameAndCodePO;
import vo.StockNameAndCodeVO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by slow_time on 2017/4/9.
 */
public enum BenchStockServiceImpl implements BenchStockService {
    BENCH_STOCK_SERVICE;

    private StockNameToCodeDAO stockNameToCodeDAO;
    private List<StockNameAndCodePO> mainBoardStockPOs;
    private List<StockNameAndCodePO> secondBoardStockPOs;
    private List<StockNameAndCodePO> SMEBoardStockPOs;
    private DAOFactoryService factory;

    BenchStockServiceImpl() {
        factory = new DAOFactoryServiceImpl();
        stockNameToCodeDAO = factory.createStockNameToCodeDAO();
        mainBoardStockPOs = stockNameToCodeDAO.getMainBoardStock();
        secondBoardStockPOs = stockNameToCodeDAO.getSecondBoardStock();
        SMEBoardStockPOs = stockNameToCodeDAO.getSMEBoardStock();
    }

    @Override
    public List<StockNameAndCodeVO> getMainBoardStock() {
        return mainBoardStockPOs.stream().map(t -> new StockNameAndCodeVO(t.getName(), t.getCode())).collect(Collectors.toList());
    }

    @Override
    public List<StockNameAndCodeVO> getSecondBoardStock() {
        return secondBoardStockPOs.stream().map(t -> new StockNameAndCodeVO(t.getName(), t.getCode())).collect(Collectors.toList());

    }

    @Override
    public List<StockNameAndCodeVO> getSMEBoardStock() {
        return SMEBoardStockPOs.stream().map(t -> new StockNameAndCodeVO(t.getName(), t.getCode())).collect(Collectors.toList());

    }
}
