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
const Stock = mongoose.model('allstocks', stockSchema);

exports.singleStockDB = {

    /**
     * 获得所有股票的代码和名称
     * @param callback
     */
    getAllStockCodeAndName: (callback) => {
        Stock.find({code: code}, ['code', 'name'], (err, docs) => {
            callback(err, docs);
        });
    },

    /**
     * 获得同行业的所有股票的代码和名称
     * @param industry
     * @param callback
     */
    getStocksByIndustry: (industry, callback) => {
        Stock.find({industry: industry}, ['code', 'name'], (err, docs) => {
            callback(err, docs);
        });
    },

    /**
     * 获得属于某些板块的所有股票的代码和名称
     * @param bench 板块的列表，如果只有一个板块，则是只包含一个元素的列表
     * @param callback
     */
    getStocksByBench: (bench, callback)=> {
        Stock.find({bench : {$all : bench}}, ['code', 'name'], (err, docs) => {
            callback(err, docs);
        });
    }
};