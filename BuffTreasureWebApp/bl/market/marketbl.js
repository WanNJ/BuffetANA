/**
 * Created by slow_time on 2017/6/7.
 */

const marketIndexDB = require('../../models/marketIndex').marketIndexDB;
const statisticTool = require('../tool/statisticTool');
const RTTool = require('../realtime/marketRT');

function splitIndex(marketIndex) {
    let categoryData = [];
    let k_values = [];
    let changeRates = [];
    let volumns = [];
    let kIndexes = [];
    let dIndexes = [];
    let jIndexes = [];
    let difs = [];
    let deas = [];
    let macds = [];
    let rsi6s = [];
    let rsi12s = [];
    let rsi24s = [];
    let BIAS6 = [];
    let BIAS12 = [];
    let BIAS24 = [];
    let Boll = [];
    let upper = [];
    let lower = [];
    let WR1 = [];
    let WR2 = [];
    for (let i = 0; i < marketIndex.length; i++) {
        categoryData.push(marketIndex[i].splice(0, 1)[0]);
        k_values.push(marketIndex[i].splice(0, 4));
        changeRates.push(marketIndex[i].splice(0, 1)[0]);
        volumns.push(marketIndex[i].splice(0, 1)[0]);
        kIndexes.push(marketIndex[i].splice(0, 1)[0]);
        dIndexes.push(marketIndex[i].splice(0, 1)[0]);
        jIndexes.push(marketIndex[i].splice(0, 1)[0]);
        difs.push(marketIndex[i].splice(0, 1)[0]);
        deas.push(marketIndex[i].splice(0, 1)[0]);
        macds.push(marketIndex[i].splice(0, 1)[0]);
        rsi6s.push(marketIndex[i].splice(0, 1)[0]);
        rsi12s.push(marketIndex[i].splice(0, 1)[0]);
        rsi24s.push(marketIndex[i].splice(0, 1)[0]);
        BIAS6.push(marketIndex[i].splice(0, 1)[0]);
        BIAS12.push(marketIndex[i].splice(0, 1)[0]);
        BIAS24.push(marketIndex[i].splice(0, 1)[0]);
        Boll.push(marketIndex[i].splice(0, 1)[0]);
        upper.push(marketIndex[i].splice(0, 1)[0]);
        lower.push(marketIndex[i].splice(0, 1)[0]);
        WR1.push(marketIndex[i].splice(0, 1)[0]);
        WR2.push(marketIndex[i].splice(0, 1)[0]);
    }
    return {
        categoryData: categoryData,
        KLineValue: k_values,
        changeRates: changeRates,
        volumns: volumns,
        kIndexes: kIndexes,
        dIndexes: dIndexes,
        jIndexes: jIndexes,
        difs: difs,
        deas: deas,
        macds: macds,
        rsi6s: rsi6s,
        rsi12s: rsi12s,
        rsi24s: rsi24s,
        BIAS6: BIAS6,
        BIAS12: BIAS12,
        BIAS24: BIAS24,
        Boll: Boll,
        upper: upper,
        lower: lower,
        WR1: WR1,
        WR2: WR2
    };
}



/**
 * 获得大盘实时的信息
 * @param code {String} 只能包括以下四个值
 * 上证指数      深证成指      沪深300        沪深300
 * sh000001     sz399001     sh000300      sz399300
 * @param callback (err, stockRTInfo) => {}
 *
 * ！！！！！！！！！！！！！注意括号中的单位，没有提到单位的属性，就是不用加单位！！！！！！！！！！！！！！！
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
                    "amplitude": 振幅（已经乘了100，单位为"%"）
  }
 */
exports.getMarketRTInfo = (code, callback) => {
    RTTool.obtainMarketRTInfoByCode(code, (err, stockRTInfo) => {
        callback(err, stockRTInfo);
    });
};

/**
 * 获得大盘日线指数
 * @param code {String} 只能包括以下四个值
 * 上证指数      深证成指      沪深300        沪深300
 * sh000001     sz399001     sh000300      sz399300
 * @param callback (err, marketIndex) => {}
 *
 * ！！！！！！！！！！！！！！！！！！！！！！！！注意，大盘指数没有前复权和后复权！！！！！！！！！！！！！！！！！！
 *
 * marketIndex如下所示
 * {
        categoryData: {Array},
        KLineValue: {Array},
        changeRates: {Array},
        volumns: {Array},
        kIndexes: {Array},
        dIndexes: {Array},
        jIndexes: {Array},
        difs: {Array},
        deas: {Array},
        macds: {Array},
        rsi6s: {Array},
        rsi12s: {Array},
        rsi24s: {Array},
        BIAS6: {Array},
        BIAS12: {Array},
        BIAS24: {Array},
        Boll: {Array},
        upper: {Array},
        lower: {Array},
        WR1: {Array},
        WR2: {Array}
 * }
 */
exports.getDailyMarketIndex = (code, callback) => {
    marketIndexDB.getMarketIndexByCode(code, (err, docs) => {
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
                one_day_data.push(parseFloat(data["changeRate"].toFixed(2)));
                one_day_data.push(data["volume"]);

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
            callback(null, splitIndex(all_day_data));
        }
    });
};


