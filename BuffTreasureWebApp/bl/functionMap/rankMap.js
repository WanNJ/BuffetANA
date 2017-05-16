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
    })

}



exports.funtionMap = {
    'MA':setMA
};