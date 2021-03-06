/**
 * Created by slow_time on 2017/5/9.
 */

let strategyDAO = require('./strategyPickle');
let statisticTool = require('../tool/statisticTool');
let strategyDB = require('../../models/strategy').strategyDB;
let strategyTool = require('./strategyTool');

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
 * add by  wsw
 * modified by ty
 * TODO 那个环境的参数已经不需要了，直接一次性传上来所有环境的参数
 * TODO 加了一个正常环境下的（就是没有划分市场的环的境）回测情况（同迭代二）
 */

/**
 * 获得回测结果
 * @param beginDate {Date} 回测的开始日期
 * @param endDate  {Date} 回测的结束日期
 * @param stockPoolConditionVO {StockPoolConditionVO} 股票池的选择条件
 * @param rank
 *       策略名称    名字 升序／降序   观察期   权重
 * eg : { "r1" :  ["MA" , "asd",      10,     0.4],
 *        "r2" : ["MOM", "des",      20,     0.6]
 *       }
 * @param filter
 *        筛选指标          比较符     值
 * eg : { "f1" :          ["volume", ">",  1000000],
 *        "f2" :        ["volume" ,"<",   0.05]
 *       }
 * @param tradeModelVO {TradeModelVO} 交易模型
 * @param envSpecDay {Number}  市场观察期
 *
 * @param setProcess 设置回测进度的函数，形如 (process) => {}
 * process ：Number类型，可选范围：0-99
 * 内部调用例子：setProcess(56);  //告诉浏览器回测进度到56%了
 * @param callback 形如 (err, data) => {}
 * data {Array}  data[0]    data[1]             data[2]             data[3]                data[4]
 *               normal     市场温度高，趋同性强   市场温度高 趋反性强   市场温度低，趋同性强      市场温度低，趋反性强
 * ===================callback中的data是一个长度为5的数组，元素的类型为JSON格式，单个元素内容如下====================
 * {
        "backDetail" : {                                     // 回测表结果
                            "yearProfitRate": Number,        // 年化收益率
                            "baseYearProfitRate": Number,    // 基准年化收益率
                            "largestBackRate": Number,       // 最大回撤率
                            "sharpRate": Number,             // 夏普率
                            "alpha": Number,                 // alpha
                            "beta": Number                   // beta
                        },
        "strategyDayRatePiece" : {                             // 策略的累计收益折线图数据
                                      "date" : Array,          // x轴的日期
                                      "profitRate" : Array     // y轴的收益率
                                  },
        "baseDayRatePiece" : {                             // 基准的累计收益折线图数据
                                  "date" : Array,          // x轴的日期
                                  "profitRate" : Array     // y轴的收益率
                              },
        "strategyEstimateResult" : {                                    // 策略评估的雷达图数据
                                        "profitAbility" : Number,       // 盈利能力：策略的盈亏比(回测期间总利润除以总亏损)越大，该项分值越高；
                                        "stability" : Number,           // 稳定性：策略的波动越小，该项分值越高
                                        "chooseStockAbility" : Number,  // 选股能力：策略的成功率越大，该项分值越高
                                        "absoluteProfit" : Number,      // 绝对收益：策略的年化收益率越大，该项分值越高
                                        "antiRiskAbility" : Number,     // 抗风险能力：策略的回撤越小，该项分值越高；
                                        "strategyScore" : Number        // 策略总得分，上面5项得分之和
                                    },
        "profitDistributePie" : {                         // 收益分布饼图的数据
                                    "green0" : Number,    // 收益为-3.5%到0的次数
                                    "green35" : Number,   // 收益为-3.5%到-7.5%的次数
                                    "green75" : Number,   // 收益小于-7.5%的次数
                                    "red0" : Number,      // 收益为0到3.5%的次数
                                    "red35" : Number,     // 收益为3.5%到7.5%的次数
                                    "red75" : Number      // 收益大于7.5%的次数
                                }
        "historyTradeRecord" : [{                         // 历史交易数据
                                    "code" : String,      // 股票代码
                                    "name" : String,      // 股票简称
                                    "buyTime" : String,   // 买入时间
                                    "sellTime" : String,  // 卖出时间
                                    "buyPrice" : Number,  // 买入价
                                    "sellPrice" : Number, // 卖出价
                                    "yieldRate" : Number  // 单次收益率
                                 }, ...]
  }
 */
