/**
 * Created by slow_time on 2017/5/11.
 */
exports.TradeModelVO = class {
    /**
     * 构造函数
     * @param holdingDays {Number}    持仓期
     * @param holdingNums {Number}    持股数
     * @param winRate {Number}        止盈率（为小数，如0.2）
     * @param loserate {Number}       止损率（为小数，如0.1）
     */
    constructor(holdingDays, holdingNums, winRate = null, loserate = null) {
        this.holdingDays = holdingDays;
        this.holdingNums = holdingNums;
        this.winRate = winRate;
        this.loseRate = loserate;
    }
};