/**
 * Created by slow_time on 2017/5/6.
 */
let singleStockDB = require('../models/singleStock.js').singleStockDB;

/**
 * 返回日K数据列表，其中每一个元素也是一个数组，形式如下
 *
 * ！！！！！！剔除了交易量为0的日期！！！！！！！！
 *
 *      日期，           开盘价，    收盘价，    最低价，    最高价，    涨跌幅(已乘100)，    成交量，    换手率(已乘100),
 * eg:  '2017-05-05'    10.2       11.50      10.10      11.50     1.275               1232       2.23
 *      K   D   J,      DIF        DEA        MACD
 * eg:  80  90  70      0.2        0.3        -0.2
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
 *      日期，           开盘价，    收盘价，    最低价，    最高价，    涨跌幅(已乘100)，    成交量，    换手率(已乘100),
 * eg:  '2017-05-05'    10.2       11.50      10.10      11.50     1.275               1232       2.23
 *      K   D   J,      DIF        DEA        MACD
 * eg:  80  90  70      0.2        0.3        -0.2
 * @param code 股票代号
 * @param callback 形如 (err, docs) => { }
 */
exports.getWeeklyData = (code, callback) => {

};

/**
 * 返回月K数据列表，其中每一个元素也是一个数组，形式如下
 *
 * ！！！！！！剔除了交易量为0的日期！！！！！！！！
 *
 *      日期，           开盘价，    收盘价，    最低价，    最高价，    涨跌幅(已乘100)，    成交量，    换手率(已乘100),
 * eg:  '2017-05-05'    10.2       11.50      10.10      11.50     1.275               1232       2.23
 *      K   D   J,      DIF        DEA        MACD
 * eg:  80  90  70      0.2        0.3        -0.2
 * @param code 股票代号
 * @param callback 形如 (err, docs) => { }
 */
exports.getMonthlyData = (code, callback) => {

};