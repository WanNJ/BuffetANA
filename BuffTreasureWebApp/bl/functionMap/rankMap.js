/**
 * Created by wshwbluebird on 2017/5/14.
 */

let strategyParam = require('../strategy/strategyParameter');


/**
 * 注入MA的Promise方法
 * TODO  没有检验  reject (如果有异常 还没写)
 * @param code          股票代码
 * @param codeIndex     股票代码在数组中的位置
 * @param rankIndex     这种排序早rank中的位置
 * @param formationPeriod  观察期
 * @param AllDataList   需要更新的五个pickleDataList
 * @param beginDate     回测的开始日期
 * @param endDate       结束如期
 * @param callback      形如（err，doc）
 *      doc 为更新后的 AllDataList
 */
function setMA(code , codeIndex, rankIndex ,formationPeriod , AllDataList ,beginDate , endDate) {
    return new Promise((resolve,reject) => {
        strategyParam.calculateMAValue(code ,beginDate , endDate, formationPeriod , (err,maList) =>{
            let keys = Object.keys(AllDataList);
            for(let i = 0 ; i < 5 ; i++){
                let pickleDataList =  AllDataList[keys[i]];
                let p = 0 ; //data的指针
                pickleDataList.forEach(pickleData =>{
                    while(maList[p+1]['date']- pickleData.beginDate < 0) p++;
                    let backData = pickleData.backDatas[codeIndex];
                    backData.mixRank.push(maList[p]['value']);
                    pickleData.backDatas[rankIndex] = backData;
                });
                AllDataList[keys[i]] = pickleDataList;
            }
            //console.log("he")
            resolve(AllDataList);
        });
    })

}



exports.funtionMap = {
    'MA':setMA
}