/**
 * Created by slow_time on 2017/6/3.
 */
const statisticTool = require('../tool/statisticTool');
const singleStockDB = require('../../models/singleStock').singleStockDB;
const exec = require('child_process').exec;
const industryCorrelationTool = require('./industryCorrelationbl');


/**
 * 使用SVM模型进行个股分析
 * @param code 股票代码
 * @param now_price 股票现价，在个股页面直接获取，不要用户填写，如果没有现价，则禁用SVM模型
 * @param holdingDays 持股天数，需要用户填写
 */
exports.SVMAnalyze = (code, now_price, holdingDays) => {
    
};

exports.NNAnalyze = (code, holdingDays, isMarket, iterationNum, learningWay) => {

};

exports.CNNAnalyze = (code, holdingDays, isMarket, iterationNum, learningWay) => {

};


/**
 * ===================================以下是私有方法===================================
 */


/**
 * 获得某支股票的风险系数
 * @param code
 * @param callback 形如(err, doc) => {}
 * doc是一个数组，eg: [0.5, '高'] 第一个是风险系数，Number类型(已经保留了两位小数)
 * 第二个是风险程度，String类型（只有'高', '中', '低'三种可能）
 */
function getCoefficientOfRisk(code, callback) {
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
}


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
function isUpOrDown(code, open_price, callback) {
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
}
