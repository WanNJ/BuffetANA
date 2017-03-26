package stockenum;

import java.time.LocalDate;
import java.util.Comparator;

/**
 * Created by wshwbluebird on 2017/3/26.
 */
public interface RankMode {
    /**
     * 获取排名的比较器
     * @param begin
     * @param end
     * @param asd   是否按升序排列
     * @param formPeriod   持有期  或  均线的日数
     * @return
     */
    Comparator<String> getCompareRank(LocalDate begin, LocalDate end , boolean asd , int formPeriod);
}
