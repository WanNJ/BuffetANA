/**
 * Created by wshwbluebird on 2017/5/14.
 */

let strategyParam = require('../strategy/strategyParameter');


/**
 * 注入MA的Promise方法
 * TODO  没有检验  reject (如果有异常 还没写)
 * @param code          股票代码
 * @param codeIndex     股票代码在数组中的位置
 * @param desc           是否按照降序排列 true 为降序 排列(也就是从大到小)
 * @param weight          所占权重
 * @param formationPeriod  观察期
 * @param AllDataList   需要更新的五个pickleDataList
 * @param beginDate     回测的开始日期
 * @param endDate       结束如期
 * resolve 的值      为更新后的 AllDataList
 */
function setMA(code , codeIndex, desc , weight ,formationPeriod , AllDataList ,beginDate , endDate) {
    return new Promise((resolve,reject) => {
        strategyParam.calculateMAValue(code ,beginDate , endDate, formationPeriod , (err,maList) =>{

            let keys = Object.keys(AllDataList);
            for(let i = 0 ; i < 5 ; i++){
                let pickleDataList =  AllDataList[keys[i]];
                let p = 0 ; //data的指针
                pickleDataList.forEach(pickleData =>{
                    let backData = pickleData.backDatas[codeIndex];
                    if(backData.valid===true && typeof maList[p] !== 'undefined') {
                        while (typeof maList[p + 1] !== 'undefined' && maList[p + 1]['date'] - pickleData.beginDate < 0) p++;
                        let temp = maList[p]['value'];
                        //判断是否降序排泄
                        if(!desc)  temp = 1- temp;
                        backData.rankValue+= temp * weight;
                    }else{
                       // console.log('here')
                        backData.rankValue+= 0;
                    }
                    pickleData.backDatas[codeIndex] = backData;
                });
                AllDataList[keys[i]] = pickleDataList;
            }
            resolve(AllDataList);
        });
    });

}


/**
 * 注入MOM的Promise方法
 * @param code
 * @param codeIndex
 * @param rankIndex
 * @param formationPeriod
 * @param AllDataList
 * @param beginDate
 * @param endDate
 */
function setMOM(code , codeIndex, rankIndex ,formationPeriod , AllDataList ,beginDate , endDate) {
    return new Promise((resolve,reject) => {
        strategyParam.calculateMOMValue(code ,beginDate , endDate, formationPeriod , (err,maList) =>{
            let keys = Object.keys(AllDataList);
            for(let i = 0 ; i < 5 ; i++){
                let pickleDataList = AllDataList[keys[i]];
                let p = 0 ; //data的指针
                pickleDataList.forEach(pickleData =>{
                    let backData = pickleData.backDatas[codeIndex];
                    if(backData.valid === true && typeof maList[p] !== 'undefined') {
                        while (typeof maList[p + 1] !== 'undefined' && maList[p + 1]['date'] - pickleData.beginDate < 0) p++;
                        backData.mixRank.push(maList[p]['value']);
                    }else{
                        backData.mixRank.push(0);
                    }
                    pickleData.backDatas[rankIndex] = backData;
                });
                AllDataList[keys[i]] = pickleDataList;
            }
            resolve(AllDataList);
        });
    });
}


function setRSI(code , codeIndex, rankIndex ,formationPeriod , AllDataList ,beginDate , endDate) {
    return new Promise((resolve,reject) => {
        strategyParam.calculateRSIValue(code ,beginDate , endDate, formationPeriod , (err,maList) =>{
            let keys = Object.keys(AllDataList);
            for(let i = 0 ; i < 5 ; i++){
                let pickleDataList =  AllDataList[keys[i]];
                let p = 0 ; //data的指针
                pickleDataList.forEach(pickleData =>{
                    let backData = pickleData.backDatas[codeIndex];
                    if(backData.valid===true && typeof maList[p] !== 'undefined') {
                        while (typeof maList[p + 1] !== 'undefined' && maList[p + 1]['date'] - pickleData.beginDate < 0) p++;
                        backData.mixRank.push(maList[p]['value']);
                    }else{
                        // console.log('here')
                        backData.mixRank.push(0);
                    }
                    pickleData.backDatas[rankIndex] = backData;
                });
                AllDataList[keys[i]] = pickleDataList;
            }
            resolve(AllDataList);
        });
    });
}


