/**
 * Created by slow_time on 2017/5/11.
 */
exports.PickleData = class {
    constructor(beginDate, endDate, backDatas, baseProfitRate) {
        this.begindate = beginDate;
        this.endDate = endDate;
        this.backDatas = backDatas;
        this.baseProfitRate = baseProfitRate;
    }
};