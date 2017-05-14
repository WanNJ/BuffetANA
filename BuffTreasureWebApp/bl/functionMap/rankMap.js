/**
 * Created by wshwbluebird on 2017/5/14.
 */

let strategyParam = require('../strategy/strategyParameter');

function setMA(code , codeIndex, rankIndex ,formationPeriod , AllDataList ,beginDate , endDate,  callback) {

    strategyParam.calculateMAValue(code ,beginDate , endDate, formationPeriod , (err,maList) =>{
        let keys = Object.keys(AllDataList);
        for(let i = 0 ; i < 5 ; i++){
            let pickleDataList =  AllDataList[keys[i]];

        }
    });
}



exports.funtionMap = {
    'MA':'12'
}