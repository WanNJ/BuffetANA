/**
 * Created by slow_time on 2017/5/5.
 */
const mongoose = require('mongoose');

const stockSchema = mongoose.Schema({
    code: String,
    name: String,
    industry: String,
    bench: Array
}, {collection: 'allstocks'});
const allStock = mongoose.model('allstocks', stockSchema);
// 储存第一次查询所有股票代码和名称的结果，以便后续调用时直接使用，不用再次查询数据库
let allstocks;
exports.allStockDB = {

    /**
     * 获得所有股票的代码和名称
     * @param callback
     */
    getAllStockCodeAndName: (callback) => {
        if (typeof allstocks === "undefined") {
            allStock.find({}, ['code', 'name'], (err, docs) => {
                if (!err) {
                    allstocks = docs;
                }
                callback(err, docs);
            });
        }
        else {
            callback(null, allstocks);
        }
    },

    /**
     * 获得同行业的所有股票的代码和名称
     * @param industry
     * @param callback
     */
    getStocksByIndustry: (industry, callback) => {
        allStock.find({industry: industry}, ['code', 'name'], (err, docs) => {
            callback(err, docs);
        });
    },

    /**
     * 获得属于某些板块的所有股票的代码和名称
     * @param bench 板块的列表，如果只有一个板块，则是只包含一个元素的列表
     * @param callback
     */
    getStocksByBench: (bench, callback)=> {
        allStock.find({bench : {$all : bench}}, ['code', 'name'], (err, docs) => {
            callback(err, docs);
        });
    }
};