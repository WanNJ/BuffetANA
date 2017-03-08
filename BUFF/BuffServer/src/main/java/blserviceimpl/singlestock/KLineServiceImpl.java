package blserviceimpl.singlestock;

import blservice.exception.DateIndexException;
import blservice.singlestock.KLineService;
import vo.KLinePieceVO;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by slow_time on 2017/3/5.
 */
public class KLineServiceImpl implements KLineService {
    @Override
    public List<KLinePieceVO> getDailyKLine(String code, LocalDate beginDate, LocalDate endDate) throws DateIndexException {
        return null;
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
