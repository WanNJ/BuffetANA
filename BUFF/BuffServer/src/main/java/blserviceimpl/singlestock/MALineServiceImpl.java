package blserviceimpl.singlestock;

import blservice.singlestock.MALineService;
import vo.MAPieceVO;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by slow_time on 2017/3/5.
 */
public class MALineServiceImpl implements MALineService {
    @Override
    public List<MAPieceVO> getMAInfo(String code, LocalDate beginDate, LocalDate endDate) {
        return null;
    }
}