function setMACD_DIF(code , codeIndex, rankIndex ,formationPeriod , AllDataList ,beginDate , endDate) {
    return new Promise((resolve,reject) => {
        strategyParam.calculateMACD_DIFValue(code ,beginDate , endDate, formationPeriod , (err,maList) =>{
            let keys = Object.keys(AllDataList);
            for(let i = 0 ; i < 5 ; i++){
                let pickleDataList =  AllDataList[keys[i]];
                let p = 0 ; //data的指针
                pickleDataList.forEach(pickleData =>{
                    let backData = pickleData.backDatas[codeIndex];
                    if(backData.valid===true && typeof maList[p] !== 'undefined') {
                        while (typeof maList[p + 1] !== 'undefined' && maList[p + 1]['date'] - pickleData.beginDate < 0) p++;
                        backData.mixRank.push(maList[p]['value']);
                    }else{
                        // console.log('here')
                        backData.mixRank.push(0);
                    }
                    pickleData.backDatas[rankIndex] = backData;
                });
                AllDataList[keys[i]] = pickleDataList;
            }
            resolve(AllDataList);
        });
    });
}


function setMACD_DEA(code , codeIndex, rankIndex ,formationPeriod , AllDataList ,beginDate , endDate) {
    return new Promise((resolve,reject) => {
        strategyParam.calculateMACD_DEAValue(code ,beginDate , endDate, formationPeriod , (err,maList) =>{
            let keys = Object.keys(AllDataList);
            for(let i = 0 ; i < 5 ; i++){
                let pickleDataList =  AllDataList[keys[i]];
                let p = 0 ; //data的指针
                pickleDataList.forEach(pickleData =>{
                    let backData = pickleData.backDatas[codeIndex];
                    if(backData.valid===true && typeof maList[p] !== 'undefined') {
                        while (typeof maList[p + 1] !== 'undefined' && maList[p + 1]['date'] - pickleData.beginDate < 0) p++;
                        backData.mixRank.push(maList[p]['value']);
                    }else{
                        // console.log('here')
                        backData.mixRank.push(0);
                    }
                    pickleData.backDatas[rankIndex] = backData;
                });
                AllDataList[keys[i]] = pickleDataList;
            }
            resolve(AllDataList);
        });
    });
}


function setMACD(code , codeIndex, rankIndex ,formationPeriod , AllDataList ,beginDate , endDate) {
    return new Promise((resolve,reject) => {
        strategyParam.calculateMACDValue(code ,beginDate , endDate, formationPeriod , (err,maList) =>{
            let keys = Object.keys(AllDataList);
            for(let i = 0 ; i < 5 ; i++){
                let pickleDataList =  AllDataList[keys[i]];
                let p = 0 ; //data的指针
                pickleDataList.forEach(pickleData =>{
                    let backData = pickleData.backDatas[codeIndex];
                    if(backData.valid===true && typeof maList[p] !== 'undefined') {
                        while (typeof maList[p + 1] !== 'undefined' && maList[p + 1]['date'] - pickleData.beginDate < 0) p++;
                        backData.mixRank.push(maList[p]['value']);
                    }else{
                        // console.log('here')
                        backData.mixRank.push(0);
                    }
                    pickleData.backDatas[rankIndex] = backData;
                });
                AllDataList[keys[i]] = pickleDataList;
            }
            resolve(AllDataList);
        });
    });
}


function setRSV(code , codeIndex, rankIndex ,formationPeriod , AllDataList ,beginDate , endDate) {
    return new Promise((resolve,reject) => {
        strategyParam.calculateRSVValue(code ,beginDate , endDate, formationPeriod , (err,maList) =>{
            let keys = Object.keys(AllDataList);
            for(let i = 0 ; i < 5 ; i++){
                let pickleDataList =  AllDataList[keys[i]];
                let p = 0 ; //data的指针
                pickleDataList.forEach(pickleData =>{
                    let backData = pickleData.backDatas[codeIndex];
                    if(backData.valid===true && typeof maList[p] !== 'undefined') {
                        while (typeof maList[p + 1] !== 'undefined' && maList[p + 1]['date'] - pickleData.beginDate < 0) p++;
                        backData.mixRank.push(maList[p]['value']);
                    }else{
                        // console.log('here')
                        backData.mixRank.push(0);
                    }
                    pickleData.backDatas[rankIndex] = backData;
                });
                AllDataList[keys[i]] = pickleDataList;
            }
            resolve(AllDataList);
        });
    });
}


