package pick;

import blserviceimpl.strategy.PickleData;
import util.DayMA;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/3/26.
 */
public enum PickStockServiceImpl implements PickStockService {
    PICK_STOCK_SERVICE;

    @Override
    public List<PickleData> seprateDaysinCommon(LocalDate begin, LocalDate end, int sep) {
        List<PickleData>  pickleDatas = new ArrayList<>();


        while(!begin.plusDays(sep).isAfter(end)){
            if(begin.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                    || begin.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                begin = begin.plusDays(1);
            } else {
                pickleDatas.add(new PickleData(begin,begin.plusDays(sep),null)) ;
                begin = begin.plusDays(sep);
            }
        }

        return pickleDatas;

    }

    @Override
    public List<DayMA> getSingleCodeMAInfo(String code, LocalDate begin, LocalDate end, int days) {
        return null;
    }
}
