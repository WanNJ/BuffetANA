/**
 * Created by slow_time on 2017/5/6.
 */
const singleStockDB = require('../models/singleStock.js').singleStockDB;
const statisticTool = require('./tool/statisticTool');
const RTTool = require('./realtime/singleStockRT');
const exec = require('child_process').exec;
const allStockDB = require('../models/allstock').allStockDB;
const hotDB = require('../models/hotStockAndBoard').hotDB;


/**
 * 返回日K数据列表，其中每一个元素也是一个数组，形式如下
 *
 * ！！！！！！剔除了交易量为0的日期！！！！！！！！
 *
 *      日期，           开盘价，    收盘价，    最低价，    最高价，   前复权开盘价，    前复权收盘价，    前复权最低价，    前复权最高价      后复权开盘价，    后复权收盘价，    后复权最低价，    后复权最高价
 * eg:  '2017-05-05'    10.2       11.50      10.10      11.50     1.275           11.50           10.10           11.50           1.275           11.50           10.10           11.50
 *      涨跌幅(已乘100)，    成交量，    换手率(已乘100),    K   D   J,      DIF        DEA        MACD     DIF_before_adj        DEA_before_adj        MACD_before_adj   DIF_after_adj        DEA_after_adj        MACD_after_adj
 * eg:  1.275              1232       2.23               80  90  70      0.2        0.3        -0.2     0.2        0.3        -0.2                  22.42             0.2                  0.3                 -0.2
 *      RSI6     RSI12     RSI24      BIAS6    BIAS12    BIAS24    Boll   upper   lower     Boll_before_adj   upper_before_adj   lower_before_adj     Boll_after_adj   upper_after_adj   lower_after_adj
 * eg:  22.42   33.33      44.44      23       24        25        26     27      28        29                30                  31                   32               33                34
 *      WR1      WR2
 * eg:  34       35
 * @param code 股票代号
 * @param callback 形如 (err, docs) => { }
 */
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

            let beforeEMA12_before_adj = 0; // 昨日的12日指数平均值
            let beforeEMA26_before_adj = 0; // 昨日的26日指数平均值
            let beforeDEA_before_adj = 0; // 昨日的九日DIF平滑移动平均值

            let beforeEMA12_after_adj = 0; // 昨日的12日指数平均值
            let beforeEMA26_after_adj = 0; // 昨日的26日指数平均值
            let beforeDEA_after_adj = 0; // 昨日的九日DIF平滑移动平均值

            // 计算RSI所需要的临时数据
            let changePrice6 = [];
            let changePrice12 = [];
            let changePrice24 = [];

            // 计算乖离率BIAS所需要的临时数据
            let MA6 = [];       // 6日移动平均线
            let MA12 = [];      // 12日移动平均线
            let MA24 = [];      // 24日移动平均线

            // 计算布林线Boll所需要的临时数据
            let MA20 = [];
            let MA20_before_adj = [];
            let MA20_after_adj = [];

            // 计算威廉指标WR所需要的临时数据
            let high_period_10 = [];
            let low_period_10 = [];
            let high_period_6 = [];
            let low_period_6 = [];

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
                // 不复权
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

                // 前复权
                let high_before = data["high"] * one_day_beforeAdjClose / data["close"];
                let low_before = data["low"] * one_day_beforeAdjClose / data["close"]
                DI = (one_day_beforeAdjClose * 2 + high_before + low_before) / 4;
                EMA12 = 2 / 13 * DI + 11 / 13 * beforeEMA12_before_adj;
                EMA26 = 2 / 27 * DI + 25 / 27 * beforeEMA26_before_adj;
                DIF = EMA12 - EMA26;
                DEA = DIF * 0.2 + beforeDEA_before_adj * 0.8;
                MACD = 2 * (DIF - DEA);
                beforeEMA12_before_adj = EMA12;
                beforeEMA26_before_adj = EMA26;
                beforeDEA_before_adj = DEA;
                one_day_data.push(DIF);
                one_day_data.push(DEA);
                one_day_data.push(MACD);

                // 后复权
                let high_after = data["high"] * data["afterAdjClose"] / data["close"];
                let low_after = data["low"] * data["afterAdjClose"] / data["close"];
                DI = (data["afterAdjClose"] * 2 + high_after + low_after) / 4;
                EMA12 = 2 / 13 * DI + 11 / 13 * beforeEMA12_after_adj;
                EMA26 = 2 / 27 * DI + 25 / 27 * beforeEMA26_after_adj;
                DIF = EMA12 - EMA26;
                DEA = DIF * 0.2 + beforeDEA_after_adj * 0.8;
                MACD = 2 * (DIF - DEA);
                beforeEMA12_after_adj = EMA12;
                beforeEMA26_after_adj = EMA26;
                beforeDEA_after_adj = DEA;
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

                /*
                 * 计算当日乖离率BIAS，固定为6、12、24日的乖离率
                 */
                MA6.push(data["close"]);
                MA12.push(data["close"]);
                MA24.push(data["close"]);
                if (MA6.length > 6)
                    MA6.shift();
                if (MA12.length > 12)
                    MA12.shift();
                if (MA24.length > 24)
                    MA24.shift();
                let Ave6 = statisticTool.getAverage(MA6);
                let Ave12 = statisticTool.getAverage(MA12);
                let Ave24 = statisticTool.getAverage(MA24);
                let BIAS6 = (data["close"] - Ave6) / Ave6;
                let BIAS12 = (data["close"] - Ave12) / Ave12;
                let BIAS24 = (data["close"] - Ave24) / Ave24;
                one_day_data.push(BIAS6);
                one_day_data.push(BIAS12);
                one_day_data.push(BIAS24);

                /*
                 * 计算当日布林线Boll
                 */
                // 不复权
                MA20.push(data["close"]);
                if (MA20.length > 20)
                    MA20.shift();
                let MD = statisticTool.getSTD(MA20);
                let Boll = 0;
                if (MA20.length > 1)
                    Boll = statisticTool.getAverage(MA20.slice(0, -1));
                else
                    Boll = statisticTool.getAverage(MA20);
                let upper = Boll + 2 * MD;
                let lower = Boll - 2 * MD;
                one_day_data.push(Boll);
                one_day_data.push(upper);
                one_day_data.push(lower);

                // 前复权
                MA20_before_adj.push(one_day_beforeAdjClose);
                if (MA20_before_adj.length > 20)
                    MA20_before_adj.shift();
                MD = statisticTool.getSTD(MA20_before_adj);
                Boll = 0;
                if (MA20_before_adj.length > 1)
                    Boll = statisticTool.getAverage(MA20_before_adj.slice(0, -1));
                else
                    Boll = statisticTool.getAverage(MA20_before_adj);
                upper = Boll + 2 * MD;
                lower = Boll - 2 * MD;
                one_day_data.push(Boll);
                one_day_data.push(upper);
                one_day_data.push(lower);

                // 后复权
                MA20_after_adj.push(data["afterAdjClose"]);
                if (MA20_after_adj.length > 20)
                    MA20_after_adj.shift();
                MD = statisticTool.getSTD(MA20_after_adj);
                Boll = 0;
                if (MA20_after_adj.length > 1)
                    Boll = statisticTool.getAverage(MA20_after_adj.slice(0, -1));
                else
                    Boll = statisticTool.getAverage(MA20_after_adj);
                upper = Boll + 2 * MD;
                lower = Boll - 2 * MD;
                one_day_data.push(Boll);
                one_day_data.push(upper);
                one_day_data.push(lower);

                /*
                 * 计算当日威廉指标WR，WR1固定为10天买卖强弱指标，WR2固定为6天买卖强弱指标
                 */
                high_period_10.push(data["high"]);
                low_period_10.push(data["low"]);
                high_period_6.push(data["high"]);
                low_period_6.push(data["low"]);
                if (high_period_10.length > 10)
                    high_period_10.shift();
                if (low_period_10.length > 10)
                    low_period_10.shift();
                if (high_period_6.length > 6)
                    high_period_6.shift();
                if (low_period_6.length > 6)
                    low_period_6.shift();
                let high_10 = Math.max.apply(null, high_period_10);
                let low_10 = Math.min.apply(null, low_period_10);
                let high_6 = Math.max.apply(null, high_period_6);
                let low_6 = Math.min.apply(null, low_period_6);
                let WR1 = 0;
                let WR2 = 0;
                if (high_10 - low_10 !== 0)
                    WR1 = 100 * (high_10 - data["close"]) / (high_10 - low_10);
                if (high_6 - low_6 !== 0)
                    WR2 = 100 * (high_6 - data["close"]) / (high_6 - low_6);
                one_day_data.push(WR1);
                one_day_data.push(WR2);

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

            let beforeEMA12_before_adj = 0; // 昨日的12日指数平均值
            let beforeEMA26_before_adj = 0; // 昨日的26日指数平均值
            let beforeDEA_before_adj = 0; // 昨日的九日DIF平滑移动平均值

            let beforeEMA12_after_adj = 0; // 昨日的12日指数平均值
            let beforeEMA26_after_adj = 0; // 昨日的26日指数平均值
            let beforeDEA_after_adj = 0; // 昨日的九日DIF平滑移动平均值

            // 计算RSI所需要的临时数据
            let changePrice6 = [];
            let changePrice12 = [];
            let changePrice24 = [];

            // 计算乖离率BIAS所需要的临时数据
            let MA6 = [];       // 6日移动平均线
            let MA12 = [];      // 12日移动平均线
            let MA24 = [];      // 24日移动平均线

            // 计算布林线Boll所需要的临时数据
            let MA20 = [];
            let MA20_before_adj = [];
            let MA20_after_adj = [];

            // 计算威廉指标WR所需要的临时数据
            let high_period_10 = [];
            let low_period_10 = [];
            let high_period_6 = [];
            let low_period_6 = [];

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

                // 前复权
                DI = (data["beforeAdjClose"] * 2 + data["high_before"] + data["low_before"]) / 4;
                EMA12 = 2 / 13 * DI + 11 / 13 * beforeEMA12_before_adj;
                EMA26 = 2 / 27 * DI + 25 / 27 * beforeEMA26_before_adj;
                DIF = EMA12 - EMA26;
                DEA = DIF * 0.2 + beforeDEA_before_adj * 0.8;
                MACD = 2 * (DIF - DEA);
                beforeEMA12_before_adj = EMA12;
                beforeEMA26_before_adj = EMA26;
                beforeDEA_before_adj = DEA;
                one_week_data.push(DIF);
                one_week_data.push(DEA);
                one_week_data.push(MACD);

                // 后复权
                DI = (data["afterAdjClose"] * 2 + data["high_after"] + data["low_after"]) / 4;
                EMA12 = 2 / 13 * DI + 11 / 13 * beforeEMA12_after_adj;
                EMA26 = 2 / 27 * DI + 25 / 27 * beforeEMA26_after_adj;
                DIF = EMA12 - EMA26;
                DEA = DIF * 0.2 + beforeDEA_after_adj * 0.8;
                MACD = 2 * (DIF - DEA);
                beforeEMA12_after_adj = EMA12;
                beforeEMA26_after_adj = EMA26;
                beforeDEA_after_adj = DEA;
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

                /*
                 * 计算当周乖离率，固定为6、12、24周的乖离率
                 */
                MA6.push(data["close"]);
                MA12.push(data["close"]);
                MA24.push(data["close"]);
                if (MA6.length > 6)
                    MA6.shift();
                if (MA12.length > 12)
                    MA12.shift();
                if (MA24.length > 24)
                    MA24.shift();
                let Ave6 = statisticTool.getAverage(MA6);
                let Ave12 = statisticTool.getAverage(MA12);
                let Ave24 = statisticTool.getAverage(MA24);
                let BIAS6 = (data["close"] - Ave6) / Ave6;
                let BIAS12 = (data["close"] - Ave12) / Ave12;
                let BIAS24 = (data["close"] - Ave24) / Ave24;
                one_week_data.push(BIAS6);
                one_week_data.push(BIAS12);
                one_week_data.push(BIAS24);

                /*
                 * 计算当日布林线Boll
                 */
                // 不复权
                MA20.push(data["close"]);
                if (MA20.length > 20)
                    MA20.shift();
                let MD = statisticTool.getSTD(MA20);
                let Boll = 0;
                if (MA20.length > 1)
                    Boll = statisticTool.getAverage(MA20.slice(0, -1));
                else
                    Boll = statisticTool.getAverage(MA20);
                let upper = Boll + 2 * MD;
                let lower = Boll - 2 * MD;
                one_week_data.push(Boll);
                one_week_data.push(upper);
                one_week_data.push(lower);

                // 前复权
                MA20_before_adj.push(data["beforeAdjClose"]);
                if (MA20_before_adj.length > 20)
                    MA20_before_adj.shift();
                MD = statisticTool.getSTD(MA20_before_adj);
                Boll = 0;
                if (MA20_before_adj.length > 1)
                    Boll = statisticTool.getAverage(MA20_before_adj.slice(0, -1));
                else
                    Boll = statisticTool.getAverage(MA20_before_adj);
                upper = Boll + 2 * MD;
                lower = Boll - 2 * MD;
                one_week_data.push(Boll);
                one_week_data.push(upper);
                one_week_data.push(lower);

                // 后复权
                MA20_after_adj.push(data["afterAdjClose"]);
                if (MA20_after_adj.length > 20)
                    MA20_after_adj.shift();
                MD = statisticTool.getSTD(MA20_after_adj);
                Boll = 0;
                if (MA20_after_adj.length > 1)
                    Boll = statisticTool.getAverage(MA20_after_adj.slice(0, -1));
                else
                    Boll = statisticTool.getAverage(MA20_after_adj);
                upper = Boll + 2 * MD;
                lower = Boll - 2 * MD;
                one_week_data.push(Boll);
                one_week_data.push(upper);
                one_week_data.push(lower);

                /*
                 * 计算当日威廉指标WR，WR1固定为10天买卖强弱指标，WR2固定为6天买卖强弱指标
                 */
                high_period_10.push(data["high"]);
                low_period_10.push(data["low"]);
                high_period_6.push(data["high"]);
                low_period_6.push(data["low"]);
                if (high_period_10.length > 10)
                    high_period_10.shift();
                if (low_period_10.length > 10)
                    low_period_10.shift();
                if (high_period_6.length > 6)
                    high_period_6.shift();
                if (low_period_6.length > 6)
                    low_period_6.shift();
                let high_10 = Math.max.apply(null, high_period_10);
                let low_10 = Math.min.apply(null, low_period_10);
                let high_6 = Math.max.apply(null, high_period_6);
                let low_6 = Math.min.apply(null, low_period_6);
                let WR1 = 0;
                let WR2 = 0;
                if (high_10 - low_10 !== 0)
                    WR1 = 100 * (high_10 - data["close"]) / (high_10 - low_10);
                if (high_6 - low_6 !== 0)
                    WR2 = 100 * (high_6 - data["close"]) / (high_6 - low_6);
                one_week_data.push(WR1);
                one_week_data.push(WR2);

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
                month_changeRate = (month_close - docs[i]["beforeClose"]) / docs[i]["beforeClose"];
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

            let beforeEMA12_before_adj = 0; // 昨日的12日指数平均值
            let beforeEMA26_before_adj = 0; // 昨日的26日指数平均值
            let beforeDEA_before_adj = 0; // 昨日的九日DIF平滑移动平均值

            let beforeEMA12_after_adj = 0; // 昨日的12日指数平均值
            let beforeEMA26_after_adj = 0; // 昨日的26日指数平均值
            let beforeDEA_after_adj = 0; // 昨日的九日DIF平滑移动平均值

            // 计算RSI所需要的临时数据
            let changePrice6 = [];
            let changePrice12 = [];
            let changePrice24 = [];

            // 计算乖离率BIAS所需要的临时数据
            let MA6 = [];       // 6日移动平均线
            let MA12 = [];      // 12日移动平均线
            let MA24 = [];      // 24日移动平均线

            // 计算布林线Boll所需要的临时数据
            let MA20 = [];
            let MA20_before_adj = [];
            let MA20_after_adj = [];

            // 计算威廉指标WR所需要的临时数据
            let high_period_10 = [];
            let low_period_10 = [];
            let high_period_6 = [];
            let low_period_6 = [];

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


                // 前复权
                DI = (data["beforeAdjClose"] * 2 + data["high_before"] + data["low_before"]) / 4;
                EMA12 = 2 / 13 * DI + 11 / 13 * beforeEMA12_before_adj;
                EMA26 = 2 / 27 * DI + 25 / 27 * beforeEMA26_before_adj;
                DIF = EMA12 - EMA26;
                DEA = DIF * 0.2 + beforeDEA_before_adj * 0.8;
                MACD = 2 * (DIF - DEA);
                beforeEMA12_before_adj = EMA12;
                beforeEMA26_before_adj = EMA26;
                beforeDEA_before_adj = DEA;
                one_month_data.push(DIF);
                one_month_data.push(DEA);
                one_month_data.push(MACD);

                // 后复权
                DI = (data["afterAdjClose"] * 2 + data["high_after"] + data["low_after"]) / 4;
                EMA12 = 2 / 13 * DI + 11 / 13 * beforeEMA12_after_adj;
                EMA26 = 2 / 27 * DI + 25 / 27 * beforeEMA26_after_adj;
                DIF = EMA12 - EMA26;
                DEA = DIF * 0.2 + beforeDEA_after_adj * 0.8;
                MACD = 2 * (DIF - DEA);
                beforeEMA12_after_adj = EMA12;
                beforeEMA26_after_adj = EMA26;
                beforeDEA_after_adj = DEA;
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

                /*
                 * 计算当月乖离率，固定为6、12、24月的乖离率
                 */
                MA6.push(data["close"]);
                MA12.push(data["close"]);
                MA24.push(data["close"]);
                if (MA6.length > 6)
                    MA6.shift();
                if (MA12.length > 12)
                    MA12.shift();
                if (MA24.length > 24)
                    MA24.shift();
                let Ave6 = statisticTool.getAverage(MA6);
                let Ave12 = statisticTool.getAverage(MA12);
                let Ave24 = statisticTool.getAverage(MA24);
                let BIAS6 = (data["close"] - Ave6) / Ave6;
                let BIAS12 = (data["close"] - Ave12) / Ave12;
                let BIAS24 = (data["close"] - Ave24) / Ave24;
                one_month_data.push(BIAS6);
                one_month_data.push(BIAS12);
                one_month_data.push(BIAS24);

                /*
                 * 计算当日布林线Boll
                 */
                // 不复权
                MA20.push(data["close"]);
                if (MA20.length > 20)
                    MA20.shift();
                let MD = statisticTool.getSTD(MA20);
                let Boll = 0;
                if (MA20.length > 1)
                    Boll = statisticTool.getAverage(MA20.slice(0, -1));
                else
                    Boll = statisticTool.getAverage(MA20);
                let upper = Boll + 2 * MD;
                let lower = Boll - 2 * MD;
                one_month_data.push(Boll);
                one_month_data.push(upper);
                one_month_data.push(lower);

                // 前复权
                MA20_before_adj.push(data["beforeAdjClose"]);
                if (MA20_before_adj.length > 20)
                    MA20_before_adj.shift();
                MD = statisticTool.getSTD(MA20_before_adj);
                Boll = 0;
                if (MA20_before_adj.length > 1)
                    Boll = statisticTool.getAverage(MA20_before_adj.slice(0, -1));
                else
                    Boll = statisticTool.getAverage(MA20_before_adj);
                upper = Boll + 2 * MD;
                lower = Boll - 2 * MD;
                one_month_data.push(Boll);
                one_month_data.push(upper);
                one_month_data.push(lower);

                // 后复权
                MA20_after_adj.push(data["afterAdjClose"]);
                if (MA20_after_adj.length > 20)
                    MA20_after_adj.shift();
                MD = statisticTool.getSTD(MA20_after_adj);
                Boll = 0;
                if (MA20_after_adj.length > 1)
                    Boll = statisticTool.getAverage(MA20_after_adj.slice(0, -1));
                else
                    Boll = statisticTool.getAverage(MA20_after_adj);
                upper = Boll + 2 * MD;
                lower = Boll - 2 * MD;
                one_month_data.push(Boll);
                one_month_data.push(upper);
                one_month_data.push(lower);

                /*
                 * 计算当日威廉指标WR，WR1固定为10天买卖强弱指标，WR2固定为6天买卖强弱指标
                 */
                high_period_10.push(data["high"]);
                low_period_10.push(data["low"]);
                high_period_6.push(data["high"]);
                low_period_6.push(data["low"]);
                if (high_period_10.length > 10)
                    high_period_10.shift();
                if (low_period_10.length > 10)
                    low_period_10.shift();
                if (high_period_6.length > 6)
                    high_period_6.shift();
                if (low_period_6.length > 6)
                    low_period_6.shift();
                let high_10 = Math.max.apply(null, high_period_10);
                let low_10 = Math.min.apply(null, low_period_10);
                let high_6 = Math.max.apply(null, high_period_6);
                let low_6 = Math.min.apply(null, low_period_6);
                let WR1 = 0;
                let WR2 = 0;
                if (high_10 - low_10 !== 0)
                    WR1 = 100 * (high_10 - data["close"]) / (high_10 - low_10);
                if (high_6 - low_6 !== 0)
                    WR2 = 100 * (high_6 - data["close"]) / (high_6 - low_6);
                one_month_data.push(WR1);
                one_month_data.push(WR2);

                return one_month_data;
            });
            callback(null, splitData(all_month_data));
        }
    });
};


