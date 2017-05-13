/**
 * Created by slow_time on 2017/5/11.
 */
exports.PickleData = class {
    /**
     * VO构造函数
     * @param beginDate {Date} 这段持仓期的开始日期
     * @param endDate {Date} 这段持仓期的截止日期
     * @param backDatas {Array} 这段持仓期内所持的股票
     * @param baseProfitRate {Number} 持仓期内的基准收益率
     */
    constructor(beginDate, endDate, backDatas = [], baseProfitRate = 0) {
        this.begindate = beginDate;
        this.endDate = endDate;
        this.backDatas = backDatas;
        this.baseProfitRate = baseProfitRate;
    }
};