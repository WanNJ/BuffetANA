package util;

import blservice.exception.RangeException;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.stream.Stream;

/**
 * 日期的范围的封装
 * Created by zjy on 2017/3/5.
 * @author zjy
 */
public class DateRange implements Serializable{
    private LocalDate from;
    private LocalDate to;

    /**
     * 封装日期范围
     * @param from 开始日期
     * @param to 结束日期
     * @throws RangeException 开始日期在结束日期之后时
     */
    public DateRange(LocalDate from, LocalDate to) throws RangeException {
        if(from.isAfter(to)){
            throw new RangeException(from,to);
        }else {
            this.from = from;
            this.to = to;
        }
    }


    /**
     * 获取实际的天数范围
     * @return
     */
    public int getRangeDays(){
        return to.compareTo(from)+1;
    }


    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }
}
