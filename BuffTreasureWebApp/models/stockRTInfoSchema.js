/**
 * Created by slow_time on 2017/6/8.
 */
const mongoose = require('mongoose');

let Schema = mongoose.Schema;

let stockRTInfoSchema = new Schema({
    code: String,                         // 股票代码
    now_price: Number,                    // 现价
    change_price: Number,                 // 涨跌额
    change_rate: Number,                  // 涨跌幅（已经乘了100，单位为"%"）
    yesterday_close: Number,              // 昨收
    today_open: Number,                   // 今开
    high: Number,                         // 最高
    low: Number,                          // 最低
    volume: Number,                       // 成交量（单位为"万手"）
    volume_of_transaction: Number,        // 成交额（单位为"万"）
    marketValue: Number,                  // 总市值（单位为"亿"）
    floatMarketValue: Number,             // 流通市值（单位为"亿"）
    turnOverRate: Number,                 // 换手率（已经乘了100，单位为"%"）
    PB_ratio: Number,                     // 市净率
    amplitude: Number,                    // 振幅（已经乘了100，单位为"%"）
    PE_ratio: Number,                     // 市盈率
}, {collection: 'stockRTInfo',
    versionKey: false});

module.exports = mongoose.model('stockRTInfo', stockRTInfoSchema);