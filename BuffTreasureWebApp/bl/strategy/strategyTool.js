/**
 * Created by wshwbluebird on 2017/5/13.
 * 一些策略需要用到的方法 暂时先写在这里了
 * 1 获取股票池
 * 2 分割日期
 * 3 参数注入
 * 4 参数过滤
 * 5 排序对比
 *
 */



let thermometerbl = require('../thermometer/thermometerbl');
let PickleDataVO = require('../../vo/PickleData').PickleData;
let StockPoolConditionVO = require('../../vo/StockPoolConditionVO').StockPoolConditionVO;
let stockPoolMap = require('../functionMap/stockPoolMap').funtionMap;
let benchAndIndustryMap = require('../functionMap/benchAndIndustryMap').funtionMap;
let singleStockDB  = require('../../models/singleStock').singleStockDB;
let BackDataVO  =require('../../vo/BackData').BackData;

//TODO 以后必须改  效率极差的去重排序算法  我自己都受不了!!!!!!!!!
/**
 * 合并两个数组的方法
 * @param arr1
 * @param arr2
 * @returns {*}
 */
let mergeArray = function(arr1, arr2){
    for (let i = 0 ; i < arr1.length ; i ++ ){
        for(let j = 0 ; j < arr2.length ; j ++ ){
            if (arr1[i][0] === arr2[j][0]){
                arr1.splice(i,1); //利用splice函数删除元素，从第i个位置，截取长度为1的元素
            }
        }
    }
    //alert(arr1.length)
    for(let i = 0; i <arr2.length; i++){
        arr1.push(arr2[i]);
    }
    return arr1;
}

/**
 * 不知道是不是有用写了
 * @param arr [code , name】  数组  去掉ST 真的有必要吗??????
 * @param isExclude  是否排除ST股，排除为true，不排除为false
 */
let excludeST = function (arr ,  isExclude){
    return   !isExclude? arr: arr.filter(t => !(t[1].substring(0,3)==='*ST'));
};



exports.getChoosedStockList = (stockPoolConditionVO , callback) =>{
    if( (stockPoolConditionVO instanceof StockPoolConditionVO) === false){
        callback(new Error('getChoosedStockList: 参数1  没有实现StockPoolConditionVO'),null);
    }else{
       // console.log(stockPoolConditionVO.stockPool)
        let promise = stockPoolMap[stockPoolConditionVO.stockPool];
        if (typeof promise === 'undefined') {
            callback(new Error('getChoosedStockList: 传入错误的stockPoolConditionVO.stockPool', null));
        }
        promise(stockPoolConditionVO.industries ,stockPoolConditionVO.benches)
            .then(list => excludeST(list ,stockPoolConditionVO.excludeST))
            .then(list => callback(null, list));

    }

};


/**
 *
 * @param beginDate
 * @param endDate
 * @param holdPeriod
 * @param envSpecDay
 * @param callback   形如 (err, docs)=>{}
 * docs  格式如下
 *    {
 *       'Normal' : normalPickleList,   //正常情况下的 分割日期方法
         'HighAndOpposite':HighAndOppositePickleList,
         'HighAndSame':HighAndSamePickleList,
         'LowAndSame':LowAndSamePickleList,
         'LowAndOpposite':LowAndOppositePickleList
      }
 *
 *    PickleList 中为 PickleData  的集合 具体形式参加VO/PickleDAta
 *    每个PickleData均包涵 一段购买时间的开始日期和结束日期 空的backDataList 和 0
 *    即  (beginDate, endDate , [] , 0)
 *    例如 (2016-06-01, 2016-06-10 , [] , 0)
 *
 */
