/**
 * Created by slow_time on 2017/5/12.
 * add by wsw
 * 将获取pickle data放在这里了   放在models里 会造成循环依赖
 */
let strategyTool  =require('./strategyTool')
let proMap  =require('../functionMap/projectionMap').proMap;

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
    let set = new Set();
    let rankKeys =  Object.keys(rank);
    let filterKeys =  Object.keys(filter);
    for(let i = 0 ; i < rankKeys.length;i++){
        let cont = proMap[rankKeys[i]];
        for(let j = 0 ; j < cont.length; j++)
            set.add(cont[i]);
    }
    for(let i = 0 ; i < filterKeys.length;i++){
        let cont = proMap[filterKeys[i]];
        for(let j = 0 ; j < cont.length; j++)
            set.add(cont[i]);
    }
    set.add('date');
    console.log(set)
    let pro = [...set]
    console.log(pro)


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
    };

    /**
     *
     * @param data
     * @returns {Promise}
     */
    let setValue = function (data) {
        return new Promise((resolve,reject) =>{
            strategyTool.setRankAndFilterToPickleDataList
            (data['code'],data['pickle'],beginDate,endDate,rank,filter,pro,(err,list) =>{
                resolve(list);
            })
        })
    };

    let filterAndRank = function (AllDataList,holdNumber) {
        //console.log(AllDataList['Normal'][0])
        let keys = Object.keys(AllDataList);
        for(let i = 0 ; i < 5 ; i++){
            let pickleDataList =  AllDataList[keys[i]];
            let p = 0 ; //data的指针
            pickleDataList.forEach(pickleData =>{
                pickleData.backDatas = pickleData.backDatas
                    .filter(t=>t.valid && t.filterValue)
                    .sort((a,b)=>b.rankValue-a.rankValue)
                    .slice(0,holdNumber);
            });
            AllDataList[keys[i]] = pickleDataList;
        }

        return AllDataList;
    }


    getCodeList()
        .then(list=>divideDays(list))
        .then(data=>setValue(data))
        .then(data=>filterAndRank(data,tradeModelVO.holdingNums))
        .then(allList=>callback(null,allList));
};

