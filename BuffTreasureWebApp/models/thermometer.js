/**
 * Created by wshwbluebird on 2017/5/8.
 */

const Thermometer = require('./thermometerSchema')


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
     * 获得日期区间范围内的  市场温度信息列表（按日期的降序排列）
     * ATTENTION 注意!!!!!!!    大的日期在前面  小的日期在后面
     * 包含两端
     * @param beginDate   开始日期
     * @param endDate     结束日期
     * @param callback
     */
    getThermometerInRangeDate: function (beginDate, endDate, callback) {
        Thermometer.find({ date : {$gte : beginDate, $lte : endDate}})
           .sort({date : 'desc'})
                   .exec(function (err, docs) {
                callback(err, docs);
         });
    }


}