exports.divideDaysByThermometer = (beginDate, endDate, holdingPeriod, envSpecDay,callback) =>{
        thermometerbl.getEachDayEnvironmentByFormation(beginDate,endDate,envSpecDay,(err,doc) =>{
            /**
             * 要返回的五种pickleList 集合
             * @type {Array}
             */
            let normalPickleList = [];
            let HighAndSamePickleList = [];
            let HighAndOppositePickleList = [];
            let LowAndSamePickleList = [];
            let LowAndOppositePickleList = [];

            /**
             * 要在循环中应用的五个指针
             * @type {number}
             */
            let normalIndex = 0;
            let HighAndSameIndex = 0;
            let HighAndOppositeIndex = 0;
            let LowAndSameIndex = 0;
            let LowAndOppositeIndex = 0;

            /**
             * 要在循环中存储的五个 开始日期
             * @type {Date}
             */
            let normalBegin;
            let HighAndSameBegin;
            let HighAndOppositeBegin;
            let LowAndSameBegin;
            let LowAndOppositeBegin;

            /**
             * forEach 顺序执行 给出分割日期的方法
             */
            doc.forEach(dd=>{
                //console.log(dd['environment']);

                /**
                 *  计算normal 的分割
                 */
                if(normalIndex === 0){
                    normalBegin = dd['date'];
                    normalIndex++;
                }else if(normalIndex === holdingPeriod){
                    normalPickleList.push(new PickleDataVO(normalBegin,dd['date'],[],0));
                    normalIndex = 0;
                }else{
                    normalIndex++;
                }

                /**
                 *  计算HighAndOpposite 的分割
                 */
                if(HighAndOppositeIndex === 0){
                    if(dd['environment'] === 'HighAndOpposite') {
                        HighAndOppositeBegin = dd['date'];
                        HighAndOppositeIndex++;
                    }
                }else if(HighAndOppositeIndex === holdingPeriod){
                    HighAndOppositePickleList.push(new PickleDataVO(HighAndOppositeBegin,dd['date'],[],0));
                    HighAndOppositeIndex = 0;
                }else{
                    HighAndOppositeIndex++;
                }

                /**
                 *  计算HighAndSame 的分割
                 */
                if(HighAndSameIndex === 0){
                    if(dd['environment'] === 'HighAndSame') {
                        HighAndSameBegin = dd['date'];
                        HighAndSameIndex++;
                    }
                }else if(HighAndSameIndex === holdingPeriod){
                    HighAndSamePickleList.push(new PickleDataVO(HighAndSameBegin,dd['date'],[],0));
                    HighAndSameIndex = 0;
                }else{
                    HighAndSameIndex++;
                }

                /**
                 *  计算LowAndSame 的分割
                 */
                if(LowAndSameIndex === 0){
                    if(dd['environment'] === 'LowAndSame') {
                        LowAndSameBegin = dd['date'];
                        LowAndSameIndex++;
                    }
                }else if(LowAndSameIndex === holdingPeriod){
                    LowAndSamePickleList.push(new PickleDataVO(LowAndSameBegin,dd['date'],[],0));
                    LowAndSameIndex = 0;
                }else{
                    LowAndSameIndex++;
                }

                /**
                 *  计算LowAndOpposite 的分割
                 */
                if(LowAndOppositeIndex === 0){
                    if(dd['environment'] === 'LowAndOpposite') {
                        LowAndOppositeBegin = dd['date'];
                        LowAndOppositeIndex++;
                    }
                }else if(LowAndOppositeIndex === holdingPeriod){
                    LowAndOppositePickleList.push(new PickleDataVO(LowAndOppositeBegin,dd['date'],[],0));
                    LowAndOppositeIndex = 0;
                }else{
                    LowAndOppositeIndex++;
                }




            });
            let ReturnValue = {
                'Normal' : normalPickleList,
                'HighAndOpposite':HighAndOppositePickleList,
                'HighAndSame':HighAndSamePickleList,
                'LowAndSame':LowAndSamePickleList,
                'LowAndOpposite':LowAndOppositePickleList
            };
            callback(null,ReturnValue);

        });
};




let filterMap = require('../functionMap/filterMap').funtionMap;
let rankMap = require('../functionMap/rankMap').funtionMap;


/**
 * 将参数按照要求注入到 AllPickleDataList  中去
 * @param codeList
 * @param AllPickleDataList
 * @param beginDate
 * @param endDate
 * @param rank
 * @param filter
 * @param callback
 */
