package vo;

/**
 * Created by wshwbluebird on 2017/3/24.
 */

/**
 * 用于画优化图的
 * 给出x轴和y轴
 */
public class BetterPieceVO {

    public int days; //作为横轴的 持有期或者 形成期的天数

    public double rate;  //赢率 或者 超额收益


    /**
     * 提供的无参数的初始化方法  不要删!
     */
    public BetterPieceVO(){

    }

    /**
     * 有参数的初始化方法
     * @param days
     * @param rate
     */
    public  BetterPieceVO(int days , double rate){
        this.days = days;
        this.rate  =rate;
    }

}
