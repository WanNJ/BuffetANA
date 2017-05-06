/**
 * Created by slow_time on 2017/5/5.
 */
const mongoose = require('mongoose');

const stockSchema = mongoose.Schema({
    date: String,
    open: Number,
    high: Number,
    low: Number,
    close: Number,
    volume: Number,
    adjClose: Number,
    code: String,
    name: String,
    market: String
});

exports.singleStockDB = {
    getStockInfoByCode: function (code, callback) {
        let Stock = mongoose.model(code, stockSchema);
        Stock.find({code: code}, null, {sort : {date : 'asc'}}, function (err, docs) {
            callback(err, docs);
        });
    },

    getStockInfoByDate: function (date, callback) {
        Stock.find({date: date}, function (err, docs) {
            callback(err, docs);
        });
    },

    getStockInFoInRangeDate: function (code, beginDate, endDate, callback) {
        Stock.find({code : code, date : {$gte : beginDate, $lte : endDate}}).sort({date : 'asc'}).exec(function (err, docs) {
            callback(err, docs);
        });
    }
}