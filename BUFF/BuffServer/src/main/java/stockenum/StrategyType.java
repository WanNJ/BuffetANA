package stockenum;

import blserviceimpl.strategy.BackData;
import blserviceimpl.strategy.PickleData;
import pick.PickStockService;
import pick.PickStockServiceImpl;
import po.StockPO;
import util.DayMA;
import util.FormationMOM;
import util.RunTimeSt;
import vo.StockPickIndexVO;

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
                ,LocalDate begin , LocalDate end, int holdPeriod ,List<StockPickIndexVO> stockPickIndexVOs) {
            for (String code: codeList) {

                RunTimeSt.getRunTime("开始读取 "+code+"股票");

                List<DayMA> dayMAs =  pickStockService.getSingleCodeMAInfo
                        (code ,begin.minusDays(1),end,holdPeriod);
                List<StockPO>  stockPOs = pickStockService
                        .getSingleCodeInfo(code , begin.minusDays(1) , end);



                RunTimeSt.getRunTime("结束读取 "+code+"股票");

                int MAcount = 0;
                int Adjcount = 0;

                //Added By TY
                int k = 0;


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

                    //Added By TY
                    double firstDayOpen = 0 ;
                    double lastDayClose = 0;
                    //System.out.println("STRATEGY  input KKKKK:::    ");
                    while(stockPOs.get(k).getDate().isEqual(pickleDatas.get(i).beginDate)) {
                        //System.out.println(k);
                        k++;
                    }
                    firstDayOpen = stockPOs.get(k).getOpen_Price();
                    while(stockPOs.get(k).getDate().isEqual( pickleDatas.get(i).endDate))
                        k++;
                    lastDayClose = stockPOs.get(k).getClose_Price();

                    //如果没有停牌 则加入可以进行进一步筛选和排序的队列
                    if(!isStop){
                        pickleData.stockCodes.add(new BackData(code,rank,firstDayOpen,lastDayClose));
                    }
                }




                // TODO
                // !!!!!!!!!!!在此处加入  过滤参数的注入
                for (StockPickIndexVO s : stockPickIndexVOs) {
                   pickleDatas =  s.stockPickIndex.setFilterValue(pickleDatas,code);
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
        PickStockService pickStockService = PickStockServiceImpl.PICK_STOCK_SERVICE;

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
                ,LocalDate begin , LocalDate end , int formationPeriod, List<StockPickIndexVO> stockPickIndexVOs) {

            for(String code: codeList) {

                List<FormationMOM> formationMOMs = pickStockService.getSingleCodeMOMInfo(code, begin, end, formationPeriod);
                List<StockPO>  stockPOs = pickStockService.getSingleCodeInfo(code, begin, end);

                int j = 0;
                int k = 0;
                for(int i = 0; i < pickleDatas.size(); i++) {
                    while(!formationMOMs.get(j).date.isEqual(pickleDatas.get(i).beginDate))
                        j++;
                    double firstDayOpen;
                    double lastDayClose;

                    boolean isStop = false;
                    while(stockPOs.get(k).getDate().isEqual(pickleDatas.get(i).beginDate))
                        k++;
                    firstDayOpen = stockPOs.get(k).getOpen_Price();
                    while(stockPOs.get(k).getDate().isEqual(pickleDatas.get(i).endDate)) {
                        if(stockPOs.get(k).getVolume() == 0)
                            isStop = true;
                        k++;
                    }
                    lastDayClose = stockPOs.get(k).getClose_Price();
                    if(!isStop)
                        pickleDatas.get(i).stockCodes.add(new BackData(code, formationMOMs.get(j).yeildRate, firstDayOpen, lastDayClose));
                }

                for (StockPickIndexVO s : stockPickIndexVOs) {
                    pickleDatas =  s.stockPickIndex.setFilterValue(pickleDatas,code);
                }


            }
            return pickleDatas;
        }
    }


}
