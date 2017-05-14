/**
 * Created by wshwbluebird on 2017/5/14.
 */


let singleStockDB = require('../../models/singleStock').singleStockDB;

/**
 * 是一个Map集合
 * key是Date类型
 * value是JSON格式 {
                        "MACD_DIF" : Number,
                        "MACD_DEA" : Number,
                        "MACD" : Number,
                        "RSV" : Number,
                        "KDJ_K" : Number,
                        "KDJ_D" : Number,
                        "KDJ_J" : Number,
                        "RSI" : Number
                    }
 *
 */
let dailyData;
let preCode = '';
/**
 *
 * @param code 股票代码
 * @param beginDate 开始日期
 * @param endDate  结束日期
 * @param formationPeriod  形成期(观察期)
 * @param callback 形如(err,docs);
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *  所有 这个预先计算的部分  返回的数组元素都按照这格式
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *      docs 形式如下
 *      {
 *        date:  date
 *        value  : MA偏离量
 *      } 的数组
 *      时间按照从小到大排
 *      !!!数组中没有value的date 不存在
 *      比如2014-01-02 没有MA均线  那么返回的数组里面没有 date 2014-01-02 就根本不会出现
 */
exports.calculateMAValue = function (code ,beginDate , endDate, formationPeriod ,callback) {
    // TODO 应该是24000*3600吧？
    let searchBeginDate = new Date(beginDate-(formationPeriod * 15+20)*24000*3600);
    singleStockDB.getStockInfoInRangeDate(code ,searchBeginDate,endDate ,(err,doc) => {
        doc.reverse();
        let curMASum = 0;
        let MAValue = [];


        /**
         * added by TY
         * TODO filter方法并不能改变原来的数组，应该需要修改！！！！
         */
        doc  = doc.filter(data => data["volume"]!==0);

        for(let i = 0; i < doc.length && i < formationPeriod ; i++){
            curMASum+= doc[i]["adjClose"];
        }


        for(let i  = 0;  i+formationPeriod < doc.length && doc[i]["date"] -beginDate >= 0; i++){

            let temp = (curMASum - doc[i]["adjClose"])/curMASum;
            let part = {
                "date" : doc[i]["date"],
                "value" : temp
            };
            MAValue.push(part);
            /**
             * added by TY
             * TODO 此处的 i+formationPeriod 有可能存在数组下标越界的可能，应该加一个判断
             */
            curMASum += doc[i+formationPeriod]["adjClose"] - doc[i]["adjClose"];
        }
        MAValue.reverse();
        callback(err,MAValue);
    });
};


/**
 * 获得动量排序值
 * @param code
 * @param beginDate
 * @param endDate
 * @param formationPeriod
 * @param callback 形如(err,docs);
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *  所有 这个预先计算的部分  返回的数组元素都按照这格式
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *      docs 形式如下
 *      {
 *        date:  date
 *        value  : MA偏离量
 *      } 的数组
 *      时间按照从小到大排
 *      !!!数组中没有value的date 不存在
 *      比如2014-01-02 没有MOM值  那么返回的数组里面没有 date 2014-01-02 就根本不会出现
 */
exports.calculateMOMValue = function (code ,beginDate , endDate, formationPeriod ,callback) {
    let searchBeginDate = new Date(beginDate-(formationPeriod * 10+20)*24000*3600);
    singleStockDB.getStockInfoInRangeDate(code, searchBeginDate, endDate, (err, docs) => {
        docs.reverse();
        /**
         * 计算观察期的收益率时的临时变量
         */
        let beginAdj = 0;  // 观察期开始时的复权收盘价
        let endAdj = 0;    // 观察期结束时的复权收盘价

        let MOMValue = [];

        docs = docs.filter(data => data["volume"] !== 0);
        for (let i = 1; docs[i]["date"] - beginDate >= 0 && i + formationPeriod < docs.length; i++) {
            endAdj = docs[i]["adjClose"];
            beginAdj = docs[i + formationPeriod]["adjClose"];
            // 该观察内的收益率
            let yield_rate = (endAdj - beginAdj) / beginAdj;
            MOMValue.push({
                "date" : docs[i - 1]["date"],
                "value" : yield_rate
            });
        }
        MOMValue.reverse();
        callback(err, MOMValue);
    });
};


/**
 * 获得RSI的排序值
 * @param code
 * @param beginDate
 * @param endDate
 * @param formationPeriod
 * @param callback 形如(err,docs);
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *  所有 这个预先计算的部分  返回的数组元素都按照这格式
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *      docs 形式如下
 *      {
 *        date:  date
 *        value  : MA偏离量
 *      } 的数组
 *      时间按照从小到大排
 *      !!!数组中没有value的date 不存在
 *      比如2014-01-02 没有RSI值  那么返回的数组里面没有 date 2014-01-02 就根本不会出现
 */
