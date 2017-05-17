/**
 * Created by wshwbluebird on 2017/5/13.
 */

 //let allStockbl = require('../../models/allstock').allStockDB;
 let allStockbl = require('../../modelsStub/allStockStub').allStockDB;

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
 * 沪深A股  promise封装
 * @returns {Promise}
 */
let getHSA = function () {
    return new Promise(function(resolve, reject){
        allStockbl.getHSAStockCodeAndName((err,doc)=> {
            if(err){
                reject();
            }else{
               // console.log('here')
                resolve(doc);
            }
        })
    });
};

/**
 * 创业板  promise封装
 * @returns {Promise}
 */
let getGEM = function () {
    return new Promise(function(resolve, reject){
        allStockbl.getGEMBoardStockCodeAndName((err,doc)=> {
            if(err){
                reject();
            }else{
                resolve(doc);
            }
        })
    });
};


/**
 * 中小板  promise封装
 * @returns {Promise}
 */
let getSME = function () {
    return new Promise(function(resolve, reject){
        allStockbl.getSMEBoardCodeAndName((err,doc)=> {
            if(err){
                reject();
            }else{
                resolve(doc);
            }
        })
    });
};

/**
 * 自定义  promise封装
 * @returns {Promise}
 */
let getDIY = function (industries, boards) {
    return new Promise(function(resolve, reject){
        allStockbl.getDIYStockPool(industries, boards,(err,doc)=> {
            if(err){
                reject();
            }else{
                resolve(doc);
            }
        })
    });
};


/**
 * 返回promise类型的注册表
 * 获取的直接就是promise
 * @type {{随机500, 沪深300}}
 */
exports.funtionMap = {
    '随机500' : getRandom500,
    '沪深300' : getHS300,
    '中小板'  : getSME,
    '沪深A股' : getHSA,
    '创业板'  : getGEM,
    '自选股票池': getDIY

}


