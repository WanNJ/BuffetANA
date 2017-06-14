/**
 * Created by wshwbluebird on 2017/6/9.
 */


const RTEnvironmentDB = require('../../models/RTEnvironment').RTEnvironmentDB;


/**
 * 获得当前的市场温度参数
 * 形式和数据库里的一样
 * @param callBack (err,doc)
 * {
      limitUp: 7,
      limitDown: 1,
      halfLimitUp: 42,
      halfLimitDown: 4,
      temperature: 87.5,
      lastLimitUp: '0.00000',
      lastLimitDown: '-0.26000',
      lastTurnOver: '62.00000',//换手率前50只股票的赚钱效应
      moneyEffect: '37.78990' //赚钱效应
      }
 */
exports.getCurrentThermometor = (callback) =>{
    RTEnvironmentDB.getRTEnv((err,doc)=>{
        if(err){
            console.log('log RT env fail')
        }
        callback(err,doc)
    })
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
            // let w = temp * 0.3 + earnEffect50 *0.3 + earnEffectAll*0.4;
            let w = temp
            let q = lastUpToday - lastDownToday;
            let strw;
            let strq;
            // console.log(w)

            //console.log(w)
            if(w >= 50)
                strw = 'High'
            else if(w < 50)
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