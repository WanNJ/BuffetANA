package stockenum;

import java.time.LocalDate;
import java.util.Comparator;

/**
 * Created by slow_time on 2017/3/24.
 */
public enum StrategyType  implements RankMode{
    /**
     * 回归策略
     */
    MA {
        @Override
        public Comparator<String> getCompareRank(LocalDate begin, LocalDate end, boolean asd, int formPeriod) {
            Comparator<String> comparator = new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return 0;
                }
            };
            return comparator;
        }
    },
    /**
     * 动量策略
     */
    MOM {
        @Override
        public Comparator<String> getCompareRank(LocalDate begin, LocalDate end, boolean asd, int formPeriod) {
            return null;
        }
    }


}
