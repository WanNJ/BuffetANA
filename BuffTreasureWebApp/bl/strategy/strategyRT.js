/**
 * Created by wshwbluebird on 2017/6/9.
 */

let RTStockDB = require('../../models/RTSTock').RTStockDB;
let StrategyDB = require('../../models/strategy').strategyDB;


/**
 *
 *  "profitAbility" : profitAbility,            // 盈利能力：策略的盈亏比(回测期间总利润除以总亏损)越大，该项分值越高；
    "stability" : stability,                    // 稳定性：策略的波动越小，该项分值越高
    "chooseStockAbility" : chooseStockAbility,  // 选股能力：策略的成功率越大，该项分值越高
    "absoluteProfit" : absoluteProfit,          // 绝对收益：策略的年化收益率越大，该项分值越高
    "antiRiskAbility" : antiRiskAbility,        // 抗风险能力：策略的回撤越小，该项分值越高；
    "strategyScore" : strategyScore
 * @type {callback} 形如 (err,doc)
 * doc 格式
 */


/**
 * 按综合能力推荐的股票 5 只
 * 返回结果为数组 长度为6 的数组   index = 0 的地方 为推荐的持仓天数
 * index = 1 - 5 分别是一个长度为5 的二维数组 第一位为股票代码 ，第二位为股票名字
 * [10,
 [ '000627', '天茂集团' ],
 [ '002024', '苏宁云商' ],
 [ '002065', '东华软件' ],
 [ '002153', '石基信息' ],
 [ '000503', '海虹控股' ]
    ]
 * @param callback
 */
exports.getRccomandStockHighScore = (callback)=>{
    RTStockDB.getStock('strategyScore',(err,doc)=>{
        callback(err,doc)
    })
}

/**
 * 按盈利能力推荐的股票 5 只
 * 返回结果为数组 长度为6 的数组   index = 0 的地方 为推荐的持仓天数
 * index = 1 - 5 分别是一个长度为5 的二维数组 第一位为股票代码 ，第二位为股票名字
 * [10,
 [ '000627', '天茂集团' ],
 [ '002024', '苏宁云商' ],
 [ '002065', '东华软件' ],
 [ '002153', '石基信息' ],
 [ '000503', '海虹控股' ]
 ]
 * @param callback
 */
exports.getRccomandStockProfit = (callback)=>{
    RTStockDB.getStock('profitAbility',(err,doc)=>{
        callback(err,doc)
    })

}


/**
 * chooseStockAbility
 * 按选股能力(策略胜率)能力推荐的股票 5 只
 * 返回结果为数组 长度为6 的数组   index = 0 的地方 为推荐的持仓天数
 * index = 1 - 5 分别是一个长度为5 的二维数组 第一位为股票代码 ，第二位为股票名字
 * [10,
 [ '000627', '天茂集团' ],
 [ '002024', '苏宁云商' ],
 [ '002065', '东华软件' ],
 [ '002153', '石基信息' ],
 [ '000503', '海虹控股' ]
 ]
 * @param callback
 */
exports.getRccomandStockWinRate = (callback)=>{
    RTStockDB.getStock('antiRiskAbility',(err,doc)=>{
        callback(err,doc)
    })

}


/**
 * 按选抗风险能力能力推荐的股票 5 只
 * 返回结果为数组 长度为6 的数组   index = 0 的地方 为推荐的持仓天数
 * index = 1 - 5 分别是一个长度为5 的二维数组 第一位为股票代码 ，第二位为股票名字
 * [10,
 [ '000627', '天茂集团' ],
 [ '002024', '苏宁云商' ],
 [ '002065', '东华软件' ],
 [ '002153', '石基信息' ],
 [ '000503', '海虹控股' ]
 ]
 * @param callback
 */
exports.getRccomandStockAntiRiskAbility = (callback)=>{
    RTStockDB.getStock('chooseStockAbility',(err,doc)=>{
        callback(err,doc)
    })
}

/**
 * 高温 ，趋同
 * @param callback 形如(err,doc)
 * doc{JSON} 形式
 * 形如：
 * {
 "HighAndSame": ['id'  ,'HighAndSame综合得分' ,'HighAndSame盈利能力' ,'HighAndSame绝对收益' ,'HighAndSame选股能力' ,'HighAndSame抗风险能力' ,'HighAndSame稳定性'],
 "HighAndOpposite": ['id' ,'HighAndOpposite综合得分' ,'HighAndOpposite盈利能力' ,'HighAndOpposite绝对收益' ,'HighAndOpposite选股能力' ,'HighAndOpposite抗风险能力' ,'HighAndOpposite稳定性'],
 "LowAndSame": ['id' ,'LowAndSame综合得分' ,'LowAndSame盈利能力' ,'LowAndSame绝对收益' ,'LowAndSame选股能力' ,'LowAndSame抗风险能力' ,'LowAndSame稳定性'],
 "LowAndOpposite": ['id' ,'LowAndOpposite综合得分' ,'LowAndOpposite盈利能力' ,'LowAndOpposite绝对收益' ,'LowAndOpposite选股能力' ,'LowAndOpposite抗风险能力' ,'LowAndOpposite稳定性'],
 "Normal": ['id' ,'Normal综合得分' ,'Normal盈利能力' ,'Normal绝对收益' ,'Normal选股能力' ,'Normal抗风险能力' ,'Normal稳定性'],
 }
 *
 */