exports.calculateRSIValue = function (code ,beginDate , endDate, formationPeriod ,callback) {
    getDailyData(code, beginDate, endDate, (err, docs) => {
        if (err)
            callback(err, null);
        else {
            let RSIValue = [];
            for (let i = beginDate; i <= endDate; i = new Date(i.getTime() + 24000 * 3600)) {
                if (docs.has(i.toISOString().substr(0, 10))) {
                    RSIValue.push({
                        "date" : i,
                        "value" : docs.get(i.toISOString().substr(0, 10))["RSI"]
                    });
                }
            }
            callback(null, RSIValue);
        }
    });
};


/**
 * 获得MACD_DIF的排序值
 * @param code
 * @param beginDate
 * @param endDate
 * @param formationPeriod
 * @param callback 形如(err,docs);
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *  所有 这个预先计算的部分  返回的数组元素都按照这格式
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *      docs 形式如下
 *      {
 *        date:  date
 *        value  : MA偏离量
 *      } 的数组
 *      时间按照从小到大排
 *      !!!数组中没有value的date 不存在
 *      比如2014-01-02 没有MACD_DIF值  那么返回的数组里面没有 date 2014-01-02 就根本不会出现
 */
exports.calculateMACD_DIFValue = function (code ,beginDate , endDate, formationPeriod ,callback) {
    getDailyData(code, beginDate, endDate, (err, docs) => {
        if (err)
            callback(err, null);
        else {
            let MACD_DIFValue = [];
            for (let i = beginDate; i <= endDate; i = new Date(i.getTime() + 24000 * 3600)) {
                if (docs.has(i.toISOString().substr(0, 10))) {
                    MACD_DIFValue.push({
                        "date" : i,
                        "value" : docs.get(i.toISOString().substr(0, 10))["MACD_DIF"]
                    });
                }
            }
            callback(null, MACD_DIFValue);
        }
    });
};


/**
 * 获得MACD_DEA的排序值
 * @param code
 * @param beginDate
 * @param endDate
 * @param formationPeriod
 * @param callback 形如(err,docs);
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *  所有 这个预先计算的部分  返回的数组元素都按照这格式
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *      docs 形式如下
 *      {
 *        date:  date
 *        value  : MA偏离量
 *      } 的数组
 *      时间按照从小到大排
 *      !!!数组中没有value的date 不存在
 *      比如2014-01-02 没有MACD_DEA值  那么返回的数组里面没有 date 2014-01-02 就根本不会出现
 */
exports.calculateMACD_DEAValue = function (code ,beginDate , endDate, formationPeriod ,callback) {
    getDailyData(code, beginDate, endDate, (err, docs) => {
        if (err)
            callback(err, null);
        else {
            let MACD_DEAValue = [];
            for (let i = beginDate; i <= endDate; i = new Date(i.getTime() + 24000 * 3600)) {
                if (docs.has(i.toISOString().substr(0, 10))) {
                    MACD_DEAValue.push({
                        "date" : i,
                        "value" : docs.get(i.toISOString().substr(0, 10))["MACD_DEA"]
                    });
                }
            }
            callback(null, MACD_DEAValue);
        }
    });
};


/**
 * 获得MACD的排序值
 * @param code
 * @param beginDate
 * @param endDate
 * @param formationPeriod
 * @param callback 形如(err,docs);
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *  所有 这个预先计算的部分  返回的数组元素都按照这格式
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *      docs 形式如下
 *      {
 *        date:  date
 *        value  : MA偏离量
 *      } 的数组
 *      时间按照从小到大排
 *      !!!数组中没有value的date 不存在
 *      比如2014-01-02 没有MACD值  那么返回的数组里面没有 date 2014-01-02 就根本不会出现
 */
exports.calculateMACDValue = function (code ,beginDate , endDate, formationPeriod ,callback) {
    getDailyData(code, beginDate, endDate, (err, docs) => {
        if (err)
            callback(err, null);
        else {
            let MACDValue = [];
            for (let i = beginDate; i <= endDate; i = new Date(i.getTime() + 24000 * 3600)) {
                if (docs.has(i.toISOString().substr(0, 10))) {
                    MACDValue.push({
                        "date" : i,
                        "value" : docs.get(i.toISOString().substr(0, 10))["MACD"]
                    });
                }
            }
            callback(null, MACDValue);
        }
    });
};


