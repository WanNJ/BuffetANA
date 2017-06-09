/**
 * Created by wshwbluebird on 2017/5/14.
 */

let para = require('../strategy/strategyParameter').calculateFilterValue


exports. setFilter = function(code ,codeIndex, name , isBigger , value ,beginDate, endDate, AllDataList) {
    return new Promise((resolve,reject)=>{
        para(name ,code ,beginDate , endDate ,(err,valueList)=>{
            let keys = Object.keys(AllDataList);
            for(let i = 0 ; i < keys.length ; i++){
                let pickleDataList =  AllDataList[keys[i]];
                let p = 0 ; //data的指针
                pickleDataList.forEach(pickleData =>{
                    let backData = pickleData.backDatas[codeIndex];
                    if(backData.valid===true && typeof valueList[p] !== 'undefined') {
                        while (typeof valueList[p + 1] !== 'undefined' && valueList[p + 1]['date'] - pickleData.beginDate < 0) p++;
                        let temp = valueList[p]['value'];
                        //判断是否降序排泄
                        if((isBigger && temp <= value)||(!isBigger && temp >= value)){
                            backData.filterValue  =false;
                        }
                    }else{
                        backData.filterValue  =false;
                    }
                    pickleData.backDatas[codeIndex] = backData;
                });
                AllDataList[keys[i]] = pickleDataList;
            }
            resolve(AllDataList);
        });
    })


}


