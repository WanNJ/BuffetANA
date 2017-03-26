package po;

import stockenum.StockPool;
import vo.StockPoolConditionVO;

import java.util.Set;

/**
 * Created by slow_time on 2017/3/24.
 */
public class StockPoolConditionPO {

    /**
     * 股票池的类型
     */
    private StockPool stockPool;
    /**
     * 所属板块的集合
     */
    private Set<String> block;
    /**
     * 所属行业的集合
     */
    private Set<String> industry;
    /**
     * 是否排除ST股票
     */
    private boolean excludeST;

    public StockPoolConditionPO() {

    }

    public StockPoolConditionPO(StockPool stockPool, Set<String> block, Set<String> industry, boolean excludeST) {
        this.stockPool = stockPool;
        this.block = block;
        this.industry = industry;
        this.excludeST = excludeST;
    }


    /**
     * 快速的po转vo  我不太清楚  是不是应该接口就定VO??
     * add by wsw
     * @param stockPoolConditionVO
     */
    public StockPoolConditionPO(StockPoolConditionVO stockPoolConditionVO){
        this.stockPool = stockPoolConditionVO.stockPool;
        this.block = stockPoolConditionVO.block;
        this.industry = stockPoolConditionVO.industry;
        this.excludeST = stockPoolConditionVO.excludeST;
    }

    public StockPool getStockPool() {
        return stockPool;
    }

    public void setStockPool(StockPool stockPool) {
        this.stockPool = stockPool;
    }

    public Set<String> getBlock() {
        return block;
    }

    public void setBlock(Set<String> block) {
        this.block = block;
    }

    public Set<String> getIndustry() {
        return industry;
    }

    public void setIndustry(Set<String> industry) {
        this.industry = industry;
    }

    public boolean isExcludeST() {
        return excludeST;
    }

    public void setExcludeST(boolean excludeST) {
        this.excludeST = excludeST;
    }
}
