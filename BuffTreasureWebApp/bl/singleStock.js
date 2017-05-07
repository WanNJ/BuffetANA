/**
 * Created by slow_time on 2017/5/6.
 */
let singleStockDB = require('../models/singleStock.js').singleStockDB;

/**
 * 返回日K数据列表，其中每一个元素也是一个数组，形式如下
 *      日期，         开盘价，    收盘价，    最低价，    最高价，    涨跌幅，    成交量，    换手率，    K   D   J
 * eg:  '2017-5-5'    10.2       11.5       10.1       11.5      1.1275      1232       2.23      80  90  70
 * @param code 股票代号
 * @param callback
 */
exports.getDailyData = (code, callback) => {

};

/**
 * 返回周K数据列表，其中每一个元素也是一个数组，形式如下
 *      日期(Monday)， 开盘价，    收盘价，    最低价，    最高价，    涨跌幅，    成交量，    换手率，    K   D   J
 * eg:  '2017-5-8'    10.2       11.5       10.1       11.5      1.1275      1232       2.23      80  90  70
 * @param code 股票代号
 * @param callback
 */
exports.getWeeklyData = (code, callback) => {

};

/**
 * 返回月K数据列表，其中每一个元素也是一个数组，形式如下
 *      日期(1st)， 开盘价，    收盘价，    最低价，    最高价，    涨跌幅，    成交量，    换手率，    K   D   J
 * eg:  '2017-5-1'  10.2       11.5       10.1       11.5      1.1275      1232       2.23      80  90  70
 * @param code 股票代号
 * @param callback
 */
exports.getMonthlyData = (code, callback) => {

};