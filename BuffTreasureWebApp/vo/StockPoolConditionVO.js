/**
 * Created by slow_time on 2017/5/9.
 */
exports.StockPoolConditionVO = class {
    /**
     * 构造函数
     * @param stockPool     String 股票池，暂定包括（"全部"，"沪深A股"，"中小板"，"创业板"，"自选股票池"）
     * @param benches       Array  板块列表，列表里的值类型为String，只有当stockPool为"自选股票池"时，才会有值，否则为null
     * @param industries    Array  行业列表，列表里的值类型为String，只有当stockPool为"自选股票池"时，才会有值，否则为null
     * @param excludeST     Bool   是否排除ST股，排除为true，不排除为false
     */
    constructor (stockPool, benches, industries, excludeST) {
        this.stockPool = stockPool;
        this.benches = benches;
        this.industries = industries;
        this.excludeST = excludeST;
    }
};