/**
 * Created by slow_time on 2017/6/7.
 */
/**
 * Created by slow_time on 2017/5/5.
 */
const mongoose = require('mongoose');

const Schema = mongoose.Schema;
const marketIndexSchema = new Schema({
    "date": Date,
    "open": Number,
    "high": Number,
    "low": Number,
    "close": Number,
    "volume": Number,
    "code": String,
    "name": String,
    "market": String,
    "beforeClose": Number,
    "changePrice": Number,
    "changeRate": Number,
});
/**
 * 用来保存上一次查询的结果
 */
let preCode = null;
let preStockList = null;

exports.marketIndexDB = {

    /**
     * 获得大盘指数
     * @param code
     * @param callback
     */
    getMarketIndexByCode: (code, callback) => {
        if (preCode === code && preStockList !== null) {
            // 防止上层对此数组进行误操作，将数组的拷贝传给上层
            callback(null, preStockList.slice(0));
        }
        else {
            let Stock = mongoose.model(code, marketIndexSchema);
            Stock.find({}, null, {sort : {date : 'asc'}}, function (err, docs) {
                if (!err) {
                    preCode = code;
                    preStockList = docs;
                }
                callback(err, docs);
            });
        }
    }
};
