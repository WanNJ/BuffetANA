package stockenum;

import blserviceimpl.strategy.BackData;
import blserviceimpl.strategy.NewPickleData;
import blserviceimpl.strategy.PickleData;
import blserviceimpl.strategy.SingleBackData;
import pick.PickStockService;
import pick.PickStockServiceImpl;
import po.StockPO;
import util.DayMA;
import util.FormationMOM;
import vo.StockPickIndexVO;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by slow_time on 2017/3/24.
 */
public enum StrategyType  implements RankMode {


    /**
     * 回归策略
     */
    MA {
        @Override
        public String toString() {
            return "均线偏离";
        }

        PickStockService pickStockService = PickStockServiceImpl.PICK_STOCK_SERVICE;

        @Override
        public Comparator<BackData> getCompareRank(boolean asd) {

            return new Comparator<BackData>() {
                @Override
                public int compare(BackData o1, BackData o2) {
                    if (asd) {
                        //System.out.println();
                        if ((double) o1.rankValue > (double) o2.rankValue) return 1;
                        else return -1;
                    } else {
                        if ((double) o1.rankValue > (double) o2.rankValue) return -1;
                        else return 1;
                    }
                }
            };
        }

        /**
         * 注入排序的参数   要注意
         * 如果该股票没有形成期的参数  要把这只股票从pickledata里踢出去
         * 如果本来就没有这只股票  就跳过
         * @param pickleDatas
         * @param code
         * @param begin
         * @param end
         * @param holdPeriod
         * @return
         */
        @Override
        public List<PickleData>  setRankValue(List<PickleData> pickleDatas , String code
                ,LocalDate begin , LocalDate end , int holdPeriod){

                List<DayMA> dayMAs = pickStockService.getSingleCodeMAInfo
                        (code, begin.minusDays(1), end, holdPeriod);
                List<StockPO> stockPOs = pickStockService
                        .getSingleCodeInfo(code, begin.minusDays(10), end.plusDays(10));



                int MAcount = 0;
                int Adjcount = 0;



                /**
                 * 暂时先算前一天的 因为begin就要买了!!!!!
                 */


                for (int i = 0; i < pickleDatas.size(); i++) {
                    int lastIndex = pickleDatas.get(i).stockCodes.size()-1;
                    PickleData pickleData = pickleDatas.get(i);

                    //如果这只股票本来在这期间就不存在 直接跳过  不用注入参数
                    if(pickleData.stockCodes.size()==0||!pickleData.stockCodes.get(lastIndex).code.equals(code)) {
                        continue;
                    }else if(dayMAs == null){
                        //如果没有数据   删除当前列表最后一个 backdata  因为这只股票没有形成期的数据
                        pickleData.stockCodes.remove(lastIndex);
                        continue;
                    }
                    while (dayMAs.get(MAcount).date.isBefore(pickleData.beginDate.minusDays(1))) {
                        MAcount++;
                    }

                    while (stockPOs.get(Adjcount).getDate().isBefore(pickleData.beginDate.minusDays(1))) {
                        Adjcount++;
                    }

                    double MA = dayMAs.get(MAcount).MAValue;

                    double Adj = stockPOs.get(Adjcount).getAdjCloseIndex();

                    double rank = (MA - Adj) / MA;

                    pickleData.stockCodes.get(lastIndex).mixRank[this.ordinal()] = rank;

                    }


            return pickleDatas;
        }


        /**
         * add by wsw  原来旧的的方法就不删除了
         * @param codeList
         * @param begin
         * @param end
         * @param stockPickIndexVOs
         * @return
         */

        @Override
        public List<NewPickleData> setAllValue(List<String> codeList, LocalDate begin, LocalDate end, List<StockPickIndexVO> stockPickIndexVOs) {


            List<NewPickleData> newPickleDataList = codeList.stream().map(t -> {
                return new NewPickleData(t);
            }).collect(Collectors.toList());

            int codeIndex = 0;

            for (String code : codeList) {

//                System.out.println(code);
//                RunTimeSt.getRunTime("开始读取 "+code+"股票");
                boolean isnull = false;

                List<StockPO> stockPOs = pickStockService
                        .getSingleCodeInfo(code, begin.minusDays(10), end.plusDays(10));

                int beforeIndex = 0;


                //System.out.println(stockPOs.get(beforeIndex).getDate());

                while (stockPOs.get(beforeIndex).getDate().isBefore(begin)) {
                    beforeIndex++;
                }

                newPickleDataList.get(codeIndex).lastAdj = stockPOs.get(beforeIndex - 1).getAdjCloseIndex();

                /**
                 * 初始化该股票  有数据的时候的值
                 * 例如 如果这只股票在2月1日 没有数据  就不放到singleBackDataList中
                 * 如果有数据就放入
                 */
                newPickleDataList.get(codeIndex).singleBackDataList =
                        stockPOs.stream()
                                .filter(t -> !(t.getDate().isBefore(begin) || t.getDate().isAfter(end))).map(t -> {
                            return new SingleBackData(t.getDate(), t.getOpen_Price()
                                    , t.getClose_Price(), t.getAdjCloseIndex(), t.getVolume());
                        }).collect(Collectors.toList());


                /**
                 * //TODO 还是太慢了   以后优化
                 *
                 * 分别计算1到60天形成期的数据
                 */

                //RunTimeSt.getRunTime(code+"  begin set rank");
                for (int holdPeriod = 1; holdPeriod <= 60 && !isnull; holdPeriod++) {

                    //TODO  返回来的数据可能数空的
                    List<DayMA> dayMAs = pickStockService.getSingleCodeMAInfo
                            (code, begin.minusDays(1), end, holdPeriod);

                    //TODO  以后要做进一步的处理
                    if (dayMAs == null) {
                        System.out.println(code);
                        NewPickleData newPickleData = new NewPickleData(code);
                        newPickleDataList.remove(newPickleData);
                        isnull = true;
                        codeIndex--;
                        continue;
                    }

                    int MAcount = 0;
                    int Adjcount = 0;


                    for (int i = 0; i < newPickleDataList.get(codeIndex).singleBackDataList.size(); i++) {
                        SingleBackData singleBackData = newPickleDataList.get(codeIndex).singleBackDataList.get(i);
                        //System.out.println(singleBackData.date==null);
                        while (dayMAs.get(MAcount).date.isBefore(singleBackData.date.minusDays(1))) {
                            MAcount++;
                        }

                        while (stockPOs.get(Adjcount).getDate().isBefore(singleBackData.date.minusDays(1))) {
                            Adjcount++;
                        }

                        double MA = dayMAs.get(MAcount).MAValue;

                        double Adj = stockPOs.get(Adjcount).getAdjCloseIndex();

                        double rank = (MA - Adj) / MA;

                        singleBackData.rankValues[holdPeriod] = rank;

                    }

                }
                //RunTimeSt.getRunTime(code+"  after set rank");


                // TODO 在此处加入  过滤参数的注入
                // !!!!!!!!!!!在此处加入  过滤参数的注入
                if(!isnull) {
                    for (StockPickIndexVO s : stockPickIndexVOs) {
                        newPickleDataList.get(codeIndex).singleBackDataList =
                                s.stockPickIndex.setNewFilterValue
                                        (newPickleDataList.get(codeIndex).singleBackDataList, code, codeIndex);
                    }
                }

                // add by wsw
                /**
                 * 在底层计算相对的收益情况 (基准的收益情况）
                 */


                codeIndex++;

            }


            return newPickleDataList;
        }
    },

