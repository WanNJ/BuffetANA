package blservice.exception;

/**
 * Created by Accident on 2017/3/16.
 */
public class InvalidStockCodeException extends Exception {
    public void printReason() {
        System.out.println("该股票代号不存在");
    }
}
