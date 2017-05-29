/**
 * Created by slow_time on 2017/5/6.
 */

let async = require("async/index.js");
let singleStockDB = require('../models/singleStock.js').singleStockDB;

/**
 * 为了测试async写的一个接口
 * 并没有什么用
 * @param date
 * @param callback
 */
exports.getTwoDayInfo = (date, callback) => {
    async.parallel([
        function (cb) {
            singleStockDB.getStockInfoByDate(new Date("2017-04-28"), cb);
        },
        function (cb) {
            singleStockDB.getStockInfoByDate(new Date("2017-04-27"), cb);
        }
        ],
        function (err, results) {
            callback(err, results);
        });
};

/**
 * 返回日K数据列表，其中每一个元素也是一个数组，形式如下
 *
 * ！！！！！！剔除了交易量为0的日期！！！！！！！！
 *
 *      日期，           开盘价，    收盘价，    最低价，    最高价，   涨跌幅(已乘100)，    成交量，    换手率(已乘100),
 * eg:  '2017-05-05'    10.2       11.50      10.10      11.50     1.275               1232       2.23
 *      K   D   J,      DIF        DEA        MACD       adj       RSI6     RSI12     RSI24
 * eg:  80  90  70      0.2        0.3        -0.2       11.0      22.42    33.33     44.44
 * @param code 股票代号
 * @param callback 形如 (err, docs) => { }
 */

function splitData(daily_rawData) {
    let categoryData = [];
    let values_no_adj = [];
    let values_before_adj = [];
    let values_after_adj = [];
    let changeRates = [];
    let volumns = [];
    let turnOverRates = [];
    let kIndexes = [];
    let dIndexes = [];
    let jIndexes = [];
    let difs = [];
    let deas = [];
    let macds = [];
    let rsi6s = [];
    let rsi12s = [];
    let rsi24s = [];
    for (let i = 0; i < daily_rawData.length; i++) {
        categoryData.push(daily_rawData[i].splice(0, 1)[0]);
        values_no_adj.push(daily_rawData[i].splice(0, 4));
        values_before_adj.push(daily_rawData[i].splice(0, 4));
        values_after_adj.push(daily_rawData[i].splice(0, 4));
        changeRates.push(daily_rawData[i].splice(0, 1)[0]);
        volumns.push(daily_rawData[i].splice(0, 1)[0]);
        turnOverRates.push(daily_rawData[i].splice(0, 1)[0]);
        kIndexes.push(daily_rawData[i].splice(0, 1)[0]);
        dIndexes.push(daily_rawData[i].splice(0, 1)[0]);
        jIndexes.push(daily_rawData[i].splice(0, 1)[0]);
        difs.push(daily_rawData[i].splice(0, 1)[0]);
        deas.push(daily_rawData[i].splice(0, 1)[0]);
        macds.push(daily_rawData[i].splice(0, 1)[0]);
        rsi6s.push(daily_rawData[i].splice(0, 1)[0]);
        rsi12s.push(daily_rawData[i].splice(0, 1)[0]);
        rsi24s.push(daily_rawData[i].splice(0, 1)[0]);
    }
    return {
        categoryData: categoryData,
        KLineValue_no_adj: values_no_adj,
        KLineValue_before_adj: values_before_adj,
        KLineValue_after_adj: values_after_adj,
        changeRates: changeRates,
        volumns: volumns,
        turnOverRates: turnOverRates,
        kIndexes: kIndexes,
        dIndexes: dIndexes,
        jIndexes: jIndexes,
        difs: difs,
        deas: deas,
        macds: macds,
        rsi6s: rsi6s,
        rsi12s: rsi12s,
        rsi24s: rsi24s
    };
}

