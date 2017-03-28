package stockenum;

import blserviceimpl.strategy.BackData;
import blserviceimpl.strategy.PickleData;
import pick.PickStockService;
import pick.PickStockServiceImpl;
import po.StockPO;
import util.DayMA;
import util.RunTimeSt;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * Created by slow_time on 2017/3/24.
 */
public enum StrategyType  implements RankMode{




    /**
     * 回归策略
     */
    MA {
        PickStockService pickStockService = PickStockServiceImpl.PICK_STOCK_SERVICE;

        @Override
        public Comparator<BackData> getCompareRank(boolean asd) {

            return new Comparator<BackData>() {
                @Override
                public int compare(BackData o1, BackData o2) {
                    if(asd){
                        if((double)o1.rankValue > (double)o2.rankValue)  return 1;
                        else return -1;
                    }else{
                        if((double)o1.rankValue > (double)o2.rankValue)  return -1;
                        else return 1;
                    }
                }
            };
        }

        @Override
        public List<PickleData> setRankValue(List<PickleData> pickleDatas, List<String> codeList
                ,LocalDate begin , LocalDate end, int holdPeriod) {
            for (String code: codeList) {

                RunTimeSt.getRunTime("开始读取 "+code+"股票");

                List<DayMA> dayMAs =  pickStockService.getSingleCodeMAInfo
                        (code ,begin.minusDays(1),end,holdPeriod);
                List<StockPO>  stockPOs = pickStockService
                        .getSingleCodeInfo(code , begin.minusDays(1) , end);



                RunTimeSt.getRunTime("结束读取 "+code+"股票");

                int MAcount = 0;
                int Adjcount = 0;


                /**
                 * 暂时先算前一天的 因为begin就要买了!!!!!
                 */
                for(int i = 0 ; i < pickleDatas.size() ; i++){
                    PickleData pickleData = pickleDatas.get(i);
                    while(dayMAs.get(MAcount).date.isBefore(pickleData.beginDate.minusDays(1))){
                        MAcount++;
                    }

                    while(stockPOs.get(Adjcount).getDate().isBefore(pickleData.beginDate.minusDays(1))){
                        Adjcount++;
                    }
//                    if(code.equals("300052"))
//                    System.out.println(dayMAs.get(MAcount).date);
                    double MA = dayMAs.get(MAcount).MAValue;
//                    if(code.equals("300052"))
//                    System.out.println(MA);
                    double Adj = stockPOs.get(Adjcount).getAdjCloseIndex();

                    double rank =  (MA-Adj)/MA;

                    /**
                     * 查看在持有期是否   出现停牌
                     */
                    boolean isStop = false; //是否停牌
                    while(!isStop && !stockPOs.get(Adjcount).getDate().isAfter(pickleData.endDate)){
                        if(stockPOs.get(Adjcount).getVolume()==0) {
                            isStop = true;

                        }
                        Adjcount++;
                    }
                    Adjcount--;
                    //如果没有停牌 则加入可以进行进一步筛选和排序的队列
                    if(!isStop){
                        pickleData.stockCodes.add(new BackData(code,rank));
                    }
                }


                RunTimeSt.getRunTime("结束计算 "+code+"股票");
            }

            return pickleDatas;
        }
    },

    /**
     * 动量策略
     */
    MOM {
        @Override
        public Comparator<BackData> getCompareRank(boolean asd) {
            return (o1, o2) -> {
                if(asd){
                    if((double)o1.rankValue > (double)o2.rankValue)  return 1;
                    else return -1;
                }else{
                    if((double)o1.rankValue > (double)o2.rankValue)  return -1;
                    else return 1;
                }
            };
        }

        @Override
        public List<PickleData> setRankValue(List<PickleData> pickleDatas, List<String> codeList
                ,LocalDate begin , LocalDate end , int holdPeriod) {
            return null;
        }
    }


}