/**
 * 获得个股实时的信息
 * @param code
 * @param callback (err, stockRTInfo) => {}
 *
 * ！！！！！！！！！！！！！注意括号中的单位，没有提到单位的属性，就是不用加单位！！！！！！！！！！！！！！！
 *
 * 如果是大盘指数，则marketValue、floatMarketValue、turnOverRate、PB_ratio、PE_ratio没有值
 * 在界面中可以以'--'来代替，最好是不显示那些属性
 *
 * stockRTInfo形如
 * {
                    "now_price": 现价
                    "change_price": 涨跌额
                    "change_rate": 涨跌幅（已经乘了100，单位为"%"）
                    "yesterday_close": 昨收
                    "today_open": 今开
                    "high": 最高
                    "low": 最低
                    "volume": 成交量（单位为"万手"）
                    "volume_of_transaction": 成交额（单位为"万"）
                    "marketValue": 总市值（单位为"亿"）
                    "floatMarketValue": 流通市值（单位为"亿"）
                    "turnOverRate": 换手率（已经乘了100，单位为"%"）
                    "PB_ratio": 市净率
                    "amplitude": 振幅（已经乘了100，单位为"%"）
                    "PE_ratio": 市盈率
  }
 */
exports.getRTInfo = (code, callback) => {
    RTTool.obtainRTInfoByCode(code, (err, stockRTInfo) => {
        callback(err, stockRTInfo);
    });
};


