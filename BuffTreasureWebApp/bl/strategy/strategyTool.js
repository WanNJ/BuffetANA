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
let PickleDataVO = require("../../vo/PickleData").PickleData;

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




            })
            let ReturnValue = {
                'Normal' : normalPickleList,
                'HighAndOpposite':HighAndOppositePickleList,
                'HighAndSame':HighAndSamePickleList,
                'LowAndSame':LowAndSamePickleList,
                'LowAndOpposite':LowAndOppositePickleList
            }
            callback(null,ReturnValue);

        });
}