exports.getBackResults = function (beginDate, endDate, stockPoolConditionVO, rank, filter, tradeModelVO, envSpecDay, setProcess, callback) {
    strategyDAO.getPickleData(beginDate, endDate, stockPoolConditionVO,
        rank, filter, tradeModelVO, envSpecDay,(pro)=>{
            setProcess(pro)
        } ,(err, docs) => {
        if (err)
            callback(err, null);
        else {
            setProcess(98);
            let results = [];
            let keys = Object.keys(docs);
            for (let i = 0; i < 5; i++) {
                pickleDatas =  docs[keys[i]];

                // 没有动态持仓
                if (tradeModelVO.winRate === null){
                    initPara(beginDate, endDate);
                }

                // 有动态持仓
                else
                    initParaWithDynamic(beginDate, endDate, tradeModelVO.winRate, tradeModelVO.loseRate);

                let result = {
                    "backDetail" : getBackDetail(),
                    "strategyDayRatePiece" : getStrategyDayRatePiece(),
                    "baseDayRatePiece" : getBaseDayRatePiece(),
                    "strategyEstimateResult" : getStrategyEstimateResult(),
                    "profitDistributePie" : getProfitDistributePie(),
                    "historyTradeRecord" : getHistoryTradeRecord()
                };
                results.push(result);
            }
            let strategy = {
                beginDate: beginDate,
                endDate: endDate,
                stockPoolConditionVO: stockPoolConditionVO,
                rank: rank,
                filter: filter,
                tradeModelVO: tradeModelVO,
                envSpecDay: envSpecDay,
                markNormal: results[0]["strategyEstimateResult"],// 正常情况下 情况下的分数
                markHS: results[1]["strategyEstimateResult"],// high and same 情况下的分数
                markHO: results[2]["strategyEstimateResult"],// high and opposite 情况下的分数
                markLS: results[3]["strategyEstimateResult"],// low and same 情况下的分数
                markLO: results[4]["strategyEstimateResult"],// low and opposite 情况下的分数
            };
            strategyDB.getAllStrategy((err, strategies) => {
                if (!err) {
                    console.log('no err');
                    let same = false;
                    for (let i = 0; i < strategies.length; i++) {

                        console.log(strategies[i]);
                        console.log(strategy);
                        // 两个策略是否相同
                        if (strategyTool.compareTo(strategies[i], strategy)) {
                            same = true;
                            console.log(same);
                            // 策略相同，回测区间不相同
                            if ((strategy["beginDate"] - strategies[i]["beginDate"]) !== 0 || (strategy["endDate"] - strategies[i]["endDate"]) !== 0) {
                                // 回测区间不同，且新策略的回测区间更加接近现在的日期
                                if ((strategy["endDate"] - strategies[i]["endDate"]) > 0) {
                                    strategyDB.removeStrategy(strategy[i]["_id"], (err) => {
                                        if (!err) {
                                            strategyDB.saveStrategy(strategy, (err, data) => {
                                                callback(err, results);
                                            });
                                        }
                                    });
                                }
                                else {
                                    callback(null, results);
                                }
                            }
                            // 策略相同，回测区间也相同
                            else {
                                // 新策略的持股数更接近5
                                if (Math.abs(strategies[i]["tradeModelVO"]["holdingNums"] - 5) > Math.abs(strategy["tradeModelVO"]["holdingNums"] - 5)) {
                                    strategyDB.removeStrategy(strategy[i]["_id"], (err) => {
                                        if (!err) {
                                            strategyDB.saveStrategy(strategy, (err, data) => {
                                                if (err)
                                                    console.log('save strategy error');
                                                else
                                                    callback(null, results);
                                            })
                                        }
                                    });
                                }
                                // 两个策略的持股数一样接近5
                                else if (Math.abs(strategies[i]["tradeModelVO"]["holdingNums"] - 5) === Math.abs(strategy["tradeModelVO"]["holdingNums"] - 5)) {
                                    // 新策略的得分高
                                    if (strategies[i]["markNormal"]["strategyScore"] < strategy["markNormal"]["strategyScore"]) {
                                        strategyDB.removeStrategy(strategy[i]["_id"], (err) => {
                                            if (!err) {
                                                strategyDB.saveStrategy(strategy, (err, data) => {
                                                    if (err)
                                                        console.log('save strategy error');
                                                    else
                                                        callback(null, results);
                                                })
                                            }
                                        });
                                    }
                                    else {
                                        callback(null, results);
                                    }
                                }
                            }
                            break;
                        }
                    }
                    if (!same) {
                        console.log('not same');
                        strategyDB.saveStrategy(strategy, (err, data) => {
                            callback(err, results);
                        });
                    }
                }
                else {
                    callback(err, null);
                }
            });
        }
    });
};



/**
 * ================================下面是一些私有的计算方法，并不是调用的接口=================================
 */



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
    baseYearProfitRate = baseYearProfitRate / ((endDate - beginDate) / (1000*60*60*24)) * 365;
    yearProfitRate = (sumOfStrategy - 100000) / 100000;
    yearProfitRate = yearProfitRate / ((endDate - beginDate) / (1000*60*60*24)) * 365;
}


