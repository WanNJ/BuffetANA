/**
 * Created by slow_time on 2017/5/14.
 */
const mongoose = require('mongoose');
let singleStockDB = require('../singleStock').singleStockDB;
let allStockDB = require('../allstock').allStockDB;
const strategySchema = mongoose.Schema({
    "date": Date,
    "name" : String,
    "RSV" : Number,
    "KDJ_K" : Number,
    "KDJ_D" : Number,
    "KDJ_J" : Number,
    "MACD_DIF" : Number,
    "MACD_DEA" : Number,
    "MACD" : Number,
    "RSI" :Number,
    "MOM" : Array,
    "MA" : Array
});


exports.saveOtherDB = (callback) => {
    allStockDB.getAllStockCodeAndName((err, docs) => {
        docs.forEach(doc => {
            singleStockDB.getStockInfoByCode(doc["code"], (err, docs) => {
                if (err)
                    callback(err, null);
                else {
                    let strategy = mongoose.model('s' + doc["code"], strategySchema);
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
                        // let dailyData = new strategy({
                        //     "date" : data["date"],
                        //     "name" : data["name"],
                        //     "RSV" : rsv,
                        //     "KDJ_K" : k,
                        //     "KDJ_D" : d,
                        //     "KDJ_J" : j,
                        //     "MACD_DIF" : DIF,
                        //     "MACD_DEA" : DEA,
                        //     "MACD" : MACD,
                        //     "RSI" : RSI14,
                        //     "MOM" : [],
                        //     "MA" : []
                        // });
                        let dailyData = {
                            "date" : data["date"],
                            "name" : data["name"],
                            "RSV" : rsv,
                            "KDJ_K" : k,
                            "KDJ_D" : d,
                            "KDJ_J" : j,
                            "MACD_DIF" : DIF,
                            "MACD_DEA" : DEA,
                            "MACD" : MACD,
                            "RSI" : RSI14,
                            "MOM" : [],
                            "MA" : []
                        };
                        console.log(dailyData);
                        // dailyData.save((err, data) => {
                        //     callback(err, data);
                        // });
                    });
                }
            });
        });
        callback(null, 'ok');
    });

};
