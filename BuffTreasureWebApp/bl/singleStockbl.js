/**
 * Created by slow_time on 2017/5/6.
 */

let async = require("async/index.js");
let singleStockDB = require('../models/singleStock.js').singleStockDB;

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
 *      K   D   J,      DIF        DEA        MACD       adj
 * eg:  80  90  70      0.2        0.3        -0.2       11.0
 * @param code 股票代号
 * @param callback 形如 (err, docs) => { }
 */
exports.getDailyData = (code, callback) => {
    singleStockDB.getStockInfoByCode(code, (err, docs) => {
        if (err) {
            callback(err, null);
        }
        else {
            // 计算KDJ所需要的临时数据
            let beforeK = 50; // 前日K值
            let beforeD = 50; // 前日D值
            let periodHigh = [];
            let periodLow = [];
            // 计算MACD所需要的临时数据
            let beforeEMA12 = 0; // 昨日的12日指数平均值
            let beforeEMA26 = 0; // 昨日的26日指数平均值
            let beforeDEA = 0; // 昨日的九日DIF平滑移动平均值
            let all_day_data = docs.filter(data => {
                return (data["volume"] !== 0);
            }).map(data => {
                let one_day_data = [];
                one_day_data.push(data["date"].toISOString().substr(0, 10));
                one_day_data.push(parseFloat(data["open"].toFixed(2)));
                one_day_data.push(parseFloat(data["close"].toFixed(2)));
                one_day_data.push(parseFloat(data["low"].toFixed(2)));
                one_day_data.push(parseFloat(data["high"].toFixed(2)));
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
                let j = 3 * d - 2 * k;
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
                one_day_data.push(parseFloat(data["adjClose"].toFixed(2)));
                return one_day_data;
            });
            callback(null, all_day_data);
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
            let week_docs = [];
            for (let i = docs.length - 1; i >= 0; i--) {
                let week_date = docs[i]["date"];
                let week_open = docs[i]["open"];
                let week_close = docs[i]["close"];
                let week_high = [];
                let week_low = [];
                let week_volume = [];
                let week_adjClose = docs[i]["adjClose"];
                let week_turnOverRate = [];
                let week_changeRate = 0;
                // 判断有没有到周一
                while (i >= 0 && docs[i]["date"].getDay() !== 1) {
                    week_high.push(docs[i]["high"]);
                    week_low.push(docs[i]["low"]);
                    week_volume.push(docs[i]["volume"]);
                    week_turnOverRate.push(docs[i]["turnOverRate"]);
                    i--;
                }
                // 此时i指向的是周一，仍然是这一周的数据，需要加进去
                if (i >= 0) {
                    week_high.push(docs[i]["high"]);
                    week_low.push(docs[i]["low"]);
                    week_volume.push(docs[i]["volume"]);
                    week_turnOverRate.push(docs[i]["turnOverRate"]);
                }
                if (i < 0)
                    i++;
                week_changeRate = (week_adjClose - docs[i]["beforeClose"]) / docs[i]["beforeClose"];
                let week = {
                    "date": week_date,
                    "open": week_open,
                    "high": Math.max.apply(null, week_high),
                    "low": Math.min.apply(null, week_low),
                    "close": week_close,
                    "volume": week_volume.reduce((x, y) => { return x + y; }),
                    "adjClose": week_adjClose,
                    "changeRate": week_changeRate,
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
            let all_week_data = week_docs.filter(data => {
                return (data["volume"] !== 0);
            }).map(data => {
                let one_week_data = [];
                one_week_data.push(data["date"].toISOString().substr(0, 10));
                one_week_data.push(parseFloat(data["open"].toFixed(2)));
                one_week_data.push(parseFloat(data["close"].toFixed(2)));
                one_week_data.push(parseFloat(data["low"].toFixed(2)));
                one_week_data.push(parseFloat(data["high"].toFixed(2)));
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
                let j = 3 * d - 2 * k;
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
                one_week_data.push(parseFloat(data["adjClose"].toFixed(2)));
                return one_week_data;
            });
            callback(null, all_week_data);
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
            let month_docs = [];
            for (let i = docs.length - 1; i >= 0; i--) {
                let month_date = docs[i]["date"];
                let month_open = docs[i]["open"];
                let month_close = docs[i]["close"];
                let month_high = [];
                let month_low = [];
                let month_volume = [];
                let month_adjClose = docs[i]["adjClose"];
                let month_turnOverRate = [];
                let month_changeRate = 0;

                let now_month = docs[i]["date"].getMonth();
                // 判断月份有没有改变
                while (i >= 0 && docs[i]["date"].getMonth() === now_month) {
                    month_high.push(docs[i]["high"]);
                    month_low.push(docs[i]["low"]);
                    month_volume.push(docs[i]["volume"]);
                    month_turnOverRate.push(docs[i]["turnOverRate"]);
                    i--;
                }
                // 由于在外层for循环也要进行一次i--，所以在此加1，防止数据丢失
                i++;
                month_changeRate = (month_adjClose - docs[i]["beforeClose"]) / docs[i]["beforeClose"];
                let month = {
                    "date": month_date,
                    "open": month_open,
                    "high": Math.max.apply(null, month_high),
                    "low": Math.min.apply(null, month_low),
                    "close": month_close,
                    "volume": month_volume.reduce((x, y) => { return x + y; }),
                    "adjClose": month_adjClose,
                    "changeRate": month_changeRate,
                    "turnOverRate": month_turnOverRate.reduce((x, y) => { return x + y; })
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
            let all_month_data = month_docs.filter(data => {
                return (data["volume"] !== 0);
            }).map(data => {
                let one_month_data = [];
                one_month_data.push(data["date"].toISOString().substr(0, 10));
                one_month_data.push(parseFloat(data["open"].toFixed(2)));
                one_month_data.push(parseFloat(data["close"].toFixed(2)));
                one_month_data.push(parseFloat(data["low"].toFixed(2)));
                one_month_data.push(parseFloat(data["high"].toFixed(2)));
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
                let j = 3 * d - 2 * k;
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
                one_month_data.push(parseFloat(data["adjClose"].toFixed(2)));
                return one_month_data;
            });
            callback(null, all_month_data);
        }
    });
};