function initParaWithDynamic(beginDate, endDate, winRate, loseRate) {
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
            let buy_price = backData.priceList[0];
            let sell_price = backData.priceList[1];
            let i = 1;
            for (i; i < backData.priceList.length - 1; i++) {
                sell_price = backData.priceList[i];
                // 如果低于止损率，则直接卖出
                if ((backData.priceList[i] - buy_price) / buy_price <= loseRate) {
                    break;
                }
            }
            // 如果持仓期内收益率超过止盈率，则坚定持有一个持仓期
            if (i === (backData.priceList.length - 1) && (sell_price - buy_price) / buy_price >= winRate) {
                sell_price = backData.priceList[i];
            }
            let cnt = 100 / buy_price;
            buyMoney += 100;
            sellMoney += cnt * sell_price;
            tempRate += (sellMoney - buyMoney) / buyMoney;
        });
        strategyRates.push(tempRate / pickleData.backDatas.length);
        sumOfStrategy += sumOfStrategy * (tempRate / pickleData.backDatas.length);
    });
    baseYearProfitRate = (sumOfBase - 100000) / 100000;
    baseYearProfitRate = baseYearProfitRate / ((endDate - beginDate) / (1000*60*60*24)) * 365;
    yearProfitRate = (sumOfStrategy - 100000) / 100000;
    yearProfitRate = yearProfitRate / ((endDate - beginDate) / (1000*60*60*24)) * 365;
}


/**
 * 获得回测表数据
 * @returns {{yearProfitRate: *, baseYearProfitRate: *, largestBackRate: (number|*), sharpRate: number, alpha: number, beta: number}}
 */
function getBackDetail() {
    if (baseRates.length === 0 || strategyRates.length === 0) {
        return {
            "yearProfitRate": yearProfitRate,            // 年化收益率
            "baseYearProfitRate": baseYearProfitRate,    // 基准年化收益率
            "largestBackRate": 0,                       // 最大回撤率
            "sharpRate": 0,                             // 夏普率
            "alpha": 0,                                 // alpha
            "beta": 0
        };
    }
    let beta = statisticTool.getCOV(strategyRates, baseRates) / statisticTool.getVariance(baseRates);

    /**
     * 无风险利率，使用中国1年期银行定期存款回报
     */
    let r = 0.0175;
    let alpha = (yearProfitRate - r) - beta * (baseYearProfitRate - r);

    let sharpRate = (yearProfitRate - r) / statisticTool.getSTD(strategyRates);

    largestBackRate = getMaxDrawDown(strategyRates);

    return {
        "yearProfitRate": yearProfitRate,            // 年化收益率
        "baseYearProfitRate": baseYearProfitRate,    // 基准年化收益率
        "largestBackRate": largestBackRate,          // 最大回撤率
        "sharpRate": sharpRate,                      // 夏普率
        "alpha": alpha,                              // alpha
        "beta": beta                                 // beta
    };
}


/**
 * 获得回测累计收益比较图中的策略收益率累积折线图
 * @returns {{date: Array, profitRate: Array}}
 */
function getStrategyDayRatePiece() {
    let sum = 100000.0;
    let dayRatePieceVO = {
        "date" : [],          // x轴的日期
        "profitRate" : []     // y轴的收益率
    };
    for(let i = 0; i < pickleDatas.length; i++) {
        sum += strategyRates[i] * sum;
        dayRatePieceVO["date"].push(pickleDatas[i].endDate.toISOString().substr(0, 10));
        dayRatePieceVO["profitRate"].push((sum - 100000.0) / 100000.0 * 100);
    }
    return dayRatePieceVO;
}


/**
 * 获得回测累计收益比较图中的基准收益率累积折线图
 * @returns {{date: Array, profitRate: Array}}
 */
function getBaseDayRatePiece() {
    let sum = 100000.0;
    let dayRatePieceVO = {
        "date" : [],          // x轴的日期
        "profitRate" : []     // y轴的收益率
    };
    for(let i = 0; i < pickleDatas.length; i++) {
        sum += baseRates[i] * sum;
        dayRatePieceVO["date"].push(pickleDatas[i].endDate.toISOString().substr(0, 10));
        dayRatePieceVO["profitRate"].push((sum - 100000.0) / 100000.0 * 100);
    }
    return dayRatePieceVO;
}


/**
 * 获得收益分布饼图
 * @returns {{green0: number, green35: number, green75: number, red0: number, red35: number, red75: number}}
 */
