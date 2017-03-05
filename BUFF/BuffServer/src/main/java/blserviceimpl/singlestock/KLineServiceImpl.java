package blserviceimpl.singlestock;

import blservice.singlestock.KLineService;
import vo.KLinePieceVO;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by slow_time on 2017/3/5.
 */
public class KLineServiceImpl implements KLineService {
    @Override
    public List<KLinePieceVO> getDailyssKLine(String code, LocalDate beginDate, LocalDate endDate) {
        return null;
    }

    @Override
    public List<KLinePieceVO> getWeeklyssKLine(String code, LocalDate beginDate, LocalDate endDate) {
        return null;
    }

    @Override
    public List<KLinePieceVO> getMonthlyssKLine(String code, LocalDate beginDate, LocalDate endDate) {
        return null;
    }
}
