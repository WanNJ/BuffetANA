package vo;

import stockenum.StockPool;

import java.util.Set;

/**
 * Created by slow_time on 2017/3/24.
 */
public class StockPoolConditionVO {

    /**
     * 股票池的类型
     */
    public StockPool stockPool;
    /**
     * 所属板块的集合
     */
    public Set<String> block;
    /**
     * 所属行业的集合
     */
    public Set<String> industry;
    /**
     * 是否排除ST股票
     */
    public boolean excludeST;

//    public StockPoolConditionVO() {
//
//    }

    public StockPoolConditionVO(StockPool stockPool, Set<String> block, Set<String> industry, boolean excludeST) {
        this.stockPool = stockPool;
        this.block = block;
        this.industry = industry;
        this.excludeST = excludeST;
    }
}