/**
 * 获得大盘周线指数
 * @param code {String} 只能包括以下四个值
 * 上证指数      深证成指      沪深300        沪深300
 * sh000001     sz399001     sh000300      sz399300
 * @param callback (err, marketIndex) => {}
 *
 * ！！！！！！！！！！！！！！！！！！！！！！！！注意，大盘指数没有前复权和后复权！！！！！！！！！！！！！！！！！！
 *
 * marketIndex如下所示
 * {
        categoryData: {Array},
        KLineValue: {Array},
        changeRates: {Array},
        volumns: {Array},
        kIndexes: {Array},
        dIndexes: {Array},
        jIndexes: {Array},
        difs: {Array},
        deas: {Array},
        macds: {Array},
        rsi6s: {Array},
        rsi12s: {Array},
        rsi24s: {Array},
        BIAS6: {Array},
        BIAS12: {Array},
        BIAS24: {Array},
        Boll: {Array},
        upper: {Array},
        lower: {Array},
        WR1: {Array},
        WR2: {Array}
 * }
 */
exports.getWeeklyMarketIndex = (code, callback) => {
    marketIndexDB.getMarketIndexByCode(code, (err, docs) => {
        if (err) {
            callback(err, null);
        }
        else {
            let week_docs = [];
            for (let i = docs.length - 1; i >= 0; i--) {
                if (docs[i]["volume"] === 0)
                    continue;
                let week_date = docs[i]["date"];
                let week_open = docs[i]["open"];
                let week_close = docs[i]["close"];
                let week_high = [];
                let week_low = [];

                let week_volume = [];
                let week_changePrice = [];
                let week_changeRate = 0.0;
                // 判断有没有到周一
                while (i >= 0 && docs[i]["date"].getDay() !== 1) {
                    if (docs[i]["volume"] !== 0) {
                        week_open = docs[i]["open"];
                        week_high.push(docs[i]["high"]);
                        week_low.push(docs[i]["low"]);
                        week_volume.push(docs[i]["volume"]);
                        week_changePrice.push(docs[i]["changePrice"]);
                    }
                    i--;
                }
                // 此时i指向的是周一，仍然是这一周的数据，需要加进去
                if (i >= 0 && docs[i]["volume"] !== 0) {
                    week_open = docs[i]["open"];
                    week_high.push(docs[i]["high"]);
                    week_low.push(docs[i]["low"]);
                    week_volume.push(docs[i]["volume"]);
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
                    "volume": week_volume.reduce((x, y) => { return x + y; }),
                    "changeRate": week_changeRate,
                    "changePrice": week_changePrice.reduce((x, y) => { return x + y; }),
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

            // 计算乖离率BIAS所需要的临时数据
            let MA6 = [];       // 6日移动平均线
            let MA12 = [];      // 12日移动平均线
            let MA24 = [];      // 24日移动平均线

            // 计算布林线Boll所需要的临时数据
            let MA20 = [];

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
                one_week_data.push(parseFloat(data["changeRate"].toFixed(2)));
                one_week_data.push(data["volume"]);

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
            callback(null, splitIndex(all_week_data));
        }
    });
};


/**
 * 获得大盘月线指数
 * @param code {String} 只能包括以下四个值
 * 上证指数      深证成指      沪深300        沪深300
 * sh000001     sz399001     sh000300      sz399300
 * @param callback (err, marketIndex) => {}
 *
 * ！！！！！！！！！！！！！！！！！！！！！！！！注意，大盘指数没有前复权和后复权！！！！！！！！！！！！！！！！！！
 *
 * marketIndex如下所示
 * {
        categoryData: {Array},
        KLineValue: {Array},
        changeRates: {Array},
        volumns: {Array},
        kIndexes: {Array},
        dIndexes: {Array},
        jIndexes: {Array},
        difs: {Array},
        deas: {Array},
        macds: {Array},
        rsi6s: {Array},
        rsi12s: {Array},
        rsi24s: {Array},
        BIAS6: {Array},
        BIAS12: {Array},
        BIAS24: {Array},
        Boll: {Array},
        upper: {Array},
        lower: {Array},
        WR1: {Array},
        WR2: {Array}
 * }
 */
exports.getMonthlyMarketIndex = (code, callback) => {
    marketIndexDB.getMarketIndexByCode(code, (err, docs) => {
        if (err) {
            callback(err, null);
        }
        else {
            let month_docs = [];
            for (let i = docs.length - 1; i >= 0; i--) {
                if (docs[i]["volume"] === 0)
                    continue;
                let month_date = docs[i]["date"];
                let month_open = docs[i]["open"];
                let month_close = docs[i]["close"];
                let month_high = [];
                let month_low = [];
                let month_volume = [];
                let month_changePrice = [];
                let month_changeRate = 0;

                let now_month = docs[i]["date"].getMonth();
                // 判断月份有没有改变
                while (i >= 0 && docs[i]["date"].getMonth() === now_month) {
                    if (docs[i]["volume"] !== 0) {
                        month_open = docs[i]["open"];
                        month_high.push(docs[i]["high"]);
                        month_low.push(docs[i]["low"]);
                        month_volume.push(docs[i]["volume"]);
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
                    "volume": month_volume.reduce((x, y) => { return x + y; }),
                    "changeRate": month_changeRate,
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

            // 计算乖离率BIAS所需要的临时数据
            let MA6 = [];       // 6日移动平均线
            let MA12 = [];      // 12日移动平均线
            let MA24 = [];      // 24日移动平均线

            // 计算布林线Boll所需要的临时数据
            let MA20 = [];

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
                one_month_data.push(parseFloat(data["changeRate"].toFixed(2)));
                one_month_data.push(data["volume"]);

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
            callback(null, splitIndex(all_month_data));
        }
    });
};