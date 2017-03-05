package blservice.singlestock;

import vo.DailyKLineVO;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by slow_time on 2017/3/5.
 */
public interface KLineService {

    /**
     * 用户点击某只个股时，未进行日期的选择时，获得默认的日K线图
     * @param name 个股的名称
     * @return
     */
    ArrayList<DailyKLineVO> getDefaultDailyKLine(String name);

    /**
     * 用户进行具体个股日K线图搜索时，输入了起始日期后，调用的方法
     * @param name 个股名称
     * @param beginDate 日K线图的开始日期
     * @param endDate 日K线图的结束日期
     * @return
     */
    ArrayList<DailyKLineVO> getConcreteDailyssKLine(String name, LocalDate beginDate, LocalDate endDate);
}
