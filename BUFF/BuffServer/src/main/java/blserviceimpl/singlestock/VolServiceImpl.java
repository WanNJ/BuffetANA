package blserviceimpl.singlestock;

import blservice.singlestock.VolService;
import vo.StockVolVO;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by slow_time on 2017/3/5.
 */
public class VolServiceImpl implements VolService {
    @Override
    public List<StockVolVO> getStockVol(String code, LocalDate beginDate, LocalDate endDate) {
        return null;
    }
}