function getProfitDistributePie() {
    /**
     * 收益为-3.5%到0的次数
     */
    let green0 = 0;
    /**
     * 收益为-3.5%到-7.5%的次数
     */
    let green35 = 0;
    /**
     * 收益小于-7.5%的次数
     */
    let green75 = 0;
    /**
     * 收益为0到3.5%的次数
     */
    let red0 = 0;
    /**
     * 收益为3.5%到7.5%的次数
     */
    let red35 = 0;
    /**
     * 收益大于7.5%的次数
     */
    let red75 = 0;

    strategyRates.forEach(strategyRate => {
        if(strategyRate < 0 && strategyRate >= -0.035)
            green0++;
        else if(strategyRate < -0.035 && strategyRate >= -0.075)
            green35++;
        else if(strategyRate < -0.075)
            green75++;
        else if(strategyRate < 0.035 && strategyRate >= 0)
            red0++;
        else if(strategyRate < 0.075 && strategyRate >= 0.035)
            red35++;
        else
            red75++;
    });
    return {
        "green0" : green0,     // 收益为-3.5%到0的次数
        "green35" : green35,   // 收益为-3.5%到-7.5%的次数
        "green75" : green75,   // 收益小于-7.5%的次数
        "red0" : red0,         // 收益为0到3.5%的次数
        "red35" : red35,       // 收益为3.5%到7.5%的次数
        "red75" : red75        // 收益大于7.5%的次数
    };
}



function getStrategyEstimateResult() {
    let antiRiskAbility = Math.round(20 - largestBackRate * 10);
    if (largestBackRate === 0) {
        antiRiskAbility = 0;
    }
    else {
        if (antiRiskAbility > 20)
            antiRiskAbility = 20;
        else if (antiRiskAbility < 0)
            antiRiskAbility = 0;
    }

    let absoluteProfit = Math.round(yearProfitRate / 2 * 20);
    if (absoluteProfit > 20)
        absoluteProfit = 20;
    else if (absoluteProfit < 0)
        absoluteProfit = 0;

    // 计算策略赢的次数
    let winCount = 0;
    //股票的盈亏比率
    let winRate = 0;
    let loseRate = 0;
    //最大赢率
    let maxWinRate = -1;
    //最大亏损率
    let maxLoseRate = 1;
    for(let i = 0; i < strategyRates.length; i++) {
        if(strategyRates[i] > baseRates[i])
            winCount++;
        if(strategyRates[i] > 0)
            winRate += strategyRates[i];
        else
            loseRate += strategyRates[i];

        if(strategyRates[i] > maxWinRate)
            maxWinRate = strategyRates[i];
        else if(strategyRates[i] < maxLoseRate)
            maxLoseRate = strategyRates[i];
    }
    let chooseStockAbility = Math.round(winCount / baseRates.length * 20);
    if(chooseStockAbility > 20)
        chooseStockAbility = 20;
    else if (chooseStockAbility < 0)
        chooseStockAbility = 0;

    let profitAbility = 0;
    if (loseRate === 0)
        profitAbility = 20;
    else {
        profitAbility = Math.round(winRate / (-loseRate) / 5 * 20);
        if (profitAbility > 20)
            profitAbility = 20;
        else if (profitAbility < 0)
            profitAbility = 0;
    }

    let stability = 20 - Math.round((maxWinRate - maxLoseRate) / 2  * 20);
    if (stability > 20)
        stability = 20;
    else if (stability < 0)
        stability = 0;

    let strategyScore = profitAbility + stability + chooseStockAbility + absoluteProfit + antiRiskAbility;

    return {
        "profitAbility" : profitAbility,            // 盈利能力：策略的盈亏比(回测期间总利润除以总亏损)越大，该项分值越高；
        "stability" : stability,                    // 稳定性：策略的波动越小，该项分值越高
        "chooseStockAbility" : chooseStockAbility,  // 选股能力：策略的成功率越大，该项分值越高
        "absoluteProfit" : absoluteProfit,          // 绝对收益：策略的年化收益率越大，该项分值越高
        "antiRiskAbility" : antiRiskAbility,        // 抗风险能力：策略的回撤越小，该项分值越高；
        "strategyScore" : strategyScore             // 策略总得分，上面5项得分之和
    };
}


function getHistoryTradeRecord() {
    let historyTradeRecord= [];
    pickleDatas.forEach(pickleData => {
        pickleData.backDatas.forEach(backData => {
            historyTradeRecord.push({
                "code" : backData.code,      // 股票代码
                "name" : backData.name,      // 股票简称
                "buyTime" : pickleData.beginDate.toISOString().substr(0, 10),   // 买入时间
                "sellTime" : pickleData.endDate.toISOString().substr(0, 10),  // 卖出时间
                "buyPrice" : backData.firstDayOpen,  // 买入价
                "sellPrice" : backData.lastDayClose, // 卖出价
                "yieldRate" : (backData.lastDayClose - backData.firstDayOpen) / backData.firstDayOpen  // 单次收益率
            });
        });
    });
    return historyTradeRecord;
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