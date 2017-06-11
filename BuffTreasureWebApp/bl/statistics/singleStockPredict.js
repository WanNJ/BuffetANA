/**
 * Created by slow_time on 2017/6/3.
 */
const async = require("async/index.js");
const statisticTool = require('../tool/statisticTool');
const singleStockDB = require('../../models/singleStock').singleStockDB;
const exec = require('child_process').exec;
const industryCorrelationTool = require('./industryCorrelationbl');
const userDB = require('../../models/user').userDB;


/**
 * 使用SVM模型进行个股分析
 * @param userName {String} 用户名称
 * @param code {String} 股票代码
 * @param open_price {Number} 股票当天开盘价，在个股页面直接获取，不要用户填写，如果没有开盘价，则禁用SVM模型
 * @param holdingDays {Number} 持股天数，需要用户填写
 * @param time {Date} 用户进行个股分析时的时间
 */
exports.SVMAnalyze = (userName, code, open_price, holdingDays, time) => {
    async.parallel([
        function (callback) {
            industryCorrelationTool.getIndustryCorrelationResult(code, holdingDays, callback);
        },
        function (callback) {
            getCoefficientOfRisk(code, callback);
        },
        function (callback) {
            isUpOrDown(code, open_price, callback);
        }
    ],
    function (err, results) {
        let message = {};
        if (err) {
            message = {
                time: time,
                isRead: false,
                type: 'error',
                codeOrName: '代码为' + code + '的股票分析结果出错',
                content: {
                    code: code,
                    open: open_price,
                    holdingDays: holdingDays,
                }
            };
        }
        else {
            message = {
                time: time,
                isRead: false,
                type: 'error',
                codeOrName: '代码为' + code + '的股票分析结果出错',
                content: {
                    code: code,
                    open: open_price,
                    holdingDays: holdingDays,
                    relatedCode: results[0]["code"],
                    relatedName: results[0]["name"],
                    correlation: results[0]["correlation"],
                    profitRate: results[0]["profitRate"],
                    base: results[0]["base"],
                    compare: results[0]["compare"],
                    CR: results[1],
                    upOrDown: results[2]
                }
            };
        }
        userDB.addUnreadMessage(userName, message, (err) => {
            if (err)
                console.log('Something wrong has happened when saving ' +  userName + "'s message!");
        });
    });
};


// TODO 以下两个接口的参数我是按照界面来定的，具体的接口参数需要问wsw！！！！！！

/**
 * 使用NN模型进行个股分析
 * @param userName
 * @param code
 * @param holdingDays
 * @param isMarket
 * @param iterationNum
 * @param learningWay
 * @param time
 * @constructor
 */
exports.NNAnalyze = (userName, code, holdingDays, isMarket, iterationNum, learningWay, time) => {

};


/**
 * 使用CNN模型进行个股分析
 * @param userName
 * @param code
 * @param holdingDays
 * @param isMarket
 * @param iterationNum
 * @param learningWay
 * @param time
 * @constructor
 */
exports.CNNAnalyze = (userName, code, holdingDays, isMarket, iterationNum, learningWay, time) => {

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