exports.getDailyData = (code, callback) => {
    singleStockDB.getStockInfoByCode(code, (err, docs) => {
        if (err) {
            callback(err, null);
        }
        else {
            // 找到前复权的基准
            let before_index_ratio = 1;
            let before_index_price = 0;
            for (let i = docs.length - 1; i >= 0; i--) {
                if (docs[i]["volume"] !== 0) {
                    before_index_ratio = docs[i]["beforeAdjClose"];
                    before_index_price = docs[i]["close"];
                    break;
                }
            }
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
            let changePrice6 = [];
            let changePrice12 = [];
            let changePrice24 = [];
            let all_day_data = docs.filter(data => {
                return (data["volume"] !== 0);
            }).map(data => {
                let one_day_data = [];
                one_day_data.push(data["date"].toISOString().substr(0, 10));
                one_day_data.push(parseFloat(data["open"].toFixed(2)));
                one_day_data.push(parseFloat(data["close"].toFixed(2)));
                one_day_data.push(parseFloat(data["low"].toFixed(2)));
                one_day_data.push(parseFloat(data["high"].toFixed(2)));
                let one_day_beforeAdjClose = data["beforeAdjClose"] / before_index_ratio * before_index_price;
                one_day_data.push(parseFloat((data["open"] * one_day_beforeAdjClose / data["close"]).toFixed(2)));
                one_day_data.push(parseFloat(one_day_beforeAdjClose.toFixed(2)));
                one_day_data.push(parseFloat((data["low"] * one_day_beforeAdjClose / data["close"]).toFixed(2)));
                one_day_data.push(parseFloat((data["high"] * one_day_beforeAdjClose / data["close"]).toFixed(2)));
                one_day_data.push(parseFloat((data["open"] * data["afterAdjClose"] / data["close"]).toFixed(2)));
                one_day_data.push(parseFloat(data["afterAdjClose"].toFixed(2)));
                one_day_data.push(parseFloat((data["low"] * data["afterAdjClose"] / data["close"]).toFixed(2)));
                one_day_data.push(parseFloat((data["high"] * data["afterAdjClose"] / data["close"]).toFixed(2)));
                one_day_data.push(parseFloat(data["changeRate"].toFixed(2)));
                one_day_data.push(data["volume"]);
                one_day_data.push(parseFloat(data["turnOverRate"].toFixed(2)));

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
                let j = 3 * k - 2 * d;
                beforeK = k;
                beforeD = d;
                one_day_data.push(k);
                one_day_data.push(d);
                one_day_data.push(j);

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
                one_day_data.push(DIF);
                one_day_data.push(DEA);
                one_day_data.push(MACD);

                /*
                 * 计算当日RSI
                 */
                changePrice6.push(data["changePrice"]);
                changePrice12.push(data["changePrice"]);
                changePrice24.push(data["changePrice"]);
                if (changePrice6.length > 6)
                    changePrice6.shift();
                if (changePrice12.length > 12)
                    changePrice12.shift();
                if (changePrice24.length > 24)
                    changePrice24.shift();
                let upAverage6 = 0;
                changePrice6.filter(price => {
                    return price >= 0;
                }).forEach(price => upAverage6 += price);
                upAverage6 /= 6;
                let downAverage6 = 0;
                changePrice6.filter(price => {
                        return price <= 0;
                    }).forEach(price => downAverage6 -= price);
                downAverage6 /= 6;
                let upAverage12 = 0;
                changePrice12.filter(price => {
                    return price >= 0;
                }).forEach(price => upAverage12 += price);
                upAverage12 /= 12;
                let downAverage12 = 0;
                changePrice12.filter(price => {
                    return price <= 0;
                }).forEach(price => downAverage12 -= price);
                downAverage12 /= 12;
                let upAverage24 = 0;
                changePrice24.filter(price => {
                    return price >= 0;
                }).forEach(price => upAverage24 += price);
                upAverage24 /= 24;
                let downAverage24 = 0;
                changePrice24.filter(price => {
                    return price <= 0;
                }).forEach(price => downAverage24 -= price);
                downAverage24 /= 24;
                let RSI6 = 0;
                let RSI12 = 0;
                let RSI24 = 0;
                if ((upAverage6 + downAverage6) !== 0)
                    RSI6 = upAverage6 / (upAverage6 + downAverage6) * 100;
                if ((upAverage12 + downAverage12) !== 0)
                    RSI12 = upAverage12 / (upAverage12 + downAverage12) * 100;
                if ((upAverage24 + downAverage24) !== 0)
                    RSI24 = upAverage24 / (upAverage24 + downAverage24) * 100;
                one_day_data.push(RSI6);
                one_day_data.push(RSI12);
                one_day_data.push(RSI24);
                return one_day_data;
            });
            callback(null, splitData(all_day_data));
        }
    });
};

