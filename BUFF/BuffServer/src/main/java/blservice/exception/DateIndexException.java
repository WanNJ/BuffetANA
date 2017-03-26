package blservice.exception;

import util.DateUtil;

import java.time.LocalDate;

/**
 * Created by slow_time on 2017/3/8.
 */
public class DateIndexException extends Exception {

    private LocalDate beginDate;
    private LocalDate endDate;

    public DateIndexException(LocalDate beginDate, LocalDate endDate) {
        this.beginDate = beginDate;
        this.endDate = endDate;
    }
    @Override
    public void printStackTrace() {
        System.out.println("The beginDate: " + DateUtil.formatLine(beginDate) + ", is after the endDate: " +
        DateUtil.formatLine(endDate) + "Which is unreasonable.");
    }
}
