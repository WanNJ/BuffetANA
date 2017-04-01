package pick;

import blserviceimpl.strategy.BackData;
import blserviceimpl.strategy.PickleData;
import dataservice.singlestock.StockDAO;
import dataserviceimpl.singlestock.StockDAOImpl;
import po.StockPO;
import util.DayMA;
import util.FormationMOM;
import vo.LongPeiceVO;

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
    PICK_STOCK_SERVICE ;

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

        LocalDate temp = end;

        double ma = 0;
        int i;
        for ( i = 0 ; js < days  ;i++){
           if(list.get(i).getAdjCloseIndex()!=0){
               js++;
               sum+= list.get(i).getAdjCloseIndex();
           }
        }

        js = 0;
        for(;!temp.isBefore(begin);i++){
            if(list.get(i).getAdjCloseIndex()!=0){
                ma = sum/days;
                while(list.get(js).getDate().isBefore(temp)){
                    ans.add(new DayMA(temp,ma));
                    temp = temp.minusDays(1);
                }
                ans.add(new DayMA(temp,ma));
                temp = temp.minusDays(1);
                sum+=list.get(i).getAdjCloseIndex()-list.get(js).getAdjCloseIndex();
                js++;
            }
        }
        Collections.reverse(ans);
        return ans;
    }

    @Override
    public List<FormationMOM> getSingleCodeMOMInfo(String code, LocalDate begin, LocalDate end, int formationPeriod) {
        List<StockPO> list = stockDAO.getStockInFoInRangeDate(code , begin.minusDays(formationPeriod+180) , end);
        Collections.reverse(list);

        List<FormationMOM> codeYields = new ArrayList<>();

        //用于保存已经取到list中哪一天的数据
        int jsBegin = 1;
        int jsEnd = 1;

        //找到第一个不是停盘日的日期
        while(list.get(jsBegin).getVolume() == 0) {
            jsBegin++;
            if(jsBegin == list.size())
                return null;
            jsEnd++;
        }

        //将jsBegin往前推一个形成期的交易日
        int days = 1;    //用于计数，已经过了几个交易日
        while(days < formationPeriod) {
            jsBegin++;
            if(list.get(jsBegin).getVolume() != 0)
                days++;
        }

        //先计算最后一天的收益率
        double yieldRate = (list.get(jsEnd).getAdjCloseIndex() - list.get(jsBegin).getAdjCloseIndex()) / list.get(jsBegin).getAdjCloseIndex();
        FormationMOM formationMOM = new FormationMOM(end, yieldRate);
        codeYields.add(formationMOM);

        LocalDate temp = end.minusDays(1);
        //用于保存当前日期在list中的位置
        days = 1;

        while(!temp.isBefore(begin)) {
            while(!temp.isEqual(list.get(days).getDate())) {
                codeYields.add(new FormationMOM(codeYields.get(codeYields.size() - 1)));
                temp = temp.minusDays(1);
            }
            //若果这一天停盘，那么他的收益率应该与后一天一样
            if(list.get(days).getAdjCloseIndex() == 0) {
                codeYields.add(new FormationMOM(codeYields.get(days - 1)));
            }
            else {
                while(true) {
                    jsBegin++;
                    if(jsBegin == list.size())
                        return null;
                    if(list.get(jsBegin).getVolume() != 0)
                        break;
                }
                while(true) {
                    jsEnd++;
                    if(list.get(jsEnd).getVolume() != 0)
                        break;
                }
                double yield = (list.get(jsEnd).getAdjCloseIndex() - list.get(jsBegin).getAdjCloseIndex()) / list.get(jsBegin).getAdjCloseIndex();
                FormationMOM formation = new FormationMOM(temp, yield);
                codeYields.add(formation);
            }
            temp = temp.minusDays(1);
            days++;
        }

        Collections.reverse(codeYields);
        return codeYields;
    }





    @Override
    public List<LongPeiceVO> getLastVol(String code, LocalDate begin, LocalDate end) {
        List<StockPO> list = stockDAO.getStockInFoInRangeDate(code , begin.minusDays(10) , end);
        Collections.reverse(list);

        list = list.stream().filter(t->t.getVolume()>0).collect(Collectors.toList());
        List<LongPeiceVO> ans = new ArrayList<>();
        LocalDate temp = end;

        //list.stream().forEach(t-> System.out.println(t.getDate()+"   "+t.getVolume()));
       // System.out.println(list.size());
        //if(list.size()<0)
        for (int i = 0 ; !temp.isBefore(begin);i++){
            if(i>list.size()-1){
                //System.out.println(i);
                while(!temp.isBefore(begin)){
                    ans.add(new LongPeiceVO(temp,0));
                    temp = temp.minusDays(1);
                }

                Collections.reverse(ans);
                return ans;
            }


            while(temp.isAfter(list.get(i).getDate())){
                ans.add(new LongPeiceVO(temp,list.get(i).getVolume()));
                temp = temp.minusDays(1);
            }
            try {
                ans.add(new LongPeiceVO(temp,list.get(i+1).getVolume()));
                temp = temp.minusDays(1);
            }catch (Exception e){
                while(!temp.isBefore(begin)){
                    ans.add(new LongPeiceVO(temp,0));
                    temp = temp.minusDays(1);
                }
                Collections.reverse(ans);
                return ans;
            }

        }

        Collections.reverse(ans);
        return ans;
    }

}
