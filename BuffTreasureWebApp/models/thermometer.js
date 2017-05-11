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
    getThermometerByDate: function (date, callback) {
        Thermometer.findOne({date:date}, function (err, docs) {
            callback(err, docs);
        });
    },

    writeThermometerByDate: function (thermometer, callback){
        if(thermometer!==null) {
            let ThermometerToSave = new Thermometer(thermometer);
            ThermometerToSave.save((err, data) => {
                callback(err, data);
            });
        }
    }
}