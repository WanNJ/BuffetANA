/**
 * Created by slow_time on 2017/5/11.
 */
exports.BackData = class {
    /**
     * VO构造函数
     * @param code {String} 股票代码
     * @param name {String} 股票名称
     * @param rankValue {Number} 用于比较的值
     * @param mixRank {Array} 混合策略中各个策略的比较值构成的数组
     * @param filterData {Array} 过滤参数
     * @param firstDayOpen {Number} 第一天买入时的价格。对应第一天的开盘价
     * @param lastDayClose {Number} 最后一天卖出时的价格，对应最后一天的收盘价
     * @param valid {bool} 这几天是否有停盘  有的话 flase 没有就是 true
     */
    constructor(code, name, rankValue, mixRank, filterData, firstDayOpen, lastDayClose ,valid) {
        this.code = code;
        this.name = name;
        /**
         * 传进来用来比较数据的值
         */
        this.rankValue = rankValue;
        this.mixRank = mixRank;
        this.filterData = filterData;
        /**
         * 第一天买入时的价格。对应第一天的开盘价
         * adjust by wsw 也可能是前一天的复权收盘价
         */
        this.firstDayOpen = firstDayOpen;

        /**
         * 最后一天卖出时的价格，对应最后一天的收盘价
         */
        this.lastDayClose = lastDayClose;
        this.valid = valid;
    }
};