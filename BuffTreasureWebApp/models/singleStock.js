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

let preRangeCode = null;
let preRangeStockList = null;

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
        let Stock = mongoose.model(date.toISOString().substr(0, 10), stockSchema);
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
    getStockInfoInRangeDate: function (code, beginDate, endDate, callback) {
        // console.log(code)
        if (preRangeCode === code && preRangeStockList !== null) {
            callback(null, preRangeStockList);
            //console.time('hereRead');
        }else{
            // console.time('hereRead');
            let Stock = mongoose.model(code, stockSchema);
            Stock.find({ date : {$gte : beginDate, $lte : endDate}}, {_id: 0, adjClose: 1, date: 1, volume: 1}, function (err, docs) {
                // console.timeEnd('hereRead');
                preRangeStockList = docs;
                preRangeCode = code;
                callback(err, docs.reverse());
            });
            // Stock.find({ date : {$gte : beginDate, $lte : endDate}}).sort({date:'asc'}).exec(function (err, docs) {
            //     console.timeEnd('hereRead');
            //     preRangeStockList = docs;
            //     preRangeCode = code;
            //     callback(err,docs);
            // });

        }
    },

    /**
     * 获得某一支股票某一天的数据
     * @param code
     * @param date
     * @param callback
     */
    getStockInfoByCodeAndDate: function (code, date, callback) {
        let Stock = mongoose.model(code, stockSchema);
        Stock.find({date : date}, (err, docs) => {
            callback(err, docs);
        });
    },


    /**
     * 这个方法
     * 不建议调用
     * TODO  最后删掉
     * @deprecated
     * @param date s
     * @param callback
     */
    getStockInfoByDateTwo: function (date, callback) {

        const oneDay = 24000*3600;
        let promises = [date,new Date(date-oneDay),new Date(date-2*oneDay),new Date(date-3*oneDay),new Date(date-4*oneDay)]
            .map(function (d) {
            let Stock = mongoose.model(d.toISOString().substr(0, 10), stockSchema);
            return Stock.find({}, function (err, docs) {
                return docs;
            });
        });

        Promise.all(promises).then(function (posts) {
            if(posts[0].length === 0 ){
                callback(null,null,null);
            }else{
                let left = posts.slice(1,4).filter((data)=>{return data.length!==0})
                if(left.length === 0 ){
                    callback(null,null,null);
                }else{
                    callback(null,posts[0],left[0]);
                }

            }

        })



    }

};
