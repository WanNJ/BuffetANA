package blservice.exception;

/**
 * 范围错误异常
 * Created by zjy on 2017/3/5.
 * @author zjy
 */
public class RangeException extends Exception{
    private Comparable from;
    private Comparable to;
    private String reason;

    /**
     *
     * @param from 范围的开始
     * @param to 范围的结束
     */
    public RangeException(Comparable from, Comparable to) {
        this.from = from;
        this.to = to;
    }

    /**
     *
     * @param from 范围的开始
     * @param to 范围的结束
     * @param reason 原因
     */
    public RangeException(Comparable from, Comparable to, String reason) {
        this.from = from;
        this.to = to;
        this.reason = reason;
    }
}
