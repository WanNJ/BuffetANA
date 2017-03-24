package vo;

/**
 * Created by wshwbluebird on 2017/3/24.
 */
public class BackDetailVO {

    public double yearProfitRate;  //年华收益率

    public double totalProfitRate;  //总收益

    public double sharpRate ;  // 夏普率

    public double  largestBackRate;  // 最大回撤率

    public double alpha;

    public double beta;

    public double fluctuaionRate ; //波动率  可以暂时先不算


    /**
     * 无参数初始化   别删!!!!
     */
    public BackDetailVO(){

    }


    /**
     * 参数初始化
     * @param yearProfitRate
     * @param totalProfitRate
     * @param sharpRate
     * @param largestBackRate
     * @param alpha
     * @param beta
     * @param fluctuaionRate
     */
    public BackDetailVO(double yearProfitRate , double  totalProfitRate ,
                        double sharpRate,double largestBackRate , double alpha ,double beta,
                        double fluctuaionRate){
        this.alpha = alpha;
        this.beta = beta;
        this.largestBackRate = largestBackRate;
        this.sharpRate = sharpRate;
        this.yearProfitRate = yearProfitRate;
        this.totalProfitRate = totalProfitRate;
        this.fluctuaionRate = fluctuaionRate;
    }




}