exports.setRankAndFilterToPickleDataList = (codeList,  AllPickleDataList,
                                            beginDate, endDate, rank, filter,
                                            callback ) =>{

    /**
     *
     * @param code
     * @param codeIndex
     * @param rank
     * @param index
     * @param AllPickleDataList
     * @param beginDate
     * @param endDate
     * @returns {Promise}
     */
    let setRankPromise = function (code ,codeIndex ,rank , index , AllPickleDataList ,beginDate ,endDate ) {
        //console.log(rank)
        return new Promise((resolve, reject) =>{
            let keys = Object.keys(rank);
            if(index === keys.length) {
                resolve(AllPickleDataList);
            }else{
                //console.log(keys)
                //console.log('length:' +keys.length)
                resolve(rankMap[keys[index]]
                    (code ,codeIndex , rank[keys[index]][0],rank[keys[index]][2],
                    rank[keys[index]][1], AllPickleDataList,beginDate ,endDate)
                    .then(setRankPromise(code,codeIndex,rank,index+1,AllPickleDataList)));
            }
        });

    }


    /**
     *
     * @param codeIndex
     * @param filter
     * @param index
     * @param AllPickleDataList
     * @returns {Promise}
     */
    let setFilterPromise = function (codeIndex ,filter , index , AllPickleDataList) {
        return new Promise((resolve, reject) => {
            let keys = Object.keys(filter);
            if (index === keys.length) {
                resolve(AllPickleDataList);
            } else {
                let promise = filterMap[keys[index]]
                    (codeIndex, index , AllPickleDataList)
                    .then(setFilterPromise(codeIndex, rank, index + 1, AllPickleDataList));
                resolve(promise);
            }
        });
    };


    /**
     *@param code
     * @param codeAndName [code,name]
     * @param AllDataList
     * @param beginDate
     * @param endDate
     * @returns {Promise}
     */
    let setCodeAndName = function (codeAndName , AllDataList , beginDate ,endDate) {
        return new Promise((resolve,reject)=>{
            setCodeAndNameToPickle(codeAndName , AllDataList , beginDate ,endDate, (err,data)=>{
                resolve(data);
            });
        })
    }


    /**
     * 全部操作的promise
     * @param codeList
     * @param codeIndex
     * @param listAll
     * @returns {Promise}
     */
    let operatePromise = function (codeList ,codeIndex , listAll){
        // console.log('start' + new Date())
        //console.log(codeList)
        // console.log('here')
        //console.log(codeList[codeIndex])
        return new Promise((resolve,reject)=>{
            if(codeIndex === codeList.length)
                resolve (listAll);
            else {
                // console.time('set name' + codeList[codeIndex])
                resolve(setCodeAndName(codeList[codeIndex], listAll, beginDate, endDate)
                    .then(list => {
                        // console.timeEnd('set name' + codeList[codeIndex])
                        // console.time('set rank'+codeList[codeIndex])
                        return setRankPromise(codeList[codeIndex]['code'], codeIndex, rank, 0, list, beginDate, endDate)
                    })
                    .then(list => {
                        // console.timeEnd('set rank' + codeList[codeIndex]);
                        //console.time(codeList[codeIndex])
                        return setFilterPromise(codeIndex, filter, 0, list)
                    })
                    .then(list => operatePromise(codeList, codeIndex + 1, list)));
            }


        })


    };

    operatePromise(codeList,0,AllPickleDataList).then(list =>callback(null,list));

};



/**
 * 向5个pickledata中注入 股票的名字 代码  以及每个阶段的开盘价和收盘价
 * @param codeAndName
 * @param AllDataList
 * @param beginDate
 * @param endDate
 * @param callback (err,docs)={}
 */
function setCodeAndNameToPickle(codeAndName , AllDataList , beginDate ,endDate , callback){
    console.time('set Name');
    singleStockDB.getStockInfoInRangeDate(codeAndName['code'],new Date(beginDate- 600*24000*3600),new Date(endDate), (err,data)=>{
        let keys = Object.keys(AllDataList);
        for(let i = 0 ; i < 5 ; i++){
            let pickleDataList =  AllDataList[keys[i]];
            let p = 0 ; //data的指针
            pickleDataList.forEach(pickleData =>{
                //console.log(p)
                //console.log(data[p])
                if(typeof data[p] !=='undefined') {
                    let valid = true;
                    while (typeof data[p] !== 'undefined' && data[p]['date'] - pickleData.beginDate < 0 ) {
                        p++;
                    }
                    let begin = 0;

                    if (typeof data[p] !== 'undefined' && pickleData.beginDate - data[p]['date'] === 0) {
                        begin = data[p]['adjClose']; //最后一天收盘价格
                    }
                    else
                        valid = false;
                    while ( typeof data[p] !== 'undefined' && data[p]['date'] - pickleData.endDate < 0 ) {
                        p++;
                        //console.log(p)

                    }
                    let end = 0;
                    if (typeof data[p] !== 'undefined' && pickleData.endDate - data[p]['date']=== 0 )
                        end = data[p]['adjClose']; //最后一天收盘价格
                    else
                        valid = false;
                    pickleData.backDatas.push
                    (new BackDataVO(codeAndName['code'], codeAndName['name'], 0, true,begin, end, valid));
                }else {
                    pickleData.backDatas.push
                    (new BackDataVO(codeAndName['code'], codeAndName['name'], 0, true ,0, 0, false));
                }

            });
            AllDataList[keys[i]] = pickleDataList;
        }
        console.timeEnd('set Name');
        callback(err,AllDataList);
    })
}




