package blservice.exception;

/**
 * Created by Accident on 2017/3/16.
 */
public class InvalidDateException extends Exception {
    public void printReason() {
        System.out.println("该日期范围无数据");
    }
}
