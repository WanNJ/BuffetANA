package blservice.singlestock;

import vo.DailyKLineVO;
import vo.StockBriefInfoVo;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by slow_time on 2017/3/5.
 */
public interface StockDetailService {

    /**
     *用户选择具体日期后，查看这支股票那天的详细信息
     * @param name 个股的名称
     * @param date 用户选择的具体日期
     * @return 该支股票此日期的详细信息
     */
    DailyKLineVO getSingleStockDetails(String name, LocalDate date);

    /**
     * 获得某一个股每一天的简要信息
     * @param name 个股的名称
     * @return 某一个股每一天的简要信息
     */
    ArrayList<StockBriefInfoVo> getStockBriefInfo(String name);
}
