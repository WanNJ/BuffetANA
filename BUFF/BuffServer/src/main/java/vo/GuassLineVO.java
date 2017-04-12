package vo;

/**
 * Created by wshwbluebird on 2017/4/12.
 */
public class GuassLineVO {

    //正太分布的横轴参数
    public double middle;

    //正太分布曲线的纵轴参数
    public double value;


    /**
     * init
     * @param middle
     * @param value
     */
    public GuassLineVO(double middle , double value){
        this.middle = middle;
        this.value = value;
    }
}
