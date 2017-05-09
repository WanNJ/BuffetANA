/**
 * Created by wshwbluebird on 2017/5/9.
 */

var async = require('async');

let thrmometerDB = require('../../models/thermometer').thermometerDB;
let singleStockDB = require('../../models/singleStock.js').singleStockDB;


/**
 * 获取某一天的市场温度环境
 * 不是实时的  基于历史的
 *
 * 如果 存在直接从数据库中提取
 * 如果不存在去计算
 *
 * @param date   日期
 * @param callback   回调函数 形如 (err, docs) => { }
 */
exports.getDailyEnvironment  = (date , callback) =>{
    thrmometerDB.getThermometerByDate(date,(err,doc)=>{
        if(err){
            callback(err,null);
        }else{

            //如果要查找的市场温度信息不存在!
            if(doc === null){
               

            }else{
                console.log("BLThermometer:  not null")
            }

        }
    });
}
