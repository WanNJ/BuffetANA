/**
 * Created by wshwbluebird on 2017/5/9.
 */


let thermometerDB = require('../../models/thermometer').thermometerDB;
let singleStockDB = require('../../models/singleStock.js').singleStockDB;
let bl = require('./thermometerbl');




/**
 * 获取某一天的市场温度环境
 * 不是实时的  基于历史的
 *
 * 如果 存在直接从数据库中提取
 * 如果不存在去计算
 *
 * @param date   日期
 * @param callback   回调函数 形如 (err, docs) => { }
 */
exports.getDailyEnvironment  = (date , callback) =>{
    thermometerDB.getThermometerByDate(date,(err,doc)=>{
        if(err){
            callback(err,null);
        }else{

            //如果要查找的市场温度信息不存在!
            if(doc === null){

               //获取返回当日信息的Promise
                console.log('BLThermometer: Calculating')
               function getStockByDate(date){
                   return new Promise(function(resolve, reject){
                       singleStockDB.getStockInfoByDate(date,(err,doc)=>{
                           //在异步操作成功时调用，并将异步操作的结果，作为参数传递出去
                           if(doc.length !==0 ) {
                               let data = {};
                               data['doc'] = doc;
                               data['date'] = date;
                               resolve(data)
                           }
                           else
                               reject();
                       });
                   })
               }

                //获取返回前一日信息的Promise
                function getStockByDateBefore(oldDoc,date){
                    date = new Date(date - 24000 * 3600);
                    //console.log(date)
                    return new Promise(function(resolve, reject){
                        singleStockDB.getStockInfoByDate(date,(err,doc)=>{
                            //TODO   最前端 怎么办 没有解决

                            //在异步操作成功时调用，并将异步操作的结果，作为参数传递出去
                            if(doc.length !==0 ) {
                                let data = {};
                                data['before'] = doc;
                                data['today'] = oldDoc;
                                resolve(data)
                            }
                            else
                                //算是另一种形式的递归调用
                                resolve(getStockByDateBefore(oldDoc,date))
                        });
                    })
                }

                //真正计算我们需要的环境内容
                function calculateThermometer(givenData) {
                    //应该是草被一份数据
                    const before = [...givenData['before']];
                    const today = [...givenData['today']];
                    //建立 要计算的数据模型
                    let curDate = today[0]['date']
                    let limitUp = 0; //当日涨停股票的个数
                    let limitDown = 0 ; //当日跌停股票的个数
                    let halfLimitUp = 0; //当日涨超过5%股票的个数
                    let halfLimitDown = 0; //当日跌超过5%股票的个数
                    let temperature;   //市场温度 这里用通信达的计算方法 越高越好
                    let lastLimitUp;  //昨日涨停股票今日表现    反应市场的追涨情况
                    let lastLimitDown; //跌停涨停股票今日表现    反应市场的追跌情况
                    let lastTurnOver;    //换手率前50只个股赚钱效应
                    let yesUpNum = 0;      // 昨日涨停股票个数
                    let yesDownNum = 0;   // 昨日跌停股票个数
                    let yesUpValue = 0    // 昨日涨停股票今日总体涨幅
                    let yesDownValue = 0  // 昨日跌停股票今日总体涨幅
                    let upNum = 0;       // 今日上涨股票个数
                    let downNum = 0;     //今日下跌股票个数
                    let moneyEffect;    //率前50只个股赚钱效应

                   today.forEach((data,i) => {

                       if(data['volume']!==0) {

                           //计算今日不同涨幅股票的个数
                           if (data['changeRate'] > 0) {
                               upNum++;
                               if (data['changeRate'] >= 5) {
                                   halfLimitUp++;
                                   if (data['changeRate'] >= 10)
                                       limitUp++
                               }
                           }else if (data['changeRate'] < 0) {
                               downNum++;
                               if (data['changeRate'] <= -5) {
                                   halfLimitDown++;
                                   if (data['changeRate'] <= -10)
                                       limitDown++
                               }
                           }

                           //计算昨日涨停跌停今天的平均涨幅
                           let lastInfo = before[i];

                           if(lastInfo['changeRate'] >= 10){
                               yesUpNum++;
                               yesUpValue += data['changeRate'];
                           }else if(lastInfo['changeRate'] <= -10){
                               yesDownNum++;
                               yesDownValue += data['changeRate'];
                           }
                       }
                   });


                    //汇总统计信息
                    temperature =
                        limitUp* 100 /((limitDown + limitUp)===0?1:(limitDown + limitUp));
                    moneyEffect =
                        upNum*100/(downNum+upNum);

                    lastLimitDown = yesDownValue / (yesDownNum===0?1:yesDownNum);
                    lastLimitUp = yesUpValue / (yesUpNum===0?1:yesUpNum);

                    //计算换手率前50 的赚钱效应

                    let changUpNum = 0;
                    let changDownNum = 0;
                    today
                        .sort((a,b) => b['turnOverRate'] - a['turnOverRate'])
                        .slice(0,50)
                        .map(d => d['changeRate'])
                        .forEach( d =>{
                            if(d > 0) changUpNum ++;
                            else if(d < 0) changDownNum ++;
                        }

                    )

                    lastTurnOver = (changUpNum * 100) / (changUpNum + changDownNum)

                    //返回统计数据
                    let thermometer = {
                        "date": curDate,
                        "limitUp": limitUp,  //当日涨停股票的个数
                        "limitDown": limitDown,  //当日跌停股票的个数
                        "halfLimitUp": halfLimitUp, //当日涨超过5%股票的个数
                        "halfLimitDown": halfLimitDown, //当日跌超过5%股票的个数
                        "temperature": temperature,  //市场温度 这里用通信达的计算方法 越高越好
                        "lastLimitUp": lastLimitUp.toFixed(5),   //昨日涨停股票今日表现    反应市场的追涨情况
                        "lastLimitDown": lastLimitDown.toFixed(5),  //跌停涨停股票今日表现    反应市场的追跌情况
                        "lastTurnOver": lastTurnOver.toFixed(5),  //换手率前50只个股赚钱效应为26%
                        "moneyEffect": moneyEffect.toFixed(5),   //总体赚钱效应在一个总体内赚钱效应  上涨家数与涨跌总家数之比
                     }

                    return thermometer

                }

                // 把得到的结果写会数据库 的promise
                //获取返回前一日信息的Promise
                function getWriteToDB(thermometer){
                    return new Promise(function(resolve, reject){
                        thermometerDB.writeThermometerByDate(thermometer,(err,doc)=>{
                            resolve(thermometer);
                        });
                    })
                }

               //Promise  逐步返回调用
               getStockByDate(date)
                   .then((data) => getStockByDateBefore(data['doc'],data['date']))
                   .then((data) => calculateThermometer(data))
                   .then((data) => getWriteToDB(data))
                   .then(data => callback(null,data))
                   .catch(()=>callback(err,null));

            }else{
                console.log('BLThermometer:  Not Need Calculate');
                callback(err, doc)

            }

        }
    });
}


exports.WriteDailyEnvironmentRange  = (beginDate, endDate , callback) =>{
    let oneDayTime = 24000*3600;
    function getWriteOneDayPromise(date) {
        //date = new Date(date - 24000 * 3600);
        console.log(date)
        return new Promise(function(resolve, reject){
            if(date  < beginDate){
                reject(new Error('nothing'));
            }else{
                bl.getDailyEnvironment(date,(err,doc)=>{
                    date = new Date(date - 24000 * 3600);
                    console.log(doc!==null)
                    console.log(date)
                    resolve(getWriteOneDayPromise(date));
                })
            }
        });
    }

    getWriteOneDayPromise(endDate).then(date =>  console.log(date)).catch(err=>callback());
}


