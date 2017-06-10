/**
 * Created by wshwbluebird on 2017/6/9.
 */

let RTStockDB = require('../../models/RTSTock').RTStockDB;


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