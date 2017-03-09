package blstub.singlestockstub;

import blservice.singlestock.MALineService;
import vo.MAPieceVO;
import vo.StockVolVO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wshwbluebird on 2017/3/9.
 */
public class MALineServiceImpl_Stub implements MALineService {

    @Override
    public List<MAPieceVO> getMAInfo(String code, LocalDate beginDate, LocalDate endDate) {
        List<MAPieceVO> maPieceVOs = new ArrayList<>();
        Random random = new Random(10);
        while(!beginDate.isAfter(endDate)) {
            MAPieceVO maPieceVO = new MAPieceVO(random.nextDouble(),random.nextDouble(),
                    random.nextDouble(),random.nextDouble(),random.nextDouble(),random.nextDouble(),beginDate);
            maPieceVOs.add(maPieceVO);
            beginDate = beginDate.plusDays(1);
        }

        return maPieceVOs;
    }

}
