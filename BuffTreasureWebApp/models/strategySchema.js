/**
 * Created by wshwbluebird on 2017/5/28.
 */
const mongoose = require('mongoose');

let Schema = mongoose.Schema;

//TODO 以后增加动态持仓就要修改持仓方式了
/**
 * 获得回测结果
 * @param beginDate {Date} 回测的开始日期
 * @param endDate  {Date} 回测的结束日期
 * @param stockPoolConditionVO {StockPoolConditionVO} 股票池的选择条件
 * @param rank
 *       策略名称   策略名称  升序／降序   观察期   权重
 * eg : { "r1" :  ["MA",   "asd",      10,     0.4],
 *        "r2" :  ["MOM",  "des",      20,     0.6]
 *       }
 * @param filter
 *         序列            筛选指标        比较符      值
 * eg : {  ' f1:         ["volume",       ">",     1000000],
 *          "f2" :       ["turnOverRate", "<",        0.05]
 *       }
 * @param tradeModelVO {TradeModelVO} 交易模型
 * @param envSpecDay {Number}  市场观察期
 *
 *
 * @param callback 形如 (err, data) => {}
 *
 * eg:  @param markHS
 * {profitAbility: 20,
       stability: 20,
       chooseStockAbility: 20,
       absoluteProfit: 20,
       antiRiskAbility: 20,
       strategyScore: 100
    }
 *
 */

let strategySchema = new Schema({
    beginDate: Date,
    endDate: Date,
    stockPoolConditionVO: {},
    rank:{},
    filter:{},
    tradeModelVO:{},
    envSpecDay:Number,
    markNormal:{},// 正常情况下 情况下的分数
    markHS:{},// high and same 情况下的分数
    markHO:{},// high and opposite 情况下的分数
    markLS:{},// low and same 情况下的分数
    markLO:{},// low and opposite 情况下的分数

}, {collection: 'strategies'});

module.exports =  mongoose.model('strategies', strategySchema);