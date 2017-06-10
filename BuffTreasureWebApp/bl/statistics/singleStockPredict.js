/**
 * Created by slow_time on 2017/6/3.
 */

// Coefficient of Variation 变异系数
const statisticTool = require('../tool/statisticTool');
const singleStockDB = require('../../models/singleStock').singleStockDB;
const exec = require('child_process').exec;

/**
 * 获得某支股票的风险系数
 * @param code
 * @param callback 形如(err, doc) => {}
 * doc是一个数组，eg: [0.5, '高'] 第一个是风险系数，Number类型(已经保留了两位小数)
 * 第二个是风险程度，String类型（只有'高', '中', '低'三种可能）
 */
exports.getCoefficientOfRisk = (code, callback) => {
    singleStockDB.getStockInfoByCode(code, (err, docs) => {
        if (err)
            callback(err, null);
        else {
            let stockInfo = docs.filter(s => s["volume"] !== 0).map(s => s["afterAdjClose"]);
            let CV = statisticTool.getCoefficientOfVariation(stockInfo);
            let result = [parseFloat(CV.toFixed(2))];
            if (CV > 0.8)
                result.push('高');
            else if (CV <= 0.8 && CV > 0.5)
                result.push('中');
            else
                result.push('低');
            callback(null, result);
        }
    });
};


/**
 * 根据个股今天的开盘价预测今日是涨还是跌
 * @param code 股票代码
 * @param open_price 股票当日的开盘价
 * @param callback (err, result) => {}
 * result {Array} 形如
 * ！！！！！！！！！！！！！注意！！！！！两个元素的类型都是String！！！！！！
 *
 *  可信度（已乘100，单位为"%"）            预测今日涨（'1'代表涨，'2'代表跌）
 * ['53.23',                             '1']
 */
exports.isUpOrDown = (code, open_price, callback) => {
    exec('python3' + ' /Users/slow_time/BuffettANA/BuffTreasureWebApp/bl/statistics/StockPredict.py ' +code + ' ' + open_price, function(err, stdout, stderr){
        if(err) {
            callback(err, null);
        }
        if(stdout) {
            let result = stdout.substr(0, stdout.length-1).split(',');
            result[0] = (parseFloat(result[0]) * 100).toFixed(2);
            callback(null, [...result]);
        }
    });
};


exports.SVMAnalyze = (holdingDays) => {

};

exports.NNAnalyze = (holdingDays, isMarket, iterationNum, learningWay) => {

};

exports.CNNAnalyze = (holdingDays, isMarket, iterationNum, learningWay) => {

};
