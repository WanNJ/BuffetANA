/**
 * Created by wshwbluebird on 2017/6/10.
 */


/**
 * Created by wshwbluebird on 2017/5/28.
 */
let RTStockSchema = require('./RTStockSchema');

exports.RTStockDB ={
    /**
     * 更新实时推荐的股票
     * @param result 要保存的结果
     * @param callback
     */
    upDate :(result,callback)=>{
        RTStockSchema.find((err,data) => {
            result['dummyID'] = 1;
            if(err||data.length===0) {
                let newSchema = new RTStockSchema(result)
                newSchema.save((err, doc) => {
                    callback(err, doc)
                })
            }
            else{
                RTStockSchema.update({dummyID:1},{$set: {"strategyScore": result['strategyScore']
                    ,"chooseStockAbility": result['chooseStockAbility']
                    ,"profitAbility": result['profitAbility']
                    ,"antiRiskAbility": result['antiRiskAbility']}},(err,doc)=>{
                    callback(err,doc)
                })
            }

        });
    },

    /**
     * 获取实时更新的股票
     * @param cap 股票代码 哪一种
     * @param callback
     */
    getStock :(cap,callback)=>{
        RTStockSchema.find((err,data) => {
            callback(err,data[0][cap])
        });
    },




}