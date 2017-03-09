package blstub.singlestockstub;

import blservice.exception.DateIndexException;
import blservice.singlestock.KLineService;
import vo.KLinePieceVO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slow_time on 2017/3/7.
 */
public class KLineServiceImpl_Stub implements KLineService {

    @Override
    public List<KLinePieceVO> getDailyKLine(String code, LocalDate beginDate, LocalDate endDate) throws DateIndexException {
        if(beginDate.isAfter(endDate)) {
            throw new DateIndexException(beginDate, endDate);
        }
        List<KLinePieceVO> kLinePieceVOs = new ArrayList<>();
        int temp = 0;
        while(!beginDate.isAfter(endDate)) {
            KLinePieceVO kLinePieceVO = new KLinePieceVO(beginDate, 112.5 + temp, 109.2 + temp,
            110.2 + temp, 111.5 + temp);
            kLinePieceVOs.add(kLinePieceVO);
            temp++;
            beginDate = beginDate.plusDays(1);
        }
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
