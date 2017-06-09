/**
 * Created by wshwbluebird on 2017/6/9.
 */


const stockRTDB = require('../../models/stockRTInfo').stockRTInfoDB;
const singleStockDB = require('../../models/singleStock').singleStockDB;

/**
 * 获得当前的市场温度参数
 * 形式和数据库里的一样
 * @param callBack (err,doc)
 * {
 *   limitUp: 7,
      limitDown: 1,
      halfLimitUp: 42,
      halfLimitDown: 4,
      temperature: 87.5,
      lastLimitUp: '0.00000',
      lastLimitDown: '-0.26000',
      lastTurnOver: '62.00000',
      moneyEffect: '37.78990' }
 */
exports.getCurrentThermometor = (callback) =>{
    stockRTDB.getAllRTInfo((err,doc)=>{
        if(err){
            callback(err,null);
        }else{
            //获取返回前一日信息的Promise
            function getStockByDateBefore(oldDoc, date) {
                date = new Date(date - 24000 * 3600);
                //console.log(date)
                return new Promise(function (resolve, reject) {
                    singleStockDB.getStockInfoByDate(date, (err, doc) => {

                        //在异步操作成功时调用，并将异步操作的结果，作为参数传递出去
                        if (doc.length !== 0) {
                            let data = {};
                            data['before'] = doc;
                            data['today'] = oldDoc;
                            resolve(data)
                        }
                        else
                        //算是另一种形式的递归调用
                            resolve(getStockByDateBefore(oldDoc, date))
                    });
                })
            }

            //真正计算我们需要的环境内容
            function calculateThermometer(givenData) {
                //应该是草被一份数据
                const before = [...givenData['before']];

                const today = [...givenData['today']];

                //建立 要计算的数据模型
                let curDate = today[0]['date'];
                let limitUp = 0; //当日涨停股票的个数
                let limitDown = 0; //当日跌停股票的个数
                let halfLimitUp = 0; //当日涨超过5%股票的个数
                let halfLimitDown = 0; //当日跌超过5%股票的个数
                let temperature;   //市场温度 这里用通信达的计算方法 越高越好
                let lastLimitUp;  //昨日涨停股票今日表现    反应市场的追涨情况
                let lastLimitDown; //跌停涨停股票今日表现    反应市场的追跌情况
                let lastTurnOver;    //换手率前50只个股赚钱效应
                let yesUpNum = 0;      // 昨日涨停股票个数
                let yesDownNum = 0;   // 昨日跌停股票个数
                let yesUpValue = 0;   // 昨日涨停股票今日总体涨幅
                let yesDownValue = 0;  // 昨日跌停股票今日总体涨幅
                let upNum = 0;       // 今日上涨股票个数
                let downNum = 0;     //今日下跌股票个数
                let moneyEffect;    //率前50只个股赚钱效应

                let js = 0;
                today.forEach((data) => {
                    if (data['volume'] !== 0) {

                        //计算今日不同涨幅股票的个数
                        if (data['change_rate'] > 0) {
                            upNum++;
                            if (data['change_rate'] >= 5) {
                                halfLimitUp++;
                                if (data['change_rate'] >= 10)
                                    limitUp++
                            }
                        } else if (data['change_rate'] < 0) {
                            downNum++;
                            if (data['change_rate'] <= -5) {
                                halfLimitDown++;
                                if (data['change_rate'] <= -10)
                                    limitDown++
                            }
                        }


                        //计算昨日涨停跌停今天的平均涨幅
                       // console.log(before.length)
                        while(js< before.length && before[js]['code']<data['code']){
                            js++;
                        }

                        if(js< before.length && before[js]['code']===data['code']) {
                            let lastInfo = before[js];
                            if (lastInfo['changeRate'] >= 10) {
                                yesUpNum++;
                                yesUpValue += data['change_rate'];
                            } else if (lastInfo['changeRate'] <= -10) {
                                yesDownNum++;
                                yesDownValue += data['change_rate'];
                            }
                        }
                    }
                });


                //汇总统计信息
                temperature =
                    limitUp * 100 / ((limitDown + limitUp) === 0 ? 1 : (limitDown + limitUp));
                moneyEffect =
                    upNum * 100 / (downNum + upNum);

                lastLimitDown = yesDownValue / (yesDownNum === 0 ? 1 : yesDownNum);
                lastLimitUp = yesUpValue / (yesUpNum === 0 ? 1 : yesUpNum);

                //计算换手率前50 的赚钱效应

                let changUpNum = 0;
                let changDownNum = 0;
                today
                    .sort((a, b) => b['turnOverRate'] - a['turnOverRate'])
                    .slice(0, 50)
                    .map(d => d['change_rate'])
                    .forEach(d => {
                            if (d > 0) changUpNum++;
                            else if (d < 0) changDownNum++;
                        }
                    );

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
                };

                return thermometer

            }

            //Promise  逐步返回调用

                getStockByDateBefore(doc, Date.now())
                .then((data) => calculateThermometer(data))
                .then(data => callback(err, data))

        }

    });
}

/**
 * 获得当前市场环境的分类
 * @param callback
 * 形如（err，doc）
 * doc ： String
 * eg：  'LowAndOpposite'
 */
exports.getCurrentENV = (callback) =>{
    this.getCurrentThermometor((err,doc)=>{
        /**
         *
         * @param temp 温度
         * @param earnEffect50
         * @param earnEffectAll
         * @param lastUpToday
         * @param lastDownToday
         */
        let getClassify = function(temp , earnEffect50, earnEffectAll, lastUpToday, lastDownToday) {
            let w = temp * 0.3 + earnEffect50 *0.3 + earnEffectAll*0.4;
            let q = lastUpToday - lastDownToday;
            let strw;
            let strq;
            // console.log(w)

            //console.log(w)
            if(w > 69)
                strw = 'High'
            else if(w < 69)
                strw = 'Low';
            else
                return 'Normal';
            if(q > 4.8)
                strq = 'Same';
            else if(q < 4.8)
                strq = 'Opposite';
            else
                return 'Normal';
            return `${strw}And${strq}`;
        }

        let result = getClassify(doc['temperature'],doc['lastTurnOver'],doc['moneyEffect'],doc['lastLimitUp'],doc['lastLimitDown']);
        callback(err,result)

    })
}