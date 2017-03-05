package blservice.singlestock;

import vo.KLinePieceVO;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by slow_time on 2017/3/5.
 */
public interface KLineService {

    /**
     * 用户进行具体个股日K线图搜索时，调用的方法
     * @param code 个股的编码
     * @param beginDate 日K线图的开始日期
     * @param endDate 日K线图的结束日期
     * @return
     */
    List<KLinePieceVO> getDailyssKLine(String code, LocalDate beginDate, LocalDate endDate);

    /**
     * 用户进行具体个股周K线图搜索时，调用的方法
     * @param code 个股的编码
     * @param beginDate 周K线图的开始日期
     * @param endDate 周K线图的结束日期
     * @return
     */
    List<KLinePieceVO> getWeeklyssKLine(String code, LocalDate beginDate, LocalDate endDate);

    /**
     * 用户进行具体个股月K线图搜索时，调用的方法
     * @param code 个股的编码
     * @param beginDate 月K线图的开始日期
     * @param endDate 月K线图的结束日期
     * @return
     */
    List<KLinePieceVO> getMonthlyssKLine(String code, LocalDate beginDate, LocalDate endDate);
}
