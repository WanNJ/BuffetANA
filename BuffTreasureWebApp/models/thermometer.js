/**
 * Created by wshwbluebird on 2017/5/8.
 */


const mongoose = require('mongoose');

const thermometerSchema = mongoose.Schema({
    "date": Date,
    "limitUp": Number,  //当日涨停股票的个数
    "limitDown": Number,  //当日跌停股票的个数
    "halfLimitUp": Number, //当日涨超过5%股票的个数
    "halfLimitDown": Number, //当日跌超过5%股票的个数
    "temperature": Number,  //市场温度 这里用通信达的计算方法 越高越好
    "lastLimitUp": Number,   //昨日涨停股票今日表现    反应市场的追涨情况
    "lastLimitDown": Number,  //跌停涨停股票今日表现    反应市场的追跌情况
    "lastTurnOver": Number,  //换手率前50只个股赚钱效应为26%
    "moneyEffect": Number,   //总体赚钱效应在一个总体内赚钱效应  上涨家数与涨跌总家数之比
},{
    collection: 'thermometer',
    versionKey: false
});


const Thermometer = mongoose.model('thermometer', thermometerSchema);


exports.thermometerDB = {

    /**
     * 获取日期为date的市场温度信息  没有返回null
     * 数据格式 如thermometerSchema
     *
     * @param date
     * @param callback
     */
    getThermometerByDate: function (date, callback) {
        Thermometer.findOne({date:date}, function (err, docs) {
            callback(err, docs);
        });
    },

    /**
     * 写入一个市场温度计信息
     * TODO如果冲突目前并未加检测
     * 数据格式 如thermometerSchema
     *
     * @param thermometer
     * @param callback
     */
    writeThermometerByDate: function (thermometer, callback){
        if(thermometer!==null) {
            let ThermometerToSave = new Thermometer(thermometer);
            ThermometerToSave.save((err, data) => {
                callback(err, data);
            });
        }
    },

    /**
     * 获得日期区间范围内的  市场温度信息列表（按日期的升序排列）
     * 包含两端
     * @param beginDate   开始日期
     * @param endDate     结束日期
     * @param callback
     */
    getThermometerInRangeDate: function (beginDate, endDate, callback) {
        Thermometer.find({ date : {$gte : beginDate, $lte : endDate}}).sort({date : 'asc'})
                   .exec(function (err, docs) {
                callback(err, docs);
         });
    }


}