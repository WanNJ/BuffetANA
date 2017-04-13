package vo;

/**
 * Created by wshwbluebird on 2017/4/13.
 */
public class ExtraNormHistVO {

    public double small;

    public double big;

    /**
     * 实际的值
     */
    public double real;

    /**
     * 拟合后的值
     */
    public double similar;


    /**
     * 初始化 额外数据值
     * @param big
     * @param small
     * @param real
     * @param similar
     */
    public ExtraNormHistVO(double big , double small , double real , double similar){
        this.big = big;
        this.small = small;
        this.real = real;
        this.similar = similar;
    }

    public ExtraNormHistVO(){

    }
}
