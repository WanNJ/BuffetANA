package blserviceimpl.singlestock;

import blservice.exception.DateIndexException;
import blservice.singlestock.KLineService;
import blserviceimpl.util.PO2VOUtil;
import dataservice.singlestock.StockDAO;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;
import po.StockPO;
import util.DateUtil;
import vo.KLinePieceVO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slow_time on 2017/3/8.
 */
public enum KLineServiceImpl implements KLineService {
    K_LINE_SERVICE;

    private StockDAO stockDAO;
    private List<StockPO> stockPOs;
    private DAOFactoryService factory;
    private String code;

    KLineServiceImpl() {
        factory = new DAOFactoryServiceImpl();
        stockDAO = factory.createStockDAO();
        code = "";
    }

    @Override
    public List<KLinePieceVO> getDailyKLine(String code, LocalDate beginDate, LocalDate endDate) throws DateIndexException {
        if(!code.equals(this.code)) {
            this.code = code;
            this.stockPOs = stockDAO.getStockInfoByCode(code);
        }

        if(beginDate.isAfter(endDate))
            throw new DateIndexException(beginDate, endDate);

        List<KLinePieceVO> kLinePieceVOs = new ArrayList<>();
        stockPOs.forEach(stockPO -> {
            if(stockPO != null) {
                if(stockPO.getVolume() != 0 && DateUtil.isBetween(stockPO.getDate(), beginDate, endDate))
                    kLinePieceVOs.add(PO2VOUtil.stockPO2KLinePieceVO(stockPO));
            }
        });
        kLinePieceVOs.sort((kLinePieceVO1, kLinePieceVO2) -> {
            if(kLinePieceVO1.date.isEqual(kLinePieceVO2.date))
                return 0;
            return kLinePieceVO1.date.isBefore(kLinePieceVO2.date) ? -1 : 1;
        });
        return kLinePieceVOs;
    }

    @Override
    public List<KLinePieceVO> getWeeklyKLine(String code, LocalDate beginDate, LocalDate endDate) {
        return null;
    }

    @Override
    public List<KLinePieceVO> getMonthlyKLine(String code, LocalDate beginDate, LocalDate endDate) {
        return null;
    }
}
