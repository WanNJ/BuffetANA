/**
 * Created by slow_time on 2017/5/27.
 */
let allStockDB = require('../../models/allStock').allStockDB;
let singleStockDB = require('../../models/singleStock.js').singleStockDB;
let statisticTool = require('../tool/statisticTool');


/**
 * 根据所给股票，选取出同行业中正相关度最高的股票，并依据此来推断可能盈利率
 * @param code{String} 股票代码
 * @param holdingPeriod{Number} 持仓期
 * @param callback 形如(err, industryCorrelationVO) => {}
 * industryCorrelationVO是一个JSON对象，格式如下
 * {
 *      "code": {String}                 股票代码
 *      "name": {String}                 股票名称
 *      "correlation": {Number}          两个股票的相关度，其绝对值小于1；正，则为正相关；负则为负相关；绝对值为1时，表示线性相关
 *      "profitRate": {Number}           预测的盈利率
 *      "base": {Array}                  散点图的X轴
 *      "compare": {Array}               散点图的Y轴
 * }
 */
exports.getInIndustryCorrelationResult = (code, holdingPeriod, callback) => {

    // 获得该股票所在的行业
    allStockDB.getIndustryByCode(code, (err, doc) => {
        if (err)
            callback(err, null);
        else {
            // 获得该行业所有的股票代码
            allStockDB.getStocksByIndustry(doc["industry"], (err, docs) => {
                if (err)
                    callback(err, null);
                else {
                    singleStockDB.getStockInfoByCode(code, (err, base) => {
                        if (err)
                            callback(err, null);
                        else {
                            base.reverse();
                            base = base.filter(t => t["volume"] !== 0).slice(0, 200);
                            if (base.length === 0)
                                callback(null, null);
                            else {
                                let maxCorrelation = 0.0;
                                let industryCorrelationVO = null;
                                for (let stock in docs) {
                                    if (stock["code"] === code)
                                        continue;
                                    singleStockDB.getStockInfoByCode(stock["code"], (err, compared) => {
                                        compared.reverse();
                                        compared = compared.filter(t => t["volume"] !== 0).slice(0 ,200);
                                        if (compared.length - holdingPeriod > 0) {
                                            let baseTemp = [];
                                            if (compared.length - holdingPeriod < base.length) {
                                                baseTemp = base.slice(0, compared.length - holdingPeriod);
                                            }
                                            else {
                                                baseTemp = [...base];
                                            }
                                            let comparedTemp = compared.slice(holdingPeriod, compared.length);
                                            let correlation = getCorrelation(baseTemp, comparedTemp);
                                            if (Math.abs(correlation) > Math.abs(maxCorrelation)) {
                                                maxCorrelation = correlation;
                                                let profitRate = (compared[0]["afterAdjClose"] - compared[holdingPeriod -1]["afterAdjClose"]) / compared[holdingPeriod -1]["afterAdjClose"] * correlation;
                                                baseTemp.reverse();
                                                comparedTemp.reverse();
                                                industryCorrelationVO = {
                                                    "code": stock["code"],
                                                    "name": stock["name"],
                                                    "correlation": correlation,
                                                    "profitRate": profitRate,
                                                    "base": baseTemp.map(t => t["afterAdjClose"]),
                                                    "compare": comparedTemp.map(t => t["afterAdjClose"])
                                                };
                                            }
                                        }
                                    });
                                }
                                callback(null, industryCorrelationVO);
                            }
                        }
                    });
                }
            });
        }
    });

};


function getCorrelation(base, compared) {
    let a = base.map(t => t["afterAdjClose"]);
    let b = compared.map(t => t["afterAdjClose"]);
    return statisticTool.getCOV(a, b) / (statisticTool.getSTD(a) * statisticTool.getSTD(b));
}