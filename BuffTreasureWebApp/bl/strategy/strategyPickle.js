/**
 * Created by slow_time on 2017/5/12.
 * add by wsw
 * 将获取pickle data放在这里了   放在models里 会造成循环依赖
 */
let strategyTool  =require('./strategyTool')

/**
 * 直接返回 5 个 pickleDataList
 * @param beginDate
 * @param endDate
 * @param stockPoolConditionVO
 * @param rank
 * @param filter
 * @param tradeModelVO
 * @param envSpecDay
 * @param callback
 */
exports.getPickleData = (beginDate, endDate, stockPoolConditionVO, rank, filter, tradeModelVO, envSpecDay, callback) => {
    /**
     *
     * @returns {Promise}
     */
    let getCodeList = function () {
        return new Promise((resolve,reject) => {
            strategyTool.getChoosedStockList(stockPoolConditionVO, (err, list) => {
                //console.log(list)
                resolve(list);
            })
        })
    }

    /**
     *
     * @param stockList
     * @returns {Promise}
     */
    let divideDays = function (stockList) {
        //console.log(stockList)
        return new Promise((resolve,reject) =>{
            // console.time('divide');
            strategyTool.divideDaysByThermometer
            (beginDate,endDate,tradeModelVO.holdingDays, envSpecDay,(err,list) =>{
                //console.log(list)
                // console.timeEnd('divide');
                let data = {
                    'code':stockList,
                    'pickle':list
                };
                resolve(data);
            })
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
            (data['code'],data['pickle'],beginDate,endDate,rank,filter,(err,list) =>{
                resolve(list);
            })
        })
    }

    getCodeList()
        .then(list=>divideDays(list))
        .then(data=>setValue(data))
        .then(allList=>callback(null,allList));
};

