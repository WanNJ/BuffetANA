package stockenum;

import blserviceimpl.strategy.BackData;
import blserviceimpl.strategy.PickleData;
import blserviceimpl.strategy.SingleBackData;
import pick.PickStockService;
import pick.PickStockServiceImpl;
import vo.*;

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
        @Override
        public String toString() {
            return "前一日成交量";
        }

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

        @Override
        public List<SingleBackData> setNewFilterValue(List<SingleBackData> current, String code, int codeIndex) {
            LocalDate begin =  current.get(0).date;
            int lastIndex =  current.size()-1;
            LocalDate end = current.get(lastIndex).date;


            List<LongPeiceVO>  longPeiceVOs = pickStockService.getLastVol(code,begin,end);

            int j = 0;

            for (int i = 0 ; i<=lastIndex ; i++){


                LocalDate beg = current.get(i).date;
                while(longPeiceVOs.get(j).localDate.isBefore(beg)){
                    j++;
                }

                current.get(i).rilterValues[this.ordinal()] =
                        longPeiceVOs.get(j).amount;


            }
            return current;
        }


    },
    /**
     * 昨日涨幅
     */
    PREVIOUS_DAY_UPRATE {

        @Override
        public String toString() {
            return "前一日涨幅";
        }

        PickStockService pickStockService = PickStockServiceImpl.PICK_STOCK_SERVICE;
        @Override
        public Predicate<BackData> getFilter(Double lowerBound, Double upBound) {
            Predicate<BackData> backDataPredicate = backData -> {
                //如果没有限制
                if(lowerBound==null && upBound==null)
                    return true;

                //有下限
                double value =  (double)backData.filterData[this.ordinal()];
                if(lowerBound!=null  && value<lowerBound)
                    return false;

                //有上限
                if(upBound!=null  && value>upBound)
                    return false;
                return true;
            };
            return backDataPredicate;
        }

        @Override
        public List<PickleData> setFilterValue(List<PickleData> current, String code) {
            LocalDate begin =  current.get(0).beginDate;
            LocalDate end = current.get(current.size()-1).endDate;


            List<UpRangeVO>  upRangeVOS = pickStockService.getLastUpRange(code, begin, end);

            int j = 0;

            for (int i = 0 ; i<current.size() ; i++){


                LocalDate beg = current.get(i).beginDate;
                //System.out.println(beg);
                //循环到我们要输入数据的那一天
                while(!upRangeVOS.get(j).localDate.plusDays(1).isAfter(beg)){
                    j++;
                }

                int last = current.get(i).stockCodes.size()-1;
                if(last < 0 || current.get(i).stockCodes.get(last).code != code){
                    // nothing todo
                }else{
                    current.get(i).stockCodes.get(last).filterData[this.ordinal()]
                            = upRangeVOS.get(j).upRange;
                }


            }
            return current;
        }

        @Override
        public List<SingleBackData> setNewFilterValue(List<SingleBackData> current, String code, int codeIndex) {

            LocalDate begin =  current.get(0).date;
            int lastIndex =  current.size()-1;
            LocalDate end = current.get(lastIndex).date;


            List<UpRangeVO>  upRangeVOS = pickStockService.getLastUpRange(code,begin,end);

            int j = 0;

            for (int i = 0 ; i<=lastIndex ; i++){


                LocalDate beg = current.get(i).date;
                while(upRangeVOS.get(j).localDate.isBefore(beg)){
                    j++;
                }

                current.get(i).rilterValues[this.ordinal()] =
                        upRangeVOS.get(j).upRange;


            }
            return current;
        }
    },

    /**
     * 昨日复权平均
     */
    PREVIOUS_DAY_ADJ {
        @Override
        public String toString() {
            return "前一日复权收盘价";
        }

        PickStockService pickStockService = PickStockServiceImpl.PICK_STOCK_SERVICE;
        @Override
        public Predicate<BackData> getFilter(Double lowerBound, Double upBound) {
            Predicate<BackData> backDataPredicate = backData -> {
                //如果没有限制
                if(lowerBound==null && upBound==null)
                    return true;

                //有下限
                double value =  (double)backData.filterData[this.ordinal()];
                if(lowerBound!=null  && value<lowerBound)
                    return false;

                //有上限
                if(upBound!=null  && value>upBound)
                    return false;
                return true;
            };
            return backDataPredicate;
        }

        @Override
        public List<PickleData> setFilterValue(List<PickleData> current, String code) {
            LocalDate begin =  current.get(0).beginDate;
            LocalDate end = current.get(current.size()-1).endDate;


            List<AdjVO>  adjVOS = pickStockService.getAdj(code, begin, end);

            int j = 0;

            for (int i = 0 ; i<current.size() ; i++){


                LocalDate beg = current.get(i).beginDate;
                //System.out.println(beg);
                //循环到我们要输入数据的那一天
                while(!adjVOS.get(j).localDate.plusDays(1).isAfter(beg)){
                    j++;
                }

                int last = current.get(i).stockCodes.size()-1;
                if(last < 0 || current.get(i).stockCodes.get(last).code != code){
                    // nothing todo
                }else{
                    current.get(i).stockCodes.get(last).filterData[this.ordinal()]
                            = adjVOS.get(j).adj;
                }


            }
            return current;
        }

        @Override
        public List<SingleBackData> setNewFilterValue(List<SingleBackData> current, String code, int codeIndex) {
            LocalDate begin =  current.get(0).date;
            int lastIndex =  current.size()-1;
            LocalDate end = current.get(lastIndex).date;


            List<AdjVO>  adjVOS = pickStockService.getAdj(code, begin, end);

            int j = 0;

            for (int i = 0 ; i<=lastIndex ; i++){


                LocalDate beg = current.get(i).date;
                while(adjVOS.get(j).localDate.isBefore(beg)){
                    j++;
                }

                current.get(i).rilterValues[this.ordinal()] =
                        adjVOS.get(j).adj;


            }
            return current;
        }
    },

    /**
     * 换手率
     */
    CHANGE_RATE {
        @Override
        public String toString() {
            return "前一日换手率";
        }
        PickStockService pickStockService = PickStockServiceImpl.PICK_STOCK_SERVICE;

        @Override
        public Predicate<BackData> getFilter(Double lowerBound, Double upBound) {
            Predicate<BackData> backDataPredicate = backData -> {
                //如果没有限制
                if (lowerBound == null && upBound == null)
                    return true;

                //有下限
                double value =  (double)backData.filterData[this.ordinal()];
                if (lowerBound != null && value < lowerBound)
                    return false;

                //有上限
                if (upBound != null && value > upBound)
                    return false;
                return true;
            };
            return backDataPredicate;
        }

        @Override
        public List<PickleData> setFilterValue(List<PickleData> current, String code) {
            LocalDate begin = current.get(0).beginDate;
            LocalDate end = current.get(current.size() - 1).endDate;


            List<ChangeRateVO> changeRateVOS = pickStockService.getChangeRate(code, begin, end);

            int j = 0;

            for (int i = 0; i < current.size(); i++) {


                LocalDate beg = current.get(i).beginDate;
                //System.out.println(beg);
                //循环到我们要输入数据的那一天
                while (!changeRateVOS.get(j).localDate.plusDays(1).isAfter(beg)) {
                    j++;
                }

                int last = current.get(i).stockCodes.size() - 1;
                if (last < 0 || current.get(i).stockCodes.get(last).code != code) {
                    // nothing todo
                } else {
                    current.get(i).stockCodes.get(last).filterData[this.ordinal()]
                            = changeRateVOS.get(j).changeRate;
                }


            }
            return current;
        }

        @Override
        public List<SingleBackData> setNewFilterValue(List<SingleBackData> current, String code, int codeIndex) {
            LocalDate begin =  current.get(0).date;
            int lastIndex =  current.size()-1;
            LocalDate end = current.get(lastIndex).date;


            List<ChangeRateVO>  changeRateVOS = pickStockService.getChangeRate(code, begin, end);

            int j = 0;

            for (int i = 0 ; i<=lastIndex ; i++){


                LocalDate beg = current.get(i).date;
                while(changeRateVOS.get(j).localDate.isBefore(beg)){
                    j++;
                }

                current.get(i).rilterValues[this.ordinal()] =
                        changeRateVOS.get(j).changeRate;


            }
            return current;
        }
    },

    CIRCULATION_MARKET_VALUE {
        @Override
        public String toString() {
            return "前一日市值";
        }
        PickStockService pickStockService = PickStockServiceImpl.PICK_STOCK_SERVICE;

        @Override
        public Predicate<BackData> getFilter(Double lowerBound, Double upBound) {
            Predicate<BackData> backDataPredicate = backData -> {
                //如果没有限制
                if (lowerBound == null && upBound == null)
                    return true;

                //有下限
                double value =  (double)backData.filterData[this.ordinal()];
                if (lowerBound != null && value < lowerBound)
                    return false;

                //有上限
                if (upBound != null && value > upBound)
                    return false;
                return true;
            };
            return backDataPredicate;
        }

        @Override
        public List<PickleData> setFilterValue(List<PickleData> current, String code) {
            LocalDate begin = current.get(0).beginDate;
            LocalDate end = current.get(current.size() - 1).endDate;


            List<CirculationMarketValueVO> circulationMarketValueVOS = pickStockService.getCirculationMarketValue(code, begin, end);

            int j = 0;

            for (int i = 0; i < current.size(); i++) {


                LocalDate beg = current.get(i).beginDate;
                //System.out.println(beg);
                //循环到我们要输入数据的那一天
                while (!circulationMarketValueVOS.get(j).localDate.plusDays(1).isAfter(beg)) {
                    j++;
                }

                int last = current.get(i).stockCodes.size() - 1;
                if (last < 0 || current.get(i).stockCodes.get(last).code != code) {
                    // nothing todo
                } else {
                    current.get(i).stockCodes.get(last).filterData[this.ordinal()]
                            = circulationMarketValueVOS.get(j).circulationMarketValue;
                }


            }
            return current;
        }

        @Override
        public List<SingleBackData> setNewFilterValue(List<SingleBackData> current, String code, int codeIndex) {
            LocalDate begin =  current.get(0).date;
            int lastIndex =  current.size()-1;
            LocalDate end = current.get(lastIndex).date;


            List<CirculationMarketValueVO>  circulationMarketValueVOS = pickStockService.getCirculationMarketValue(code, begin, end);

            int j = 0;

            for (int i = 0 ; i<=lastIndex ; i++){


                LocalDate beg = current.get(i).date;
                while(circulationMarketValueVOS.get(j).localDate.isBefore(beg)){
                    j++;
                }

                current.get(i).rilterValues[this.ordinal()] =
                        circulationMarketValueVOS.get(j).circulationMarketValue;


            }
            return current;
        }
    },

    /**
     * 昨日振幅
     */
    PREVIOUS_DAY_AMPLITUDE {
        @Override
        public String toString() {
            return "前一日振幅";
        }

        PickStockService pickStockService = PickStockServiceImpl.PICK_STOCK_SERVICE;
        @Override
        public Predicate<BackData> getFilter(Double lowerBound, Double upBound) {
            Predicate<BackData> backDataPredicate = backData -> {
                //如果没有限制
                if(lowerBound==null && upBound==null)
                    return true;

                //有下限
                double value =  (double)backData.filterData[this.ordinal()];
                if(lowerBound!=null  && value<lowerBound)
                    return false;

                //有上限
                if(upBound!=null  && value>upBound)
                    return false;
                return true;
            };
            return backDataPredicate;
        }

        @Override
        public List<PickleData> setFilterValue(List<PickleData> current, String code) {
            LocalDate begin =  current.get(0).beginDate;
            LocalDate end = current.get(current.size()-1).endDate;


            List<AmplitudeVO>  amplitudeVOS = pickStockService.getAmplitude(code, begin, end);

            int j = 0;

            for (int i = 0 ; i<current.size() ; i++){


                LocalDate beg = current.get(i).beginDate;
                //System.out.println(beg);
                //循环到我们要输入数据的那一天
                while(!amplitudeVOS.get(j).localDate.plusDays(1).isAfter(beg)){
                    j++;
                }

                int last = current.get(i).stockCodes.size()-1;
                if(last < 0 || current.get(i).stockCodes.get(last).code != code){
                    // nothing todo
                }else{
                    current.get(i).stockCodes.get(last).filterData[this.ordinal()]
                            = amplitudeVOS.get(j).amplitude;
                }


            }
            return current;
        }

        @Override
        public List<SingleBackData> setNewFilterValue(List<SingleBackData> current, String code, int codeIndex) {
            LocalDate begin =  current.get(0).date;
            int lastIndex =  current.size()-1;
            LocalDate end = current.get(lastIndex).date;


            List<AmplitudeVO>  amplitudeVOS = pickStockService.getAmplitude(code,begin,end);

            int j = 0;

            for (int i = 0 ; i<=lastIndex ; i++){


                LocalDate beg = current.get(i).date;
                while(amplitudeVOS.get(j).localDate.isBefore(beg)){
                    j++;
                }

                current.get(i).rilterValues[this.ordinal()] =
                        amplitudeVOS.get(j).amplitude;


            }
            return current;
        }

    }

}
