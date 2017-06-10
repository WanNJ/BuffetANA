/**
 * Created by wshwbluebird on 2017/5/28.
 */
let strategySchema = require('./strategySchema');

const mapTable = {
    'LowAndOpposite':'markLO',
    'HighAndOpposite':'markHO',
    'LowAndSame':'markLS',
    'HighAndSame':'markHS',
    'Normal':'markNormL'

}

exports.strategyDB ={
    saveStrategy :(strategy,callback)=>{
        let newStrategy = new strategySchema(strategy);
        newStrategy.save((err, data) => {
            callback(err, data);
        });
    },

    getRandomStrategy: (callback) => {
        strategySchema.findOne((err, doc) => {
            callback(err, doc)
        });
    },

    getAllStrategy: (callback) => {
        strategySchema.find((err, doc) => {
            callback(err, doc)
        });
    },

    /**
     *
     * @param env  是哪种市场环境 传递原来定义的标准字符串
     * @param cap   选择是那种能力
     * @param num   选择最好的几个策略
     * @param callback
     */
    getBestStrategyByEnv: (env, cap , num ,callback) => {
        let str = mapTable[env]+"."+cap;
        let arg = {};
        arg[str] = 'desc';
        let select = {sort:arg}
        strategySchema.find({},null, select,(err, doc) => {
            callback(err, doc.slice(0,num))
        });
    },
}