exports.getRtStrategyALL = (callback) =>{

    const mapTable = {
        'LowAndOpposite':'markLO',
        'HighAndOpposite':'markHO',
        'LowAndSame':'markLS',
        'HighAndSame':'markHS',
        'Normal':'markNormal'

    }
    let getBestStrategy = function (env,data) {
        return new Promise((resolve,reject)=>{
            StrategyDB.getBestStrategyByEnv(env,'strategyScore',1,(err,datas)=>{
                let doc = datas[0][mapTable[env]]
                let list = [];
                list.push(datas[0]['_id'])
                list.push(doc['strategyScore']);
                list.push(doc['profitAbility']);
                list.push(doc['absoluteProfit']);
                list.push(doc['chooseStockAbility']);
                list.push(doc['antiRiskAbility']);
                list.push(doc['stability'])
                data[env] = list;
                resolve(list)
            })

        })
    }

    getBestStrategy('HighAndSame',{})
        .then(t=>getBestStrategy('HighAndOpposite',t))
        .then(t=>getBestStrategy('LowAndSame',t))
        .then(t=>getBestStrategy('LowAndOpposite',t))
        .then(t=>getBestStrategy('Normal',t))
        .then(t=>callback(null,t))
        .catch(r=>callback(r,null))
}

/**
 * 根据strategy的ID 获取strategy
 * @param strategyKey
 * @param callback (err,doc)
 * doc形式{JSON}:
 * { _id: 593b594c2dd1ad40f7c85c8b,
  beginDate: 2015-01-01T00:00:00.000Z,
  endDate: 2017-03-17T00:00:00.000Z,
  stockPoolConditionVO:
   { excludeST: false,
     industries: [],
     benches: [],
     stockPool: '沪深300' },
  rank: { r1: [ 'MA', 'asd', 10, 1 ], r2: [ 'MOM', 'desc', 9, 1 ] },
  filter: { f1: [ 'turnOverRate', '<', 1 ] },
  tradeModelVO: { loseRate: null, winRate: null, holdingNums: 2, holdingDays: 2 },
  envSpecDay: 3,
  markNormal:
   { profitAbility: 5,
     stability: 17,
     chooseStockAbility: 9,
     absoluteProfit: 1,
     antiRiskAbility: 14,
     strategyScore: 46 },
  markHS:
   { profitAbility: 7,
     stability: 17,
     chooseStockAbility: 11,
     absoluteProfit: 2,
     antiRiskAbility: 18,
     strategyScore: 55 },
  markHO:
   { profitAbility: 8,
     stability: 17,
     chooseStockAbility: 11,
     absoluteProfit: 2,
     antiRiskAbility: 19,
     strategyScore: 57 },
  markLS:
   { profitAbility: 3,
     stability: 16,
     chooseStockAbility: 9,
     absoluteProfit: 0,
     antiRiskAbility: 16,
     strategyScore: 44 },
  markLO:
   { profitAbility: 7,
     stability: 17,
     chooseStockAbility: 10,
     absoluteProfit: 4,
     antiRiskAbility: 16,
     strategyScore: 54 } }

 */


exports.getStragtegyByID=(strategyKey,callback)=>{
    StrategyDB.getStrategyByID(strategyKey,(err,doc)=>{

        let result = {
            beginDate:doc['beginDate'].toISOString().slice(0,10).replace(/-/g,'/'),
            endDate:doc['endDate'].toISOString().slice(0,10).replace(/-/g,'/'),
            stockPool:doc['stockPoolConditionVO'].stockPool,
            industries: doc['stockPoolConditionVO'].industries,
            benches: doc['stockPoolConditionVO'].benches,
            excludeST:doc['stockPoolConditionVO'].excludeST?"on":"off",
            rank:JSON.stringify(doc['rank']),
            filter:JSON.stringify(doc['filter']),
            reserveDays:doc['tradeModelVO'].holdingDays,
            numberOfStock:doc['tradeModelVO'].holdingNums,
            marketObserve:doc['envSpecDay'],
            dynamic_hold:(doc['tradeModelVO'].loseRate!==null || doc['tradeModelVO'].winRate!==null)?"on":"off",
            maxWinRate: doc['tradeModelVO'].winRate,
            maxLoseRate:doc['tradeModelVO'].loseRate
        }
        //console.log(result)
        callback(err,result);
    })
}