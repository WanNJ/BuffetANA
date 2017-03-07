package blstub.singlestockstub;

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
    public List<KLinePieceVO> getDailyssKLine(String code, LocalDate beginDate, LocalDate endDate) {
        if(beginDate.isAfter(endDate)) {
            System.out.println("beginDate cannot be after endDate");
            return null;
        }
        List<KLinePieceVO> kLinePieceVOs = new ArrayList<>();
        int temp = 0;
        while(!beginDate.isAfter(endDate)) {
            KLinePieceVO kLinePieceVO = new KLinePieceVO(beginDate, 11.25 + temp, 10.92 + temp,
            11.02 + temp, 11.16 + temp);
            kLinePieceVOs.add(kLinePieceVO);
            temp++;
            beginDate = beginDate.plusDays(1);
        }
        return kLinePieceVOs;
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
