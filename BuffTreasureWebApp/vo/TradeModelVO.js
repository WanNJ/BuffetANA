/**
 * Created by slow_time on 2017/5/11.
 */
exports.TradeModelVO = class {
    constructor(holdingDays, holdingNums, winRate = null, loserate = null) {
        this.holdingDays = holdingDays;
        this.holdingNums = holdingNums;
        this.winRate = winRate;
        this.loseRate = loserate;
    }
};