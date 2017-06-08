/**
 * Created by slow_time on 2017/6/8.
 */
const stockRTInfo = require('./stockRTInfoSchema');

exports.stockRTInfoDB = {

    /**
     * 更新股票实时信息
     * @param code {String} 股票代码
     * @param rtInfo 股票实时信息，形如：
     * {
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
     * }
     * @param callback
     */
    updateRTInfo: (code, rtInfo, callback) => {
        let conditions = {code: code};
        let update = {$set: rtInfo};
        stockRTInfo.updateOne(conditions, update, (err) => {
            if (err)
                callback(err, false);
            else
                callback(null, true);
        });
    },

    /**
     * 新增实时信息，只会在初始化RTInfo这个Collection时调用，调用一次后，不会再被调用
     * @deprecated
     * @param rtInfo
     * @param callback
     */
    addRTInfo: (rtInfo, callback) => {
        let info = new stockRTInfo(rtInfo);
        info.save((err) => {
            if (err)
                callback(err, false);
            else
                callback(null, true);
        });
    }
};