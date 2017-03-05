package blserviceimpl.singlestock;

import blservice.singlestock.StockDetailService;
import vo.StockBriefInfoVO;
import vo.StockDetailVO;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by slow_time on 2017/3/5.
 */
public class StockDetailServiceImpl implements StockDetailService {
    @Override
    public StockDetailVO getSingleStockDetails(String code, LocalDate date) {
        return null;
    }

    @Override
    public List<StockBriefInfoVO> getStockBriefInfo(String code) {
        return null;
    }
}