    /**
     * 动量策略
     */
    MOM {
        @Override
        public String toString() {
            return "动量策略";
        }

        PickStockService pickStockService = PickStockServiceImpl.PICK_STOCK_SERVICE;

        @Override
        public Comparator<BackData> getCompareRank(boolean asd) {
            return (o1, o2) -> {
                if (asd) {
                    if ((double) o1.rankValue > (double) o2.rankValue) return 1;
                    else if((double) o1.rankValue == (double) o2.rankValue) return 0;
                    else return -1;
                } else {
                    if ((double) o1.rankValue > (double) o2.rankValue) return -1;
                    else if((double) o1.rankValue == (double) o2.rankValue) return 0;
                    else return 1;
                }
            };
        }



        //TODO
        @Override
        public List<PickleData> setRankValue(List<PickleData> pickleDatas, String code, LocalDate begin, LocalDate end, int holdPeriod) {
            List<FormationMOM> formationMOMS = pickStockService.getSingleCodeMOMInfo
                    (code, begin.minusDays(1), end, holdPeriod);
            List<StockPO> stockPOs = pickStockService
                    .getSingleCodeInfo(code, begin.minusDays(10), end.plusDays(10));



            int j = 0;
            int k = 0;



            /**
             * 暂时先算前一天的 因为begin就要买了!!!!!
             */


            for (int i = 0; i < pickleDatas.size(); i++) {
                int lastIndex = pickleDatas.get(i).stockCodes.size()-1;
                PickleData pickleData = pickleDatas.get(i);

                //如果这只股票本来在这期间就不存在 直接跳过  不用注入参数
                if(pickleData.stockCodes.size()==0||!pickleData.stockCodes.get(lastIndex).code.equals(code)) {
                    continue;
                }else if(formationMOMS == null){
                    //如果没有数据   删除当前列表最后一个 backdata  因为这只股票没有形成期的数据
                    pickleData.stockCodes.remove(lastIndex);
                    continue;
                }
                while (formationMOMS.get(j).date.isBefore(pickleData.beginDate)) {
                    j++;
                }

                double rank = formationMOMS.get(j).yeildRate;

                pickleData.stockCodes.get(lastIndex).mixRank[this.ordinal()] = rank;

            }


            return pickleDatas;
        }

//        @Override
//        public List<PickleData> setRankValue(List<PickleData> pickleDatas, List<String> codeList
//                , LocalDate begin, LocalDate end, int formationPeriod, List<StockPickIndexVO> stockPickIndexVOs) {
//            for (String code : codeList) {
//                List<FormationMOM> formationMOMs = pickStockService.getSingleCodeMOMInfo(code, begin, end, formationPeriod);
//                if (formationMOMs == null) {
////                    System.out.println(code);
//                    continue;
//                }
//                List<StockPO> stockPOs = pickStockService.getSingleCodeInfo(code, begin.minusDays(10), end.plusDays(10));
//                int j = 0;
//                int k = 0;
//                for (int i = 0; i < pickleDatas.size(); i++) {
//                    while (formationMOMs.get(j).date.isBefore(pickleDatas.get(i).beginDate))
//                        j++;
//                    double firstDayOpen;
//                    double lastDayClose;
//
//                    boolean isStop = false;
//                    while (stockPOs.get(k).getDate().isBefore(pickleDatas.get(i).beginDate))
//                        k++;
//                    if (k == 0) {
//                        continue;
//                    }
//                    firstDayOpen = stockPOs.get(k - 1).getAdjCloseIndex();
//                    if (stockPOs.get(k).getVolume() == 0)
//                        isStop = true;
//                    while (stockPOs.get(k).getDate().isBefore(pickleDatas.get(i).endDate)) {
//                        if (stockPOs.get(k).getVolume() == 0) {
//                            isStop = true;
//                            break;
//                        }
//                        k++;
//                    }
//                    if (stockPOs.get(k).getDate().isAfter(pickleDatas.get(i).endDate))
//                        k--;
//                    if (stockPOs.get(k).getVolume() == 0)
//                        isStop = true;
//                    lastDayClose = stockPOs.get(k).getAdjCloseIndex();
//                    if (!isStop) {
//                        pickleDatas.get(i).stockCodes.add(new BackData(code, formationMOMs.get(j).yeildRate, firstDayOpen, lastDayClose));
//                    }
//                }
//                for (StockPickIndexVO s : stockPickIndexVOs) {
//                    pickleDatas = s.stockPickIndex.setFilterValue(pickleDatas, code);
//                }
//
//
//            }
//
//            for (PickleData pickleData : pickleDatas) {
//                double sum = 0.0;
//                for (BackData backData : pickleData.stockCodes) {
//                    sum += (backData.lastDayClose - backData.firstDayOpen) / backData.firstDayOpen;
//                }
//                pickleData.baseProfitRate = sum / pickleData.stockCodes.size();
//            }
//            return pickleDatas;
//        }

        @Override
        public List<NewPickleData> setAllValue(List<String> codeList, LocalDate begin, LocalDate end, List<StockPickIndexVO> stockPickIndexVOs) {
            List<NewPickleData> newPickleDataList = codeList.stream().map(t -> {
                return new NewPickleData(t);
            }).collect(Collectors.toList());

            int codeIndex = 0;

            for (String code : codeList) {

//                RunTimeSt.getRunTime("开始读取 "+code+"股票");
                boolean isnull = false;

                List<StockPO> stockPOs = pickStockService
                        .getSingleCodeInfo(code, begin.minusDays(10), end.plusDays(10));

                int beforeIndex = 0;


                //System.out.println(stockPOs.get(beforeIndex).getDate());

                while (stockPOs.get(beforeIndex).getDate().isBefore(begin)) {
                    beforeIndex++;
                }

                newPickleDataList.get(codeIndex).lastAdj = stockPOs.get(beforeIndex - 1).getAdjCloseIndex();

                /**
                 * 初始化该股票  有数据的时候的值
                 * 例如 如果这只股票在2月1日 没有数据  就不放到singleBackDataList中
                 * 如果有数据就放入
                 */
                newPickleDataList.get(codeIndex).singleBackDataList =
                        stockPOs.stream()
                                .filter(t -> !(t.getDate().isBefore(begin) || t.getDate().isAfter(end))).map(t -> {
                            return new SingleBackData(t.getDate(), t.getOpen_Price()
                                    , t.getClose_Price(), t.getAdjCloseIndex(), t.getVolume());
                        }).collect(Collectors.toList());


                /**
                 * //TODO 还是太慢了   以后优化
                 *
                 * 分别计算1到60天形成期的数据
                 */

                //RunTimeSt.getRunTime(code+"  begin set rank");
                for (int holdPeriod = 1; holdPeriod <= 60 && !isnull; holdPeriod++) {

                    //TODO  返回来的数据可能数空的
                    List<FormationMOM> formationMOMS = pickStockService.getSingleCodeMOMInfo
                            (code, begin, end, holdPeriod);

                    //TODO  以后要做进一步的处理
                    if (formationMOMS == null) {
                        NewPickleData newPickleData = new NewPickleData(code);
                        newPickleDataList.remove(newPickleData);
                        isnull = true;
                        codeIndex--;
                        System.out.println(code);
                        continue;
                    }

                    int j = 0;

                    for (int i = 0; i < newPickleDataList.get(codeIndex).singleBackDataList.size(); i++) {
                        SingleBackData singleBackData = newPickleDataList.get(codeIndex).singleBackDataList.get(i);
                        //System.out.println(singleBackData.date==null);
                        while (formationMOMS.get(j).date.isBefore(singleBackData.date))
                            j++;

                        singleBackData.rankValues[holdPeriod] = formationMOMS.get(j).yeildRate;

                    }

                }
                //RunTimeSt.getRunTime(code+"  after set rank");


                // TODO 在此处加入  过滤参数的注入
                // !!!!!!!!!!!在此处加入  过滤参数的注入
                if(!isnull) {
                    for (StockPickIndexVO s : stockPickIndexVOs) {
                        newPickleDataList.get(codeIndex).singleBackDataList =
                                s.stockPickIndex.setNewFilterValue
                                        (newPickleDataList.get(codeIndex).singleBackDataList, code, codeIndex);
                    }
                }
                /**
                 * 在底层计算相对的收益情况 (基准的收益情况）
                 */


                codeIndex++;

            }


            return newPickleDataList;
        }
    }
}

