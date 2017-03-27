package pick;

import blserviceimpl.strategy.BackData;
import blserviceimpl.strategy.PickleData;
import dataservice.singlestock.StockDAO;
import dataserviceimpl.singlestock.StockDAOImpl;
import po.StockPO;
import util.DayMA;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wshwbluebird on 2017/3/26.
 */
public enum PickStockServiceImpl implements PickStockService {
    PICK_STOCK_SERVICE;



    private StockDAO stockDAO;

     PickStockServiceImpl(){
        this.stockDAO = StockDAOImpl.STOCK_DAO_IMPL;
    }

    public void setStockDAO(StockDAO stockDAO){
        this.stockDAO = stockDAO;
    }


    @Override
    public List<StockPO> getSingleCodeInfo(String code, LocalDate begin, LocalDate end) {
        return stockDAO.getStockInFoInRangeDate(code,begin,end);
    }

    @Override
    public List<PickleData> seprateDaysinCommon(LocalDate begin, LocalDate end, int sep) {
        List<PickleData>  pickleDatas = new ArrayList<>();


        while(!begin.plusDays(sep).isAfter(end)){
            if(begin.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                    || begin.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                begin = begin.plusDays(1);
            } else {
                pickleDatas.add(new PickleData(begin,begin.plusDays(sep),new ArrayList<BackData>())) ;
                begin = begin.plusDays(sep);
            }
        }

        if(!begin.isAfter(end)  && !(begin.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                || begin.getDayOfWeek().equals(DayOfWeek.SUNDAY))){
            pickleDatas.add(new PickleData(begin,begin.plusDays(sep),new ArrayList<BackData>())) ;
        }

        return pickleDatas;

    }

    @Override
    public List<DayMA> getSingleCodeMAInfo(String code, LocalDate begin, LocalDate end, int days) {
        List<StockPO> list = stockDAO.getStockInFoInRangeDate(code , begin.minusDays(days+180) , end);
        Collections.reverse(list);

        List<DayMA> ans = new ArrayList<>();

        //用于计算和
        double sum = 0;

        //用于计算算了几天的均线
        int js = 0;

        //用于计算adj为0的天数
        int none = 0;

        //用于计算要计算几天的均线
        long betweendays = end.toEpochDay() - begin.toEpochDay()+1;

        double ma = 0;
        for (int i = 0 ; js < betweendays ;i++){
            //System.out.println("i:  "+i);
            double adj = list.get(i).getAdjCloseIndex();
            if(ma!=0  && (end.minusDays(js).getDayOfWeek().equals(DayOfWeek.SATURDAY)
                            || end.minusDays(js).getDayOfWeek().equals(DayOfWeek.FRIDAY))){
                DayMA dayMA = new DayMA(end.minusDays(js),ma);
                ans.add(dayMA);
                js++;
                continue;
            }
            if(adj!=0) {
                sum+= adj;
                if(i+1>= days){


                    ma = sum/days;
                    DayMA dayMA = new DayMA(end.minusDays(js),ma);
                    ans.add(dayMA);
                    sum -= list.get(i-days+1-none).getAdjCloseIndex();
                    js++;
                }
            }else {
                none++;
            }

        }
        Collections.reverse(ans);
        return ans;
    }
}
