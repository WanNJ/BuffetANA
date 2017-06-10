/**
 * Created by wshwbluebird on 2017/6/9.
 */

let proMap  =require('../functionMap/projectionMap').proMap;
let strategyTool  =require('./strategyTool')

let filterMap = require('../functionMap/filterMap');
let rankMap = require('../functionMap/rankMap').funtionMap;

let PickleDataVO =require('../../vo/PickleData').PickleData

/**
 * 股票池 rank 和 filter  参数在以前的接口都写过了

 * @param stockPoolConditionVO
 * @param rank
 * @param filter
 * @param callback
 *形如(err,doc)
 * doc:{list}
 [ [ '000627', '天茂集团' ],
 [ '002024', '苏宁云商' ],
 [ '002065', '东华软件' ],
 [ '002153', '石基信息' ],
 [ '000503', '海虹控股' ] ]
 */
exports.getRTPickleData = (stockPoolConditionVO, rank, filter,callback) =>{
    let set = new Set();
    let rankKeys =  Object.keys(rank);
    let filterKeys =  Object.keys(filter);
    for(let i = 0 ; i < rankKeys.length;i++){
        let cont = proMap[rank[rankKeys[i]][0]];
        for(let j = 0 ; j < cont.length; j++)
            set.add(cont[j]);
    }
    for(let i = 0 ; i < filterKeys.length;i++){
        set.add(filter[filterKeys[i]][0]);
    }
    set.add('date');
    let pro = [...set]


    /**
     *
     * @param stockList
     * @returns {Promise}
     */
    let divideDays = function (stockList) {
        //console.log(stockList)
        return new Promise((resolve,reject) =>{
            let list = []
            list.push(new PickleDataVO(new Date("2017-05-01"),Date.now(),[],0));
            let pickle ={
                'predict':list
            }

            let data = {
                'code':stockList,
                'pickle':pickle
            };
            //console.log(data)
            resolve(data)
        })
    };


    let getCodeList = function () {
        return new Promise((resolve,reject) => {
                if(stockPoolConditionVO.benches===null) stockPoolConditionVO.benches = []
                if(stockPoolConditionVO.industries===null) stockPoolConditionVO.industries = []
                strategyTool.getChoosedStockList(stockPoolConditionVO, (err, list) => {
                resolve(list);
            })
        })
    }

    let setValue = function (data) {
        return new Promise((resolve,reject) =>{
            strategyTool.setRankAndFilterToPickleDataList
            (data['code'],data['pickle'],new Date("2017-01-01"),Date.now(),rank,filter,pro,1,(err,list) =>{
                //console.log(list)
                //console.log(list)
                resolve(list);
            })
        })
    };

    let filterAndRank = function (AllDataList) {
        //console.log(AllDataList['Normal'][0])
       // console.log(AllDataList['predict'][0].backDatas)
        let keys = Object.keys(AllDataList);
        for(let i = 0 ; i < keys.length ; i++){
            let pickleDataList =  AllDataList[keys[i]];
            let p = 0 ; //data的指针
            pickleDataList.forEach(pickleData =>{
                pickleData.backDatas = pickleData.backDatas
                    .filter(t=>t.valid && t.filterValue)
                    .sort((a,b)=>b.rankValue-a.rankValue)
                    .slice(0,5);
            });
            AllDataList[keys[i]] = pickleDataList;
        }

        let backs =  AllDataList['predict'][0].backDatas;
        reList = []
        for(let i = 0 ; i < 5 ; i++) {
            let temp = [];
            temp.push(backs[i]['code'])
            temp.push(backs[i]['name'])
            reList.push(temp)
        }
        return reList
    }

    getCodeList()
        .then(list=>divideDays(list))
    //.catch(e => callback(e,null))
        .then(data=>setValue(data))
        //.catch(e=>callback(e,null))
        .then(data=>filterAndRank(data))
        //.catch(e=>callback(e,null))
        .then(allList=>callback(null,allList))
        .catch(e=>callback(e,null))


}