package stockenum;

import pick.PickStockService;
import pick.PickStockServiceImpl;

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

        PickStockService  pickStockService  = PickStockServiceImpl.PICK_STOCK_SERVICE;

        @Override
        public Comparator<String> getCompareRank(LocalDate begin, LocalDate end, boolean asd, int formPeriod) {
            Comparator<String> comparator = new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    double ma1 = pickStockService.getSingleCodeMAInfo(o1, begin.minusDays(1),begin,formPeriod)
                            .get(0).MAValue;

                    double ma2 = pickStockService.getSingleCodeMAInfo(o2, begin.minusDays(1),begin,formPeriod)
                            .get(0).MAValue;

                    double adj1  = pickStockService.getSingleCodeInfo(o1 ,begin.minusDays(1),begin)
                            .get(0).getAdjCloseIndex();

                    double adj2  = pickStockService.getSingleCodeInfo(o2 ,begin.minusDays(1),begin)
                            .get(0).getAdjCloseIndex();

                    double ans1 = (adj1-ma1)/adj1;
                    double ans2 = (adj2-ma2)/adj2;

                    if(asd){
                        if(ans1 > ans2)  return 1;
                        else return -1;
                    }else{
                        if(ans1 > ans2)  return -1;
                        else return 1;
                    }

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
