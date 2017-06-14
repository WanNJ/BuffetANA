/**
 * Created by slow_time on 2017/6/3.
 */
const async = require("async/index.js");
const statisticTool = require('../tool/statisticTool');
const singleStockDB = require('../../models/singleStock').singleStockDB;
const exec = require('child_process').exec;
const industryCorrelationTool = require('./industryCorrelationbl');
const userDB = require('../../models/user').userDB;
const CNNModel = require('./ML/testCNNModel').CNNPredict;
const RFCModel = require('./ML/testRFCModel').RFCPredict;


/**
 * 使用SVM模型进行个股分析
 * @param userName {String} 用户名称
 * @param code {String} 股票代码
 * @param stockName {String} 股票名称
 * @param open_price {Number} 股票当天开盘价，在个股页面直接获取，不要用户填写，如果没有开盘价，则禁用SVM模型
 * @param holdingDays {Number} 持股天数，需要用户填写
 * @param time {Date} 用户进行个股分析时的时间
 * @param callback {function} (err, isOK) => {} 暂时就当成一个空壳的函数，里面没有内容
 */
exports.SVMAnalyze = (userName, code, stockName, open_price, holdingDays, time = new Date(), callback) => {
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
                codeOrName: '代码为' + code + '的股票相关性分析结果出错',
                stockName: stockName,
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
                type: 'SVM',
                codeOrName: code,
                stockName: stockName,
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
            if (err) {
                console.log('Something wrong has happened when saving ' +  userName + "'s message!");
                callback(err, null);
            }
            else {
                callback(null, "ok");
            }
        });
    });
};


/**
 * 使用RFC模型进行个股分析
 * @param userName {String} 用户名称
 * @param code {String} 股票代码
 * @param stockName {String} 股票名称
 * @param holdingDays {Number} 持股天数，需要用户填写
 * @param time {Date} 用户进行个股分析时的时间
 * @param callback {function} (err, isOK) => {} 暂时就当成一个空壳的函数，里面没有内容
 */
exports.RFCAnalyze = (userName, code, stockName, holdingDays, time = new Date(), callback) => {
    RFCModel.rfcPredict(code, holdingDays, (err, result) => {
        let message = {};
        if (err) {
            message = {
                time: time,
                isRead: false,
                type: 'error',
                codeOrName: '代码为' + code + '的股票特征分析结果出错',
                stockName: stockName,
                content: {
                    code: code,
                    holdingDays: holdingDays,
                }
            };
        }
        else {
            message = {
                time: time,
                isRead: false,
                type: 'RFC',
                codeOrName: code,
                stockName: stockName,
                content: {
                    code: code,
                    holdingDays: holdingDays,
                    importance: result[0]["importance"],
                    down: result[0]["down"],
                    up: result[0]["up"],
                    smooth: result[0]["smooth"],
                    accuracy: result[0]["accuracy"]
                }
            };
        }
        userDB.addUnreadMessage(userName, message, (err) => {
            if (err) {
                console.log('Something wrong has happened when saving ' +  userName + "'s message!");
                callback(err, null);
            }
            else {
                callback(null, "ok");
            }
        })
    })
};


/**
 * 使用CNN模型进行个股分析
 * @param userName 用户名称
 * @param code 股票代码
 * @param stockName {String} 股票名称
 * @param holdingDays 持股天数
 * @param isMarket 是否考虑市场环境（默认为false）
 * @param iterationNum 迭代量（默认为4）
 * @param learningWay 学习方式（默认为'ALL'）
 * @param time {Date} 用户进行个股分析时的时间
 * @param callback {function} (err, isOK) => {} 暂时就当成一个空壳的函数，里面没有内容
 */
exports.CNNAnalyze = (userName, code, stockName, holdingDays, isMarket=false, iterationNum=4, learningWay='ALL', time = new Date(), callback) => {
    CNNModel.betterPredict(code, holdingDays, iterationNum, isMarket, "2006-04-05", learningWay, (err, result) => {
        let message = {};
        if (err) {
            message = {
                time: time,
                isRead: false,
                type: 'error',
                codeOrName: '代码为' + code + '的股票涨幅预测结果出错',
                stockName: stockName,
                content: {
                    code: code,
                    holdingDays: holdingDays,
                    iterationNum: iterationNum,
                    isMarket: isMarket,
                    learningWay: learningWay,
                    beginStr: "2006-04-05"
                }
            };
        }
        else {
            message = {
                time: time,
                isRead: false,
                type: 'CNN',
                codeOrName: code,
                stockName: stockName,
                content: {
                    code: code,
                    holdingDays: holdingDays,
                    iterationNum: iterationNum,
                    isMarket: isMarket,
                    learningWay: learningWay,
                    beginStr: "2006-04-05",
                    process: result["process"],
                    less10: result["less10"],
                    "10-7_5": result["10-7.5"],
                    "7_5-5": result["7.5-5"],
                    "5-2_5": result["5-2.5"],
                    "0-2_5": result["0-2.5"],
                    "2_5-0": result["2.5-0"],
                    "2_5-5": result["2.5-5"],
                    "5-7_5": result["5-7.5"],
                    "7_5-10": result["7.5-10"],
                    more10: result["more10"],
                    accuracy: result["accuracy"]
                }
            };
        }
        userDB.addUnreadMessage(userName, message, (err) => {
            if (err) {
                console.log('Something wrong has happened when saving ' +  userName + "'s message!");
                callback(err, null);
            }
            else {
                callback(null, "ok");
            }
        })
    });
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
    exec('python3' + ' ../bl/statistics/StockPredict.py ' +code + ' ' + open_price, function(err, stdout, stderr){
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
