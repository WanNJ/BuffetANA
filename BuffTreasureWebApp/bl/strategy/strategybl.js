/**
 * Created by slow_time on 2017/5/9.
 */

let strategyDAO = require('../../models/strategy');

// 腌制好的所有数据，用于各种图表的计算
let pickleDatas;

/**
 * 基准收益率列表
 */
let baseRates;
/**
 * 策略收益率表
 */
let strategyRates;
/**
 * 基准年化收益率
 */
let baseYearProfitRate;
/**
 * 策略年化收益率
 */
let yearProfitRate;
/**
 * 最大回撤率
 */
let largestBackRate;


/**
 *
 * @param beginDate
 * @param endDate
 * @param stockPoolConditionVO
 * @param rank
 * @param filter
 * @param tradeModelVO
 * @param envSpecDay
 */
exports.initMixed = function (beginDate, endDate, stockPoolConditionVO, rank, filter, tradeModelVO, envSpecDay, callback) {
    strategyDAO.getPickleData(beginDate, endDate, stockPoolConditionVO,
        rank, filter, tradeModelVO, envSpecDay, (err, docs) => {
        pickleDatas = docs;
    })
};

/**
 * 初始化一些需要重复计算的参数
 * 只能在pickleDatas初始化后调用
 */
function initPara(beginDate, endDate) {
    baseRates = [];
    strategyRates = [];
    pickleDatas = pickleDatas.filter(t => t.backDatas.length > 0);
    let sumOfBase = 100000;
    let sumOfStrategy = 100000;
    pickleDatas.forEach(pickleData => {
        let tempRate = 0.0;
        sumOfBase += sumOfBase * pickleData.baseProfitRate;
        baseRates.push(pickleData.baseProfitRate);

        let buyMoney = 0;
        let sellMoney = 0;

        pickleData.backDatas.forEach(backData => {
            let cnt = 100 / backData.firstDayOpen;
            buyMoney += 100;
            sellMoney += cnt * backData.lastDayClose;
            tempRate += (sellMoney - buyMoney) / buyMoney;
        });
        strategyRates.push(tempRate / pickleData.backDatas.length);
        sumOfStrategy += sumOfStrategy * (tempRate / pickleData.backDatas.length);
    });
    baseYearProfitRate = (sumOfBase - 100000) / 100000;
    baseYearProfitRate = baseYearProfitRate / (endDate - beginDate) / (1000*60*60*24) * 365;
    yearProfitRate = (sumOfStrategy - 100000) / 100000;
    yearProfitRate = yearProfitRate / (endDate - beginDate) / (1000*60*60*24) * 365;
}

/**
 * 获得协方差
 * @param a
 * @param b
 * @returns {number}
 */
function getCOV(a, b) {
    let aAve = getAverage(a);
    let bAve = getAverage(b);
    let sum = 0.0;
    for (let i = 0; i < a.length; i++) {
        sum += (a[i] - aAve) * (b[i] - bAve);
    }
    return sum / a.length;
}

/**
 * 获得平均值
 * @param a
 * @returns {number}
 */
function getAverage(a) {
    return a.reduce((x, y) => { return x + y; }) / a.length;
}

/**
 * 获得方差
 * @param a
 * @returns {number}
 */
function getVariance(a) {
    let ave = getAverage(a);
    let sum = 0.0;
    a.forEach(x => {
        sum += Math.pow((x - ave), 2);
    });
    return sum / a.length;
}

/**
 * 获得标准差
 * @param a
 * @returns {number}
 */
function getSTD(a) {
    return Math.sqrt(getVariance(a));
}

/**
 * 获得最大回撤率
 * @param a
 * @returns {number}
 */
function getMaxDrawDown(a) {
    let minSum = 0.0, thisSum = 0.0;

    for(let i = 1; i < a.length; i++) {
        thisSum += a[i];
        if(thisSum < minSum)
            minSum = thisSum;
        else if(thisSum > 0)
            thisSum = 0;
    }
    return -minSum;
}