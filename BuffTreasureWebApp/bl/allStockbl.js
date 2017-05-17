/**
 * Created by slow_time on 2017/5/7.
 */
let allStockDB = require('../models/allStock').allStockDB;

/**
 * 获取所有股票的代码以及名称
 * @param callback 形如 (err, allstocks) => { }
 * allstocks为查询结果，是一个形如 ['平安银行(000001)',...] 的数组
 */
exports.getAllStockCodeAndName = (callback) => {
    allStockDB.getAllStockCodeAndName((err, docs) => {
        if (err) {
            callback(err, null);
        }
        else {
            let allstocks = docs.map(stock => {
                return stock.code + '(' + stock.name + ')';
            });
            callback(err, allstocks);
        }
    });
};