/**
 * 返回周K数据列表，其中每一个元素也是一个数组，形式如下
 *
 * ！！！！！！剔除了交易量为0的日期！！！！！！！！
 *
 *      日期，           开盘价，    收盘价，    最低价，    最高价，   涨跌幅(已乘100)，    成交量，    换手率(已乘100),
 * eg:  '2017-05-05'    10.2       11.50      10.10      11.50     1.275               1232       2.23
 *      K   D   J,      DIF        DEA        MACD       adj
 * eg:  80  90  70      0.2        0.3        -0.2       11.0
 * @param code 股票代号
 * @param callback 形如 (err, docs) => { }
 */
exports.getWeeklyData = (code, callback) => {
    singleStockDB.getStockInfoByCode(code, (err, docs) => {
        if (err) {
            callback(err, null);
        }
        else {
            // 找到前复权的基准
            let before_index_ratio = 1;
            let before_index_price = 0;
            for (let i = docs.length - 1; i >= 0; i--) {
                if (docs[i]["volume"] !== 0) {
                    before_index_ratio = docs[i]["beforeAdjClose"];
                    before_index_price = docs[i]["close"];
                    break;
                }
            }

            let week_docs = [];
            for (let i = docs.length - 1; i >= 0; i--) {
                if (docs[i]["volume"] === 0)
                    continue;
                let week_date = docs[i]["date"];
                let week_open = docs[i]["open"];
                let week_close = docs[i]["close"];
                let week_high = [];
                let week_low = [];

                // 前复权计算
                let week_beforeAdjClose = docs[i]["beforeAdjClose"] / before_index_ratio * before_index_price;
                let week_open_before = docs[i]["open"] * week_beforeAdjClose / docs[i]["close"];
                let week_close_before = week_beforeAdjClose;
                let week_high_before = [];
                let week_low_before = [];

                // 后复权计算
                let week_open_after = docs[i]["open"] * docs[i]["afterAdjClose"] / docs[i]["close"];
                let week_close_after = docs[i]["afterAdjClose"];
                let week_high_after = [];
                let week_low_after = [];
                let week_volume = [];
                let week_turnOverRate = [];
                let week_changePrice = [];
                let week_changeRate = 0.0;
                // 判断有没有到周一
                while (i >= 0 && docs[i]["date"].getDay() !== 1) {
                    if (docs[i]["volume"] !== 0) {
                        week_open = docs[i]["open"];
                        week_beforeAdjClose = docs[i]["beforeAdjClose"] / before_index_ratio * before_index_price;
                        week_open_before = docs[i]["open"] * week_beforeAdjClose / docs[i]["close"];
                        week_open_after = docs[i]["open"] * docs[i]["afterAdjClose"] / docs[i]["close"];
                        week_high.push(docs[i]["high"]);
                        week_low.push(docs[i]["low"]);
                        week_high_before.push(docs[i]["high"] * week_beforeAdjClose / docs[i]["close"]);
                        week_low_before.push(docs[i]["low"] * week_beforeAdjClose / docs[i]["close"]);
                        week_high_after.push(docs[i]["high"] * docs[i]["afterAdjClose"] / docs[i]["close"]);
                        week_low_after.push(docs[i]["low"] * docs[i]["afterAdjClose"] / docs[i]["close"]);
                        week_volume.push(docs[i]["volume"]);
                        week_turnOverRate.push(docs[i]["turnOverRate"]);
                        week_changePrice.push(docs[i]["changePrice"]);
                    }
                    i--;
                }
                // 此时i指向的是周一，仍然是这一周的数据，需要加进去
                if (i >= 0 && docs[i]["volume"] !== 0) {
                    week_open = docs[i]["open"];
                    week_beforeAdjClose = docs[i]["beforeAdjClose"] / before_index_ratio * before_index_price;
                    week_open_before = docs[i]["open"] * week_beforeAdjClose / docs[i]["close"];
                    week_open_after = docs[i]["open"] * docs[i]["afterAdjClose"] / docs[i]["close"];
                    week_high.push(docs[i]["high"]);
                    week_low.push(docs[i]["low"]);
                    week_high_before.push(docs[i]["high"] * week_beforeAdjClose / docs[i]["close"]);
                    week_low_before.push(docs[i]["low"] * week_beforeAdjClose / docs[i]["close"]);
                    week_high_after.push(docs[i]["high"] * docs[i]["afterAdjClose"] / docs[i]["close"]);
                    week_low_after.push(docs[i]["low"] * docs[i]["afterAdjClose"] / docs[i]["close"]);
                    week_volume.push(docs[i]["volume"]);
                    week_turnOverRate.push(docs[i]["turnOverRate"]);
                    week_changePrice.push(docs[i]["changePrice"]);
                }
                if (i < 0)
                    i++;
                week_changeRate = (week_close - docs[i]["beforeClose"]) / docs[i]["beforeClose"];

                if (week_volume.length === 0)
                    continue;
                let week = {
                    "date": week_date,
                    "open": week_open,
                    "high": Math.max.apply(null, week_high),
                    "low": Math.min.apply(null, week_low),
                    "close": week_close,
                    "open_before": week_open_before,
                    "high_before": Math.max.apply(null, week_high_before),
                    "low_before": Math.min.apply(null, week_low_before),
                    "beforeAdjClose": week_close_before,
                    "open_after": week_open_after,
                    "high_after": Math.max.apply(null, week_high_after),
                    "low_after": Math.min.apply(null, week_low_after),
                    "afterAdjClose": week_close_after,
                    "volume": week_volume.reduce((x, y) => { return x + y; }),
                    "changeRate": week_changeRate,
                    "changePrice": week_changePrice.reduce((x, y) => { return x + y; }),
                    "turnOverRate": week_turnOverRate.reduce((x, y) => { return x + y; })
                };
                week_docs.unshift(week);
            }
            // 计算KDJ所需要的临时数据
            let beforeK = 50; // 上周K值
            let beforeD = 50; // 上周D值
            let periodHigh = [];
            let periodLow = [];
            // 计算MACD所需要的临时数据
            let beforeEMA12 = 0; // 上周的12周指数平均值
            let beforeEMA26 = 0; // 上周的26周指数平均值
            let beforeDEA = 0; // 上周的九周DIF平滑移动平均值

            // 计算RSI所需要的临时数据
            let changePrice6 = [];
            let changePrice12 = [];
            let changePrice24 = [];

            let all_week_data = week_docs.filter(data => {
                return (data["volume"] !== 0);
            }).map(data => {
                let one_week_data = [];
                one_week_data.push(data["date"].toISOString().substr(0, 10));
                one_week_data.push(parseFloat(data["open"].toFixed(2)));
                one_week_data.push(parseFloat(data["close"].toFixed(2)));
                one_week_data.push(parseFloat(data["low"].toFixed(2)));
                one_week_data.push(parseFloat(data["high"].toFixed(2)));
                one_week_data.push(parseFloat(data["open_before"].toFixed(2)));
                one_week_data.push(parseFloat(data["beforeAdjClose"].toFixed(2)));
                one_week_data.push(parseFloat(data["low_before"].toFixed(2)));
                one_week_data.push(parseFloat(data["high_before"].toFixed(2)));
                one_week_data.push(parseFloat(data["open_after"].toFixed(2)));
                one_week_data.push(parseFloat(data["afterAdjClose"].toFixed(2)));
                one_week_data.push(parseFloat(data["low_after"].toFixed(2)));
                one_week_data.push(parseFloat(data["high_after"].toFixed(2)));
                one_week_data.push(parseFloat(data["changeRate"].toFixed(2)));
                one_week_data.push(data["volume"]);
                one_week_data.push(parseFloat(data["turnOverRate"].toFixed(2)));

                /*
                 * 计算当周KDJ
                 */
                // 找出9周内的最高和最低价，不足九周的周期，则有几周算几周
                periodHigh.push(data["high"]);
                periodLow.push(data["low"]);
                if (periodHigh.length > 9 && periodLow.length > 9) {
                    periodHigh.shift();
                    periodLow.shift();
                }
                let high = Math.max.apply(null, periodHigh);
                let low = Math.min.apply(null, periodLow);
                // 以9周为周期计算RSV
                let rsv = 0;
                if (high !== low)
                    rsv = (data["close"] - low) / (high - low) * 100;
                // K、D的平滑因子分别取1/3和2/3
                let k = 2 / 3 * beforeK + 1 / 3 * rsv;
                let d = 2 / 3 * beforeD + 1 / 3 * k;
                let j = 3 * k - 2 * d;
                beforeK = k;
                beforeD = d;
                one_week_data.push(k);
                one_week_data.push(d);
                one_week_data.push(j);

                /*
                 * 计算当周MACD
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
                one_week_data.push(DIF);
                one_week_data.push(DEA);
                one_week_data.push(MACD);

                /*
                 * 计算当周RSI
                 */
                changePrice6.push(data["changePrice"]);
                changePrice12.push(data["changePrice"]);
                changePrice24.push(data["changePrice"]);
                if (changePrice6.length > 6)
                    changePrice6.shift();
                if (changePrice12.length > 12)
                    changePrice12.shift();
                if (changePrice24.length > 24)
                    changePrice24.shift();
                let upAverage6 = 0;
                changePrice6.filter(price => {
                    return price >= 0;
                }).forEach(price => upAverage6 += price);
                upAverage6 /= 6;
                let downAverage6 = 0;
                changePrice6.filter(price => {
                    return price <= 0;
                }).forEach(price => downAverage6 -= price);
                downAverage6 /= 6;
                let upAverage12 = 0;
                changePrice12.filter(price => {
                    return price >= 0;
                }).forEach(price => upAverage12 += price);
                upAverage12 /= 12;
                let downAverage12 = 0;
                changePrice12.filter(price => {
                    return price <= 0;
                }).forEach(price => downAverage12 -= price);
                downAverage12 /= 12;
                let upAverage24 = 0;
                changePrice24.filter(price => {
                    return price >= 0;
                }).forEach(price => upAverage24 += price);
                upAverage24 /= 24;
                let downAverage24 = 0;
                changePrice24.filter(price => {
                    return price <= 0;
                }).forEach(price => downAverage24 -= price);
                downAverage24 /= 24;
                let RSI6 = 0;
                let RSI12 = 0;
                let RSI24 = 0;
                if ((upAverage6 + downAverage6) !== 0)
                    RSI6 = upAverage6 / (upAverage6 + downAverage6) * 100;
                if ((upAverage12 + downAverage12) !== 0)
                    RSI12 = upAverage12 / (upAverage12 + downAverage12) * 100;
                if ((upAverage24 + downAverage24) !== 0)
                    RSI24 = upAverage24 / (upAverage24 + downAverage24) * 100;
                one_week_data.push(RSI6);
                one_week_data.push(RSI12);
                one_week_data.push(RSI24);
                return one_week_data;
            });
            callback(null, splitData(all_week_data));
        }
    });
};

