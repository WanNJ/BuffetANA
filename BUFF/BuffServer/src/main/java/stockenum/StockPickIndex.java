package stockenum;

import blserviceimpl.strategy.BackData;
import blserviceimpl.strategy.PickleData;
import pick.PickStockService;
import pick.PickStockServiceImpl;
import vo.LongPeiceVO;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

/**
 * 选股指标的类型
 * Created by slow_time on 2017/3/24.
 */


//TODO   以后就在这里添加参数
public enum StockPickIndex implements FilterMode {
    /**
     * 前一日成交量
     */
    PREVIOUS_DAY_VOL {

        PickStockService pickStockService = PickStockServiceImpl.PICK_STOCK_SERVICE;

        @Override
        public Predicate<BackData> getFilter(Double lowerBound, Double upBound) {
            Predicate<BackData> backDataPredicate = new Predicate<BackData>() {
                @Override
                public boolean test(BackData backData) {
                    //如果没有限制
                    if(lowerBound==null && upBound==null)
                        return true;

                    //有下限
                    long value =  (long)backData.filterData[PREVIOUS_DAY_VOL.ordinal()];
                    if(lowerBound!=null  && value<lowerBound)
                        return false;

                    //有上限
                    if(upBound!=null  && value>upBound)
                        return false;
                    return true;
                }
            };
            return backDataPredicate ;
        }

        @Override
        public List<PickleData> setFilterValue(List<PickleData> current, String code) {
           LocalDate begin =  current.get(0).beginDate;
           LocalDate end = current.get(current.size()-1).endDate;


           List<LongPeiceVO>  longPeiceVOs = pickStockService.getLastVol(code,begin,end);

           int j = 0;

           for (int i = 0 ; i<current.size() ; i++){


               LocalDate beg = current.get(i).beginDate;
               //System.out.println(beg);
               //循环到我们要输入数据的那一天
               while(!longPeiceVOs.get(j).localDate.plusDays(1).isAfter(beg)){
                   j++;
               }

               int last = current.get(i).stockCodes.size()-1;
               if(last < 0 || current.get(i).stockCodes.get(last).code!=code){
                   // nothing todo
               }else{
                   current.get(i).stockCodes.get(last).filterData[this.ordinal()]
                           = longPeiceVOs.get(j).amount;
               }


           }
           return current;
        }
    },
    /**
     * 昨日涨幅
     */
    PREVIOUS_DAY_UPRATE {
        @Override
        public Predicate<BackData> getFilter(Double lowerBound, Double upBound) {
            return null;
        }

        @Override
        public List<PickleData> setFilterValue(List<PickleData> current, String code) {
            return current;
        }
    }

}
