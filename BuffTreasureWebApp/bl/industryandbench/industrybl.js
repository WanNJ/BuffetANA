/**
 * Created by slow_time on 2017/5/8.
 */
let allStockDB = require('../../models/allStock').allStockDB;

/**
 * 根据所给的股票代号获得这支股票所在的行业
 * ！！！一支股票只有一个行业！！！！
 * @param code
 * @param callback
 */
exports.getIndustryByCode = (code, callback) => {
    allStockDB.getIndustryByCode(code, (err, doc) => {
        if (err) {
            callback(err, null);
        }
        else {
            callback(null, doc["industry"]);
        }
    });
};

/**
 * 根据所给的股票代号获得这支股票所在的板块
 * ！！！一支股票可能从属于多个板块，所以所属板块是一个数组！！！！
 * @param code
 * @param callback
 */
exports.getBoardsByCode = (code, callback) => {
    allStockDB.getBoardsByCode(code, (err, doc) => {
        if (err) {
            callback(err, null);
        }
        else {
            callback(null, doc['bench']);
        }
    });
};

/**
 * 获得所有的行业
 *
 * ！！！！返回的是一个所有行业名的Set！！！！
 *
 * @param callback
 */
exports.getAllIndustries = (callback) => {
    allStockDB.getAllIndustry((err, docs) => {
        if (err) {
            callback(err, null);
        }
        else {
            let all_industry = new Set();
            docs.forEach(doc => {
                all_industry.add(doc["industry"]);
            });
            all_industry.delete('--');
            callback(null, all_industry);
        }
    });
};

/**
 * 获得所有板块
 *
 * ！！！！返回的是一个所有板块名的Set！！！！
 *
 * @param callback
 */
exports.getAllBoards = (callback) => {
    allStockDB.getAllBoards((err, docs) => {
        if (err) {
            callback(err, null);
        }
        else {
            let all_bench = new Set();
            docs.forEach(doc => {
                doc["bench"].forEach(bench => {
                    all_bench.add(bench);
                });
            });
            all_bench.delete('--');
            callback(null, all_bench);
        }
    });
};