function setKDJ_K(code , codeIndex, rankIndex ,formationPeriod , AllDataList ,beginDate , endDate) {
    return new Promise((resolve,reject) => {
        strategyParam.calculateKDJ_KValue(code ,beginDate , endDate, formationPeriod , (err,maList) =>{
            let keys = Object.keys(AllDataList);
            for(let i = 0 ; i < 5 ; i++){
                let pickleDataList =  AllDataList[keys[i]];
                let p = 0 ; //data的指针
                pickleDataList.forEach(pickleData =>{
                    let backData = pickleData.backDatas[codeIndex];
                    if(backData.valid===true && typeof maList[p] !== 'undefined') {
                        while (typeof maList[p + 1] !== 'undefined' && maList[p + 1]['date'] - pickleData.beginDate < 0) p++;
                        backData.mixRank.push(maList[p]['value']);
                    }else{
                        // console.log('here')
                        backData.mixRank.push(0);
                    }
                    pickleData.backDatas[rankIndex] = backData;
                });
                AllDataList[keys[i]] = pickleDataList;
            }
            resolve(AllDataList);
        });
    });
}


function setKDJ_D(code , codeIndex, rankIndex ,formationPeriod , AllDataList ,beginDate , endDate) {
    return new Promise((resolve,reject) => {
        strategyParam.calculateKDJ_DValue(code ,beginDate , endDate, formationPeriod , (err,maList) =>{
            let keys = Object.keys(AllDataList);
            for(let i = 0 ; i < 5 ; i++){
                let pickleDataList =  AllDataList[keys[i]];
                let p = 0 ; //data的指针
                pickleDataList.forEach(pickleData =>{
                    let backData = pickleData.backDatas[codeIndex];
                    if(backData.valid===true && typeof maList[p] !== 'undefined') {
                        while (typeof maList[p + 1] !== 'undefined' && maList[p + 1]['date'] - pickleData.beginDate < 0) p++;
                        backData.mixRank.push(maList[p]['value']);
                    }else{
                        // console.log('here')
                        backData.mixRank.push(0);
                    }
                    pickleData.backDatas[rankIndex] = backData;
                });
                AllDataList[keys[i]] = pickleDataList;
            }
            resolve(AllDataList);
        });
    });
}


function setKDJ_J(code , codeIndex, rankIndex ,formationPeriod , AllDataList ,beginDate , endDate) {
    return new Promise((resolve,reject) => {
        strategyParam.calculateKDJ_JValue(code ,beginDate , endDate, formationPeriod , (err,maList) =>{
            let keys = Object.keys(AllDataList);
            for(let i = 0 ; i < 5 ; i++){
                let pickleDataList =  AllDataList[keys[i]];
                let p = 0 ; //data的指针
                pickleDataList.forEach(pickleData =>{
                    let backData = pickleData.backDatas[codeIndex];
                    if(backData.valid===true && typeof maList[p] !== 'undefined') {
                        while (typeof maList[p + 1] !== 'undefined' && maList[p + 1]['date'] - pickleData.beginDate < 0) p++;
                        backData.mixRank.push(maList[p]['value']);
                    }else{
                        // console.log('here')
                        backData.mixRank.push(0);
                    }
                    pickleData.backDatas[rankIndex] = backData;
                });
                AllDataList[keys[i]] = pickleDataList;
            }
            resolve(AllDataList);
        });
    });
}

exports.funtionMap = {
    "MA" :setMA,
    "MOM" : setMOM,
    "RSI" : setRSI,
    "MACD_DIF" : setMACD_DIF,
    "MACD_DEA" : setMACD_DEA,
    "MACD" : setMACD,
    "RSV" : setRSV,
    "KDJ_K" : setKDJ_K,
    "KDJ_D" : setKDJ_D,
    "KDJ_J" : setKDJ_J
};