/**
 * 获得RSV的排序值
 * @param code
 * @param beginDate
 * @param endDate
 * @param formationPeriod
 * @param callback 形如(err,docs);
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *  所有 这个预先计算的部分  返回的数组元素都按照这格式
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *      docs 形式如下
 *      {
 *        date:  date
 *        value  : MA偏离量
 *      } 的数组
 *      时间按照从小到大排
 *      !!!数组中没有value的date 不存在
 *      比如2014-01-02 没有RSV值  那么返回的数组里面没有 date 2014-01-02 就根本不会出现
 */
exports.calculateRSVValue = function (code ,beginDate , endDate, formationPeriod ,callback) {
    getDailyData(code, beginDate, endDate, (err, docs) => {
        if (err)
            callback(err, null);
        else {
            let RSVValue = [];
            for (let i = beginDate; i <= endDate; i = new Date(i.getTime() + 24000 * 3600)) {
                if (docs.has(i.toISOString().substr(0, 10))) {
                    RSVValue.push({
                        "date" : i,
                        "value" : docs.get(i.toISOString().substr(0, 10))["RSV"]
                    });
                }
            }
            callback(null, RSVValue);
        }
    });
};

/**
 * 获得KDJ_K的排序值
 * @param code
 * @param beginDate
 * @param endDate
 * @param formationPeriod
 * @param callback 形如(err,docs);
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *  所有 这个预先计算的部分  返回的数组元素都按照这格式
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *      docs 形式如下
 *      {
 *        date:  date
 *        value  : MA偏离量
 *      } 的数组
 *      时间按照从小到大排
 *      !!!数组中没有value的date 不存在
 *      比如2014-01-02 没有KDJ_K值  那么返回的数组里面没有 date 2014-01-02 就根本不会出现
 */
exports.calculateKDJ_KValue = function (code ,beginDate , endDate, formationPeriod ,callback) {
    getDailyData(code, beginDate, endDate, (err, docs) => {
        if (err)
            callback(err, null);
        else {
            let KDJ_KValue = [];
            for (let i = beginDate; i <= endDate; i = new Date(i.getTime() + 24000 * 3600)) {
                if (docs.has(i.toISOString().substr(0, 10))) {
                    KDJ_KValue.push({
                        "date" : i,
                        "value" : docs.get(i.toISOString().substr(0, 10))["KDJ_K"]
                    });
                }
            }
            callback(null, KDJ_KValue);
        }
    });
};


/**
 * 获得KDJ_D的排序值
 * @param code
 * @param beginDate
 * @param endDate
 * @param formationPeriod
 * @param callback 形如(err,docs);
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *  所有 这个预先计算的部分  返回的数组元素都按照这格式
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *      docs 形式如下
 *      {
 *        date:  date
 *        value  : MA偏离量
 *      } 的数组
 *      时间按照从小到大排
 *      !!!数组中没有value的date 不存在
 *      比如2014-01-02 没有KDJ_D值  那么返回的数组里面没有 date 2014-01-02 就根本不会出现
 */
exports.calculateKDJ_DValue = function (code ,beginDate , endDate, formationPeriod ,callback) {
    getDailyData(code, beginDate, endDate, (err, docs) => {
        if (err)
            callback(err, null);
        else {
            let KDJ_DValue = [];
            for (let i = beginDate; i <= endDate; i = new Date(i.getTime() + 24000 * 3600)) {
                if (docs.has(i.toISOString().substr(0, 10))) {
                    KDJ_DValue.push({
                        "date" : i,
                        "value" : docs.get(i.toISOString().substr(0, 10))["KDJ_D"]
                    });
                }
            }
            callback(null, KDJ_DValue);
        }
    });
};


/**
 * 获得KDJ_J的排序值
 * @param code
 * @param beginDate
 * @param endDate
 * @param formationPeriod
 * @param callback 形如(err,docs);
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *  所有 这个预先计算的部分  返回的数组元素都按照这格式
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *      docs 形式如下
 *      {
 *        date:  date
 *        value  : MA偏离量
 *      } 的数组
 *      时间按照从小到大排
 *      !!!数组中没有value的date 不存在
 *      比如2014-01-02 没有KDJ_J值  那么返回的数组里面没有 date 2014-01-02 就根本不会出现
 */
