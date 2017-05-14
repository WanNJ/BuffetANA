/**
 * Created by slow_time on 2017/5/12.
 * add by wsw
 * 将获取pickle data放在这里了   放在models里 会造成循环依赖
 */
let strategyTool  =require('./strategyTool')

exports.getPickleData = (beginDate, endDate, stockPoolConditionVO, rank, filter, tradeModelVO, envSpecDay, callback) => {
    /**
     *
     * @returns {Promise}
     */
    let getCodeList = function () {
        return new Promise((resolve,reject) =>{
            strategyTool.getChoosedStockList(stockPoolConditionVO),(err,list) =>{
                resolve(list);
            }
        })
    }

    /**
     *
     * @param stockList
     * @returns {Promise}
     */
    let divideDays = function (stockList) {
        return new Promise((resolve,reject) =>{
            strategyTool.divideDaysByThermometer
            (beginDate,endDate,tradeModelVO.holdingDays, envSpecDay,callback),(err,list) =>{
                resolve([stockList,list]);
            }
        })
    }

    /**
     *
     * @param data
     * @returns {Promise}
     */
    let setValue = function (data) {
        return new Promise((resolve,reject) =>{
            strategyTool.setRankAndFilterToPickleDataList
            (data[0],data[1],beginDate,endDate,rank,filter,(err,list) =>{
                resolve([list,list]);
            })
        })
    }

    getCodeList()
        .then(list=>divideDays(list))
        .then(data=>setValue(data))
        .then(allList=>callback(null,allList));
};