/**
 * 获得最近浏览股票的实时信息
 * @param codes {Array} 最近浏览股票的代码数组
 * @param callback (err, results) =. {}
 * results是一个数组形如（对应顺序为传下来的codes数组的顺序）
 *   现价  涨跌幅
 * [[9.2, 0.11]...]
 */
exports.getLatestStockRTInfo = getLatestStockInfo;


/**
 * 获得该个股对应的公司简介
 * @param code
 * @param callback (err, companyInfo) => {}
 * companyInfo {JSON} 形如
 * {
    "公司名称": {String}
    "公司英文名称": {String}
    "上市市场": {String}
    "上市日期": {String}
    "发行价格": {Number}
    "主承销商": {String}
    "成立日期": {String}
    "注册资本": {String}
    "机构类型": {String}
    "组织形式": {String}
    "董事会秘书": {String}
    "公司电话": {String}
    "董秘电话": {String}
    "公司传真": {String}
    "董秘传真": {String}
    "公司电子邮箱": {String}
    "董秘电子邮箱": {String}
    "公司网址": {String}
    "邮政编码": {String}
    "信息披露网址": {String}
    "证券简称更名历史": {String}
    "注册地址": {String}
    "办公地址": {String}
    "公司简介": {String}
    "经营范围": {String}
 * }
 */
