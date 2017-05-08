/**
 * Created by slow_time on 2017/5/5.
 */
const mongoose = require('mongoose');

const stockSchema = mongoose.Schema({
    "date": Date,
    "open": Number,
    "high": Number,
    "low": Number,
    "close": Number,
    "volume": Number,
    "adjClose": Number,
    "code": String,
    "name": String,
    "market": String,
    "beforeClose": Number,
    "changePrice": Number,
    "changeRate": Number,
    "turnOverRate": Number,
    "marketValue": Number,
    "floatMarketValue": Number
});
/**
 * 用来保存上一次查询的结果
 */
let preCode = null;
let preStockList = null;

exports.singleStockDB = {
    /**
     * 获得具体某一支股票的每一天的数据（按日期的升序排列）
     * @param code
     * @param callback
     */
    getStockInfoByCode: function (code, callback) {
        if (preCode === code && preStockList !== null) {
            // 防止上层对此数组进行误操作，将数组的拷贝传给上层
            callback(null, preStockList.slice(0));
        }
        else {
            let Stock = mongoose.model(code, stockSchema);
            Stock.find({}, null, {sort : {date : 'asc'}}, function (err, docs) {
                if (!err) {
                    preCode = code;
                    preStockList = docs;
                }
                callback(err, docs);
            });
        }
    },

    /**
     * 获得具体某一天的所有的股票的数据
     * @param date 日期必须大于1990年12月19号，小于昨天的日期
     * @param callback
     */
    getStockInfoByDate: function (date, callback) {
        let Stock = mongoose.model(date.toISOString.substr(0, 10), stockSchema);
        Stock.find({}, function (err, docs) {
            callback(err, docs);
        });
    },

    /**
     * 获得日期区间范围内的  股票信息列表（按日期的升序排列）
     * 包含两端
     * @param code
     * @param beginDate
     * @param endDate
     * @param callback
     */
    getStockInFoInRangeDate: function (code, beginDate, endDate, callback) {
        if (preCode === code && preStockList !== null) {
            callback(null, preStockList.filter((stock) => {
                return (stock.date - beginDate >= 0 && stock.date - endDate <= 0);
            }));
        }
        else {
            let Stock = mongoose.model(code, stockSchema);
            Stock.find({code : code, date : {$gte : beginDate, $lte : endDate}}).sort({date : 'asc'}).exec(function (err, docs) {
                callback(err, docs);
            });
        }
    }
}