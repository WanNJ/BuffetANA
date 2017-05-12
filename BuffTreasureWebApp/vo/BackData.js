/**
 * Created by slow_time on 2017/5/11.
 */
exports.BackData = class {
    constructor(code, rankValue, mixRank, filterData, firstDayOpen, lastDayClose) {
        this.code = code;
        this.rankValue = rankValue;
        this.mixRank = mixRank;
        this.filterData = filterData;
        this.firstDayOpen = firstDayOpen;
        this.lastDayClose = lastDayClose;
    }
};