exports.getCompanyInfo = (code, callback) => {
    exec('python3' + ' /Users/slow_time/BuffettANA/BuffTreasureWebApp/bl/companyInfo.py ' +code, function(err, stdout, stderr){
        if(err) {
            callback(err, null);
        }
        if(stdout) {
            let result = stdout.replace(/'/g, '"');
            result = result.replace(/\\xa0/g, '');
            result = result.replace(/\\r\\n/g, '');
            result = result.replace(/\\"/g, "\"");
            let companyInfo = JSON.parse(result);
            callback(null, companyInfo);
        }
    });
};


/**
 * 获得热门股票的实时信息
 * @param callback (err, infos) => {}
 * infos形如
 *   股票名称    现价     涨跌幅（已乘100，单位为"%"）
 * [['平安银行', '9.04', '0.11']...]
 */
exports.getHotStocks = (callback) => {
    hotDB.getHotStocks((err, hotStocks) => {
        if (err)
            callback(err, null);
        else
            callback(null, hotStocks["hot"]);
    });
};


/**
 * 更新热门股票
 * @param callback
 */
exports.updateHotStocks = (callback) => {
    exec('python3' + ' /Users/Accident/BuffettANA/BuffTreasureWebApp/bl/hot_stock.py', function (err, stdout, stderr) {
        if (err) {
            callback(err, null);
        }
        if (stdout) {
            let hot_codes = stdout.split('|');
            hot_codes = hot_codes.map(t => t.substr(0, 6));
            let promises = hot_codes.map(code => new Promise((resolve, reject) => {
                allStockDB.getNameByCode(code, (err, name) => {
                    if (err)
                        reject(err);
                    else {
                        resolve(name["name"]);
                    }
                });
            }));
            Promise.all(promises).then(names => {
                getLatestStockInfo(hot_codes, (err, infos) => {
                    if (err)
                        callback(err, null);
                    else {
                        for (let i = 0; i < infos.length; i++) {
                            infos[i].unshift(names[i]);
                            infos[i].push(hot_codes[i]);
                        }
                        hotDB.updateHotStocks(infos.slice(0, 10), (err, isOK) => {
                            if (err)
                                callback(err, isOK);
                            else
                                callback(null, isOK);
                        });
                    }
                });
            }).catch((err) => {
                callback(err, null);
            });
        }
    });
};
/**
 * =======================================以下是私有方法==============================================
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
    let difs_before_adj = [];
    let deas_before_adj = [];
    let macds_before_adj = [];
    let difs__after_adj = [];
    let deas_after_adj = [];
    let macds_after_adj = [];
    let rsi6s = [];
    let rsi12s = [];
    let rsi24s = [];
    let BIAS6 = [];
    let BIAS12 = [];
    let BIAS24 = [];
    let Boll = [];
    let upper = [];
    let lower = [];
    let Boll_before_adj = [];
    let upper_before_adj = [];
    let lower_before_adj = [];
    let Boll_after_adj = [];
    let upper_after_adj = [];
    let lower_after_adj = [];
    let WR1 = [];
    let WR2 = [];
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
        difs_before_adj.push(daily_rawData[i].splice(0, 1)[0]);
        deas_before_adj.push(daily_rawData[i].splice(0, 1)[0]);
        macds_before_adj.push(daily_rawData[i].splice(0, 1)[0]);
        difs__after_adj.push(daily_rawData[i].splice(0, 1)[0]);
        deas_after_adj.push(daily_rawData[i].splice(0, 1)[0]);
        macds_after_adj.push(daily_rawData[i].splice(0, 1)[0]);
        rsi6s.push(daily_rawData[i].splice(0, 1)[0]);
        rsi12s.push(daily_rawData[i].splice(0, 1)[0]);
        rsi24s.push(daily_rawData[i].splice(0, 1)[0]);
        BIAS6.push(daily_rawData[i].splice(0, 1)[0]);
        BIAS12.push(daily_rawData[i].splice(0, 1)[0]);
        BIAS24.push(daily_rawData[i].splice(0, 1)[0]);
        Boll.push(daily_rawData[i].splice(0, 1)[0]);
        upper.push(daily_rawData[i].splice(0, 1)[0]);
        lower.push(daily_rawData[i].splice(0, 1)[0]);
        Boll_before_adj.push(daily_rawData[i].splice(0, 1)[0]);
        upper_before_adj.push(daily_rawData[i].splice(0, 1)[0]);
        lower_before_adj.push(daily_rawData[i].splice(0, 1)[0]);
        Boll_after_adj.push(daily_rawData[i].splice(0, 1)[0]);
        upper_after_adj.push(daily_rawData[i].splice(0, 1)[0]);
        lower_after_adj.push(daily_rawData[i].splice(0, 1)[0]);
        WR1.push(daily_rawData[i].splice(0, 1)[0]);
        WR2.push(daily_rawData[i].splice(0, 1)[0]);
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
        difs_before_adj: difs_before_adj,
        deas_before_adj: deas_before_adj,
        macds_before_adj: macds_before_adj,
        difs_after_adj: difs__after_adj,
        deas_after_adj: deas_after_adj,
        macds_after_adj: macds_after_adj,
        rsi6s: rsi6s,
        rsi12s: rsi12s,
        rsi24s: rsi24s,
        BIAS6: BIAS6,
        BIAS12: BIAS12,
        BIAS24: BIAS24,
        Boll: Boll,
        upper: upper,
        lower: lower,
        Boll_before_adj: Boll_before_adj,
        upper_before_adj: upper_before_adj,
        lower_before_adj: lower_before_adj,
        Boll_after_adj: Boll_after_adj,
        upper_after_adj: upper_after_adj,
        lower_after_adj: lower_after_adj,
        WR1: WR1,
        WR2: WR2
    };
}


/**
 * 获得最近浏览股票的实时信息
 * @param codes {Array} 最近浏览股票的代码数组
 * @param callback (err, results) =. {}
 * results是一个数组形如（对应顺序为传下来的codes数组的顺序）
 *   现价  涨跌幅
 * [[9.2, 0.11]...]
 */
function getLatestStockInfo(codes, callback) {
    let promises = codes.map(code => new Promise((resolve, reject) => {
        RTTool.obtainRTInfoByCode(code, (err, stockRTInfo) => {
            if (err)
                reject(err);
            else {
                let info = [];
                info.push(stockRTInfo["now_price"]);
                info.push(stockRTInfo["change_rate"]);
                resolve(info);
            }
        });
    }));
    Promise.all(promises).then(results => {
        callback(null, results);
    }).catch((err) => {
        callback(err, null);
    });
}

