/**
 * Created by slow_time on 2017/5/11.
 */
exports.BackData = class {
    constructor(code, name, rankValue, mixRank, filterData, firstDayOpen, lastDayClose) {
        this.code = code;
        this.name = name;
        this.rankValue = rankValue;
        this.mixRank = mixRank;
        this.filterData = filterData;
        this.firstDayOpen = firstDayOpen;
        this.lastDayClose = lastDayClose;
    }
};