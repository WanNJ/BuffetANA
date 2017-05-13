/**
 * Created by wshwbluebird on 2017/5/13.
 */

let allStockbl = require('../../models/allstock').allStockDB;
//let allStockbl = require('../../modelsStub/allStockStub').allStockDB;
/**
 * 返回获取所有的 板块的股票  的promise对像
 * @param benches  里面的板块所包含的股票不能重复!!!!! TODO   检查 很重要!!!!
 * @param index
 * @param list
 * @returns {Promise}
 */
let getByBenches = function (benches , index , list) {
    return new Promise(function(resolve, reject){
        if(index === benches.length)
            resolve(list);
        else {
            allStockbl.getStocksByBench(benches[index],(err, doc) => {
                if (err) {
                    reject();
                } else {
                    list.push.apply(list,doc);
                    index++;
                    resolve(getByBenches(benches, index, list));
                }

            })
        }
    });
}

/**
 *返回获取所有的 行业的股票  的promise对向
 * @param industries    里面的板块所包含的股票不能重复!!!!! TODO   检查 很重要!!!!
 * @param index
 * @param list
 * @returns {Promise}
 */
let getByIndustries = function (industries , index , list) {
    return new Promise(function(resolve, reject){
        if(index === industries.length)
            resolve(list);
        else {
            allStockbl.getStocksByIndustry(industries[index],(err, doc) => {
                if (err) {
                    reject();
                } else {
                    list.push.apply(list,doc);
                    index++;
                    resolve(getByIndustries(industries, index, list));
                }

            })
        }
    });
}


/**
 * 返回promise类型的注册表
 * 获取的直接就是promise
 * @type {{随机500, 沪深300}}
 */
exports.funtionMap = {
    'industries' : getByIndustries,
    'benches' : getByBenches,
}