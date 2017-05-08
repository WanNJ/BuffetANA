/**
 * Created by slow_time on 2017/5/6.
 */
let singleStockDB = require('../models/singleStock.js').singleStockDB;

/**
 * 返回日K数据列表，其中每一个元素也是一个数组，形式如下
 *
 * ！！！！！！剔除了交易量为0的日期！！！！！！！！
 *
 *      日期，           开盘价，    收盘价，    最低价，    最高价，    涨跌幅(已乘100)，    成交量，    换手率(已乘100)，    K   D   J
 * eg:  '2017-05-05'    10.2       11.50      10.10      11.50     1.275               1232       2.23               80  90  70
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
            let all_day_data = docs.filter(data => {
                return (data["volume"] !== 0);
            }).map(data => {
                let one_day_data = [];
                one_day_data.push(data["date"].toISOString().substr(0, 10));
                one_day_data.push(data["open"].toFixed(2));
                one_day_data.push(data["close"].toFixed(2));
                one_day_data.push(data["low"].toFixed(2));
                one_day_data.push(data["high"].toFixed(2));
                one_day_data.push(data["changeRate"].toFixed(2));
                one_day_data.push(data["volume"]);
                one_day_data.push(data["turnOverRate"].toFixed(2));

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
                return one_day_data;
            });
            callback(null, all_day_data);
        }
    });
};

/**
 * 返回周K数据列表，其中每一个元素也是一个数组，形式如下
 *      日期(Monday)，   开盘价，    收盘价，    最低价，    最高价，    涨跌幅(已乘100)，    成交量，    换手率(已乘100)，    K   D   J
 * eg:  '2017-05-08'    10.2       11.5       10.1       11.5      1.275               1232       2.23               80  90  70
 * @param code 股票代号
 * @param callback 形如 (err, docs) => { }
 */
exports.getWeeklyData = (code, callback) => {

};

/**
 * 返回月K数据列表，其中每一个元素也是一个数组，形式如下
 *      日期(1st)，    开盘价，    收盘价，    最低价，    最高价，    涨跌幅(已乘100)，    成交量，    换手率(已乘100)，    K   D   J
 * eg:  '2017-05-01'  10.2       11.5       10.1       11.5      1.275               1232       2.23               80  90  70
 * @param code 股票代号
 * @param callback 形如 (err, docs) => { }
 */
exports.getMonthlyData = (code, callback) => {

};