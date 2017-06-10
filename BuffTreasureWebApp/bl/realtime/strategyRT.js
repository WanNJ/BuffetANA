/**
 * Created by wshwbluebird on 2017/6/10.
 */


const curThermeoeter = require('./thememometerRT')
const strategyDB = require('../../models/strategy').strategyDB
const stockRTRec = require('../strategy/strategyPredict')
const StockPool = require('../../vo/StockPoolConditionVO').StockPoolConditionVO;
const RTStockDB = require('../../models/RTSTock').RTStockDB


/**
 "profitAbility" : profitAbility,            // 盈利能力：策略的盈亏比(回测期间总利润除以总亏损)越大，该项分值越高；
 "stability" : stability,                    // 稳定性：策略的波动越小，该项分值越高
 "chooseStockAbility" : chooseStockAbility,  // 选股能力：策略的成功率越大，该项分值越高
 "absoluteProfit" : absoluteProfit,          // 绝对收益：策略的年化收益率越大，该项分值越高
 "antiRiskAbility" : antiRiskAbility,        // 抗风险能力：策略的回撤越小，该项分值越高；
 "strategyScore" : strategyScore             // 策略总得分，上面5项得分之和
 * @param callback
 */
exports.calculateStockRt = (callback) =>{
    curThermeoeter.getCurrentENV((err,env)=>{
        let result = {}

         getAndPredict = function (env,cap,result) {
            return new Promise((resolve,reject)=>{
                strategyDB.getBestStrategyByEnv(env,cap,1,(err,doc)=>{
                    let temp = doc[0]['stockPoolConditionVO']
                    // console.log(cap)
                    // console.log(doc[0]['_id'])
                    let stockpool = new StockPool(temp.stockPool,temp.benches,temp.industries,temp.excludeST)
                    stockRTRec.getRTPickleData(stockpool,doc[0]['rank'],doc[0]['filter'],(err,stocks)=>{
                        result[cap] = [doc[0]['tradeModelVO'].holdingDays];
                        for(let i = 0 ; i < 5 ; i++){
                            result[cap].push(stocks[i])
                        }
                        resolve(result)
                    })
                })
            })
        }

        getAndPredict(env,'strategyScore',{})
            .then(t=>getAndPredict(env,'chooseStockAbility',t))
            .then(t=>getAndPredict(env,'profitAbility',t))
            .then(t=>getAndPredict(env,'antiRiskAbility',t))
            .then(t=>callback(null,t))

    })
}

/**
 * 形如 err
 * @param callback
 * @constructor
 */
exports.WriteRTStockToDB = (callback)=>{
    this.calculateStockRt((err,doc)=>{
        RTStockDB.upDate(doc,(err)=>{
            callback(err)
        })
    })
}