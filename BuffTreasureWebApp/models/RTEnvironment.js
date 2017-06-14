/**
 * Created by wshwbluebird on 2017/6/10.
 */

let RTEnvironmentSchema = require('./RTEnvironmentSChema');

exports.RTEnvironmentDB ={
    /**
     * 更新实时推荐的股票
     * @param result 要保存的结果
     * @param callback
     "limitUp": limitUp,  //当日涨停股票的个数
     "limitDown": limitDown,  //当日跌停股票的个数
     "halfLimitUp": halfLimitUp, //当日涨超过5%股票的个数
     "halfLimitDown": halfLimitDown, //当日跌超过5%股票的个数
     "temperature": temperature,  //市场温度 这里用通信达的计算方法 越高越好
     "lastLimitUp": lastLimitUp.toFixed(5),   //昨日涨停股票今日表现    反应市场的追涨情况
     "lastLimitDown": lastLimitDown.toFixed(5),  //跌停涨停股票今日表现    反应市场的追跌情况
     "lastTurnOver": lastTurnOver.toFixed(5),  //换手率前50只个股赚钱效应为26%
     "moneyEffect": moneyEffect.toFixed(5),
     */
    upDate :(result,callback)=>{
        RTEnvironmentSchema.find((err,data) => {
            result['dummyID'] = 1;
            if(err||data.length===0) {
                let newSchema = new RTEnvironmentSchema(result)
                newSchema.save((err, doc) => {
                    callback(err, doc)
                })
            }
            else{
                RTEnvironmentSchema.update({dummyID:1},{$set:
                    {"limitUp": result['limitUp']
                    ,"limitDown": result['limitDown']
                    ,"halfLimitUp": result['halfLimitUp']
                    ,"halfLimitDown": result['halfLimitDown']
                    ,"temperature": result['temperature']
                    ,"lastLimitUp": result['lastLimitUp']
                    ,"lastLimitDown": result['lastLimitDown']
                    ,"lastTurnOver": result['lastTurnOver']
                    ,"moneyEffect": result['moneyEffect']}},(err,doc)=>{
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
    getRTEnv :(callback)=>{
        RTEnvironmentSchema.find((err,data) => {
            callback(err,data[0])
        });
    },




}