/**
 * 返回月K数据列表，其中每一个元素也是一个数组，形式如下
 *
 * ！！！！！！剔除了交易量为0的日期！！！！！！！！
 *
 *      日期，           开盘价，    收盘价，    最低价，    最高价，   涨跌幅(已乘100)，    成交量，    换手率(已乘100),
 * eg:  '2017-05-05'    10.2       11.50      10.10      11.50     1.275               1232       2.23
 *      K   D   J,      DIF        DEA        MACD       adj
 * eg:  80  90  70      0.2        0.3        -0.2       11.0
 * @param code 股票代号
 * @param callback 形如 (err, docs) => { }
 */
exports.getMonthlyData = (code, callback) => {
    singleStockDB.getStockInfoByCode(code, (err, docs) => {
        if (err) {
            callback(err, null);
        }
        else {
            // 找到前复权的基准
            let before_index_ratio = 1;
            let before_index_price = 0;
            for (let i = docs.length - 1; i >= 0; i--) {
                if (docs[i]["volume"] !== 0) {
                    before_index_ratio = docs[i]["beforeAdjClose"];
                    before_index_price = docs[i]["close"];
                    break;
                }
            }

            let month_docs = [];
            for (let i = docs.length - 1; i >= 0; i--) {
                if (docs[i]["volume"] === 0)
                    continue;
                let month_date = docs[i]["date"];
                let month_open = docs[i]["open"];
                let month_close = docs[i]["close"];
                let month_high = [];
                let month_low = [];
                let month_beforeAdjClose = docs[i]["beforeAdjClose"] / before_index_ratio * before_index_price;
                let month_open_before = docs[i]["open"] * month_beforeAdjClose / docs[i]["close"];
                let month_close_before = month_beforeAdjClose;
                let month_high_before = [];
                let month_low_before = [];
                let month_open_after = docs[i]["open"] * docs[i]["afterAdjClose"] / docs[i]["close"];
                let month_close_after = docs[i]["afterAdjClose"];
                let month_high_after = [];
                let month_low_after = [];
                let month_volume = [];
                let month_adjClose = docs[i]["close"];
                let month_turnOverRate = [];
                let month_changePrice = [];
                let month_changeRate = 0;

                let now_month = docs[i]["date"].getMonth();
                // 判断月份有没有改变
                while (i >= 0 && docs[i]["date"].getMonth() === now_month) {
                    if (docs[i]["volume"] !== 0) {
                        month_open = docs[i]["open"];
                        month_beforeAdjClose = docs[i]["beforeAdjClose"] / before_index_ratio * before_index_price;
                        month_open_before = docs[i]["open"] * month_beforeAdjClose / docs[i]["close"];
                        month_open_after = docs[i]["open"] * docs[i]["afterAdjClose"] / docs[i]["close"];
                        month_high.push(docs[i]["high"]);
                        month_low.push(docs[i]["low"]);
                        month_high_before.push(docs[i]["high"] * month_beforeAdjClose / docs[i]["close"]);
                        month_low_before.push(docs[i]["low"] * month_beforeAdjClose / docs[i]["close"]);
                        month_high_after.push(docs[i]["high"] * docs[i]["afterAdjClose"] / docs[i]["close"]);
                        month_low_after.push(docs[i]["low"] * docs[i]["afterAdjClose"] / docs[i]["close"]);
                        month_volume.push(docs[i]["volume"]);
                        month_turnOverRate.push(docs[i]["turnOverRate"]);
                        month_changePrice.push(docs[i]["changePrice"]);
                    }
                    i--;
                }
                // 由于在外层for循环也要进行一次i--，所以在此加1，防止数据丢失
                i++;
                month_changeRate = (month_adjClose - docs[i]["beforeClose"]) / docs[i]["beforeClose"];
                if (month_volume.length === 0)
                    continue;
                let month = {
                    "date": month_date,
                    "open": month_open,
                    "high": Math.max.apply(null, month_high),
                    "low": Math.min.apply(null, month_low),
                    "close": month_close,
                    "open_before": month_open_before,
                    "high_before": Math.max.apply(null, month_high_before),
                    "low_before": Math.min.apply(null, month_low_before),
                    "beforeAdjClose": month_close_before,
                    "open_after": month_open_after,
                    "high_after": Math.max.apply(null, month_high_after),
                    "low_after": Math.min.apply(null, month_low_after),
                    "afterAdjClose": month_close_after,
                    "volume": month_volume.reduce((x, y) => { return x + y; }),
                    "adjClose": month_adjClose,
                    "changeRate": month_changeRate,
                    "turnOverRate": month_turnOverRate.reduce((x, y) => { return x + y; }),
                    "changePrice": month_changePrice.reduce((x, y) => { return x + y; })
                };
                month_docs.unshift(month);
            }
            // 计算KDJ所需要的临时数据
            let beforeK = 50; // 上月K值
            let beforeD = 50; // 上月D值
            let periodHigh = [];
            let periodLow = [];
            // 计算MACD所需要的临时数据
            let beforeEMA12 = 0; // 上月的12月指数平均值
            let beforeEMA26 = 0; // 上月的26月指数平均值
            let beforeDEA = 0; // 上月的九个月DIF平滑移动平均值

            // 计算RSI所需要的临时数据
            let changePrice6 = [];
            let changePrice12 = [];
            let changePrice24 = [];

            let all_month_data = month_docs.filter(data => {
                return (data["volume"] !== 0);
            }).map(data => {
                let one_month_data = [];
                one_month_data.push(data["date"].toISOString().substr(0, 10));
                one_month_data.push(parseFloat(data["open"].toFixed(2)));
                one_month_data.push(parseFloat(data["close"].toFixed(2)));
                one_month_data.push(parseFloat(data["low"].toFixed(2)));
                one_month_data.push(parseFloat(data["high"].toFixed(2)));
                one_month_data.push(parseFloat(data["open_before"].toFixed(2)));
                one_month_data.push(parseFloat(data["beforeAdjClose"].toFixed(2)));
                one_month_data.push(parseFloat(data["low_before"].toFixed(2)));
                one_month_data.push(parseFloat(data["high_before"].toFixed(2)));
                one_month_data.push(parseFloat(data["open_after"].toFixed(2)));
                one_month_data.push(parseFloat(data["afterAdjClose"].toFixed(2)));
                one_month_data.push(parseFloat(data["low_after"].toFixed(2)));
                one_month_data.push(parseFloat(data["high_after"].toFixed(2)));
                one_month_data.push(parseFloat(data["changeRate"].toFixed(2)));
                one_month_data.push(data["volume"]);
                one_month_data.push(parseFloat(data["turnOverRate"].toFixed(2)));

                /*
                 * 计算当周KDJ
                 */
                // 找出9个月内的最高和最低价，不足九个月的周期，则有几月算几月
                periodHigh.push(data["high"]);
                periodLow.push(data["low"]);
                if (periodHigh.length > 9 && periodLow.length > 9) {
                    periodHigh.shift();
                    periodLow.shift();
                }
                let high = Math.max.apply(null, periodHigh);
                let low = Math.min.apply(null, periodLow);
                // 以9个月为周期计算RSV
                let rsv = 0;
                if (high !== low)
                    rsv = (data["close"] - low) / (high - low) * 100;
                // K、D的平滑因子分别取1/3和2/3
                let k = 2 / 3 * beforeK + 1 / 3 * rsv;
                let d = 2 / 3 * beforeD + 1 / 3 * k;
                let j = 3 * k - 2 * d;
                beforeK = k;
                beforeD = d;
                one_month_data.push(k);
                one_month_data.push(d);
                one_month_data.push(j);

                /*
                 * 计算当月MACD
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
                one_month_data.push(DIF);
                one_month_data.push(DEA);
                one_month_data.push(MACD);

                /*
                 * 计算当月RSI
                 */
                changePrice6.push(data["changePrice"]);
                changePrice12.push(data["changePrice"]);
                changePrice24.push(data["changePrice"]);
                if (changePrice6.length > 6)
                    changePrice6.shift();
                if (changePrice12.length > 12)
                    changePrice12.shift();
                if (changePrice24.length > 24)
                    changePrice24.shift();
                let upAverage6 = 0;
                changePrice6.filter(price => {
                    return price >= 0;
                }).forEach(price => upAverage6 += price);
                upAverage6 /= 6;
                let downAverage6 = 0;
                changePrice6.filter(price => {
                    return price <= 0;
                }).forEach(price => downAverage6 -= price);
                downAverage6 /= 6;
                let upAverage12 = 0;
                changePrice12.filter(price => {
                    return price >= 0;
                }).forEach(price => upAverage12 += price);
                upAverage12 /= 12;
                let downAverage12 = 0;
                changePrice12.filter(price => {
                    return price <= 0;
                }).forEach(price => downAverage12 -= price);
                downAverage12 /= 12;
                let upAverage24 = 0;
                changePrice24.filter(price => {
                    return price >= 0;
                }).forEach(price => upAverage24 += price);
                upAverage24 /= 24;
                let downAverage24 = 0;
                changePrice24.filter(price => {
                    return price <= 0;
                }).forEach(price => downAverage24 -= price);
                downAverage24 /= 24;
                let RSI6 = 0;
                let RSI12 = 0;
                let RSI24 = 0;
                if ((upAverage6 + downAverage6) !== 0) {
                    RSI6 = upAverage6 / (upAverage6 + downAverage6) * 100;
                }
                if ((upAverage12 + downAverage12) !== 0)
                    RSI12 = upAverage12 / (upAverage12 + downAverage12) * 100;
                if ((upAverage24 + downAverage24) !== 0)
                    RSI24 = upAverage24 / (upAverage24 + downAverage24) * 100;
                one_month_data.push(RSI6);
                one_month_data.push(RSI12);
                one_month_data.push(RSI24);
                return one_month_data;
            });
            callback(null, splitData(all_month_data));
        }
    });
};