exports.calculateKDJ_JValue = function (code ,beginDate , endDate, formationPeriod ,callback) {
    getDailyData(code, beginDate, endDate, (err, docs) => {
        if (err)
            callback(err, null);
        else {
            let KDJ_JValue = [];
            for (let i = beginDate; i <= endDate; i = new Date(i.getTime() + 24000 * 3600)) {
                if (docs.has(i.toISOString().substr(0, 10))) {
                    KDJ_JValue.push({
                        "date" : i,
                        "value" : docs.get(i.toISOString().substr(0, 10))["KDJ_J"]
                    });
                }
            }
            callback(null, KDJ_JValue);
        }
    });
};


/**
 * ==========================================私有的计算方法=================================================
 */
/**
 * 用于一次性获得所有需要周期性计算的rankValue值，减少计算的开销
 * @param code
 * @param beginDate
 * @param endDate
 * @param callback
 */
function getDailyData(code, beginDate, endDate, callback) {
    if (typeof dailyData === "undefined" || preCode !== code) {
        // 往前推600天计算
        let searchBeginDate = new Date(beginDate - 600*24000*3600);
        singleStockDB.getStockInfoInRangeDate(code, searchBeginDate, endDate, (err, docs) => {
            if (err)
                callback(err, null);
            else {
                dailyData = new Map();
                preCode = code;
                // 计算KDJ所需要的临时数据
                let beforeK = 50; // 前日K值
                let beforeD = 50; // 前日D值
                let periodHigh = [];
                let periodLow = [];
                // 计算MACD所需要的临时数据
                let beforeEMA12 = 0; // 昨日的12日指数平均值
                let beforeEMA26 = 0; // 昨日的26日指数平均值
                let beforeDEA = 0; // 昨日的九日DIF平滑移动平均值
                // 计算RSI所需要的临时数据
                let changePrice14 = [];
                docs.filter(data => {
                    return (data["volume"] !== 0);
                }).forEach(data => {
                    /*
                     * 计算当日KDJ
                     */
                    // 找出9日内的最高和最低价，不足九日的周期，则有几天算几天
                    periodHigh.push(data["high"]);
                    periodLow.push(data["low"]);
                    if (periodHigh.length > 9 && periodLow.length > 9) {
                        periodHigh.shift();
                        periodLow.shift();
                    }
                    let high = Math.max.apply(null, periodHigh);
                    let low = Math.min.apply(null, periodLow);
                    // 以9日为周期计算RSV
                    let rsv = 0;
                    if (high !== low)
                        rsv = (data["close"] - low) / (high - low) * 100;
                    // K、D的平滑因子分别取1/3和2/3
                    let k = 2 / 3 * beforeK + 1 / 3 * rsv;
                    let d = 2 / 3 * beforeD + 1 / 3 * k;
                    let j = 3 * d - 2 * k;
                    beforeK = k;
                    beforeD = d;

                    /*
                     * 计算当日MACD
                     */
                    // 由于每日行情震荡波动之大小不同，并不适合以每日之收盘价来计算移动平均值，
                    // 于是有需求指数（Demand Index）之产生，用需求指数代表每日的收盘指数
                    let DI = (data["close"] * 2 + data["high"] + data["low"]) / 4;
                    let EMA12 = 2 / 13 * DI + 11 / 13 * beforeEMA12;
                    let EMA26 = 2 / 27 * DI + 25 / 27 * beforeEMA26;
                    let DIF = EMA12 - EMA26;
                    let DEA = DIF * 0.2 + beforeDEA * 0.8;
                    let MACD = 2 * (DIF - DEA);
                    beforeEMA12 = EMA12;
                    beforeEMA26 = EMA26;
                    beforeDEA = DEA;

                    /*
                     * 计算当日RSI
                     */
                    changePrice14.push(data["changePrice"]);
                    if (changePrice14.length > 14)
                        changePrice14.shift();
                    let upAverage14 = 0;
                    changePrice14.filter(price => {
                        return price >= 0;
                    }).forEach(price => upAverage14 += price);
                    upAverage14 /= 14;
                    let downAverage14 = 0;
                    changePrice14.filter(price => {
                        return price <= 0;
                    }).forEach(price => downAverage14 -= price);
                    downAverage14 /= 6;
                    let RSI14 = 0;
                    if ((upAverage14 + downAverage14) !== 0)
                        RSI14 = upAverage14 / (upAverage14 + downAverage14) * 100;
                    dailyData.set(data["date"].toISOString().substr(0, 10), {
                        "MACD_DIF" : DIF,
                        "MACD_DEA" : DEA,
                        "MACD" : MACD,
                        "RSV" : rsv,
                        "KDJ_K" : k,
                        "KDJ_D" : d,
                        "KDJ_J" : j,
                        "RSI" : RSI14
                    });
                });
                callback(null, dailyData);
            }
        });
    }
    else
        callback(null, dailyData);
}