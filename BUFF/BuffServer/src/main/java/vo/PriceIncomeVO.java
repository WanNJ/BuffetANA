package vo;

/**
 * Created by wshwbluebird on 2017/4/14.
 */

/**
 * 价格和收益比 piece
 */
public class PriceIncomeVO {
    public double price;

    public double incomeRate;



    public PriceIncomeVO(double price, double incomeRate){
        this.incomeRate = incomeRate;

        this.price = price;
    }
}
