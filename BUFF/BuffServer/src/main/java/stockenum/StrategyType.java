package stockenum;

import blserviceimpl.strategy.BackData;

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
        public Comparator<BackData> getCompareRank(LocalDate begin, LocalDate end, boolean asd, int formPeriod) {
            return null;
        }
    },
    /**
     * 动量策略
     */
    MOM {
        @Override
        public Comparator<BackData> getCompareRank(LocalDate begin, LocalDate end, boolean asd, int formPeriod) {
            return null;
        }
    }


}
