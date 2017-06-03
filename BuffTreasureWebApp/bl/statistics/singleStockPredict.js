/**
 * Created by slow_time on 2017/6/3.
 */

// Coefficient of Variation 变异系数
const statisticTool = require('../tool/statisticTool');
const singleStockDB = require('../../models/singleStock').singleStockDB;


/**
 * 获得某支股票的风险系数
 * @param code
 * @param callback 形如(err, doc) => {}
 * doc是一个数组，eg: [0.5, '高'] 第一个是风险系数，Number类型，第二个是风险程度，String类型（只有'高', '中', '低'三种可能）
 */
exports.getCoefficientOfRisk = (code, callback) => {
    singleStockDB.getStockInfoByCode(code, (err, docs) => {
        if (err)
            callback(err, null);
        else {
            let stockInfo = docs.filter(s => s["volume"] !== 0).map(s => s["afterAdjClose"]);
            let CV = statisticTool.getCoefficientOfVariation(stockInfo);
            let result = [CV];
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