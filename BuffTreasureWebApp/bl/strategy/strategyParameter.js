/**
 * Created by wshwbluebird on 2017/5/14.
 */


let singleStockDB = require('../../models/singleStock').singleStockDB;

/**
 *
 * @param code 股票代码
 * @param beginDate 开始日期
 * @param endDate  结束日期
 * @param formationPeriod  形成期(观察期)
 * @param callback 形如(err,docs);
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *  所有 这个预先计算的部分  返回的数组元素都按照这格式
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *      docs 形式如下
 *      {
 *        date:  date
 *        value  : MA偏离量
 *      } 的数组
 *      时间按照从小到大排
 *      !!!数组中没有value的date 不存在
 *      比如2014-01-02 没有MA均线  那么返回的数组里面没有 date 2014-01-02 就根本不会出现
 */
exports.calculateMAValue = function (code ,beginDate , endDate, formationPeriod ,callback) {
    let searchBeginDate = new Date(beginDate-(formationPeriod * 10+20)*24000*600);
    singleStockDB.getStockInfoInRangeDate(code ,searchBeginDate,endDate ,(err,doc) => {
        doc.reverse();
        let curMASum = 0;
        let MAValue = [];
        //console.log(doc[0])

        /**
         * added by TY
         * TODO filter方法并不能改变原来的数组，应该需要修改！！！！
         */
        doc.filter(data => data["volume"]!==0);
        for(let i = 0; i < formationPeriod ; i++){
            curMASum+= doc[i]["adjClose"];
        }


        for(let i  = 0;  doc[i]["date"] -beginDate >= 0 ; i++){

            let temp = (curMASum - doc[i]["adjClose"])/curMASum;
            let part = {
                "date" : doc[i]["date"],
                "value" : temp
            };
            MAValue.push(part);
            /**
             * added by TY
             * TODO 此处的 i+formationPeriod 有可能存在数组下标越界的可能，应该加一个判断
             */
            curMASum += doc[i+formationPeriod]["adjClose"] - doc[i]["adjClose"];
        }
        MAValue.reverse();
        callback(err,MAValue);
    });
};


/**
 * 获得动量排序值
 * @param code
 * @param beginDate
 * @param endDate
 * @param formationPeriod
 * @param callback 形如(err,docs);
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *  所有 这个预先计算的部分  返回的数组元素都按照这格式
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *      docs 形式如下
 *      {
 *        date:  date
 *        value  : MA偏离量
 *      } 的数组
 *      时间按照从小到大排
 *      !!!数组中没有value的date 不存在
 *      比如2014-01-02 没有MOM值  那么返回的数组里面没有 date 2014-01-02 就根本不会出现
 */
exports.calculateMOMValue = function (code ,beginDate , endDate, formationPeriod ,callback) {
    let searchBeginDate = new Date(beginDate-(formationPeriod * 10+20)*24000*600);
    singleStockDB.getStockInfoInRangeDate(code, searchBeginDate, endDate, (err, docs) => {
        docs.reverse();
        /**
         * 计算观察期的收益率时的临时变量
         */
        let beginAdj = 0;  // 观察期开始时的复权收盘价
        let endAdj = 0;    // 观察期结束时的复权收盘价

        let MOMValue = [];

        docs = docs.filter(data => data["volume"] !== 0);
        for (let i = 1; docs[i]["date"] - beginDate >= 0 && i + formationPeriod < docs.length; i++) {
            endAdj = docs[i]["adjClose"];
            beginAdj = docs[i + formationPeriod]["adjClose"];
            // 该观察内的收益率
            let yield_rate = (endAdj - beginAdj) / beginAdj;
            MOMValue.push({
                "date" : docs[i - 1]["date"],
                "value" : yield_rate
            });
        }
        MOMValue.reverse();
        callback(err, MOMValue);
    });
};

