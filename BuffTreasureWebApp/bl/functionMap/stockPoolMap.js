/**
 * Created by wshwbluebird on 2017/5/13.
 */

let allStockbl = require('../../models/allstock').allStockDB;

/**
 * 随机500  promise封装
 * @returns {Promise}
 */
let getRandom500 = function () {
    return new Promise(function(resolve, reject){
        allStockbl.getRandom500StockCodeAndName((err,doc)=> {
            if(err){
                reject();
            }else{
                resolve(doc);
            }
        })
    });
}

/**
 * 沪深300  promise封装
 * @returns {Promise}
 */
let getHS300 = function () {
    return new Promise(function(resolve, reject){
        allStockbl.getHS300StockCodeAndName((err,doc)=> {
            if(err){
                reject();
            }else{
                resolve(doc);
            }
        })
    });
}



/**
 * 返回promise类型的注册表
 * 获取的直接就是promise
 * @type {{随机500, 沪深300}}
 */
exports.funtionMap = {
    '随机500' : getRandom500,
    '沪深300' : getHS300,

}


