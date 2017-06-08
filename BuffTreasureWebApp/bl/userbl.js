/**
 * Created by slow_time on 2017/5/4.
 */
let userDB = require('../models/user.js').userDB;
let allStockDB = require('../models/allstock').allStockDB;
let RTTool = require('./realtime/singleStockRT');

/**
 * 用户登录
 * @param username
 * @param password
 * @param callback
 */
exports.login = (username, password, callback) => {
  userDB.getPasswordByName(username, (err, doc) => {
      if (doc === null)
          callback(err, false);
      else if (doc["password"] !== password)
          callback(err, false);
      else
          callback(err, true);
  });
};


/**
 * 用户注册
 * @param username
 * @param password
 * @param email
 * @param callback
 */
exports.signUp = (username, password, email, callback) => {
    userDB.getPasswordByName(username, (err, doc) => {
        if (doc === null) {
            let user = {
                username : username,
                password : password,
                email : email,
                selfSelectedStock : [],
                strategy: []
            };
            userDB.registerUser(user, (err, data) => {
                callback(err, true);
            });
        }
        else
            callback(err, false);
    });
};


/**
 * 添加自选股
 * @param userName
 * @param stock
 * @param callback (err, doc) => {}
 *
 * 如果添加成功，则doc为SUCCESS
 * 如果添加的股票不存在，则doc为NOTEXIST
 * 如果重复添加同一支股票，则doc为DUPLICATED
 */
exports.addToSelfSelectStock = (userName, stock, callback) => {
    userDB.getSelfSelectStock(userName, (err, stocks) => {
        if (err)
            callback(err, false);
        else {
            let has = false;
            for (let i = 0; i < stocks["selfSelectStock"].length; i++) {
                if (stocks["selfSelectStock"][i]["stockCode"] === stock["stockCode"]) {
                    has = true;
                    break;
                }
            }
            if (has === false) {
                allStockDB.getNameByCode(stock["stockCode"], (err, doc) => {
                    if (err)
                        callback(err, false);
                    else {
                        // 判断这只股票存不存在
                        if (doc === null)
                            callback(null, 'NOTEXIST');
                        else {
                            userDB.addToSelfSelectStock(userName, stock, (err, isOK) => {
                                callback(err, 'SUCCESS');
                            });
                        }
                    }
                });
            }
            else {
                callback(null, 'DUPLICATED')
            }
        }
    });
};


/**
 * 获得某一用户的所有自选股票
 * @param userName
 * @param callback 形如(err, docs) => {}
 * docs是JSON格式，其中的键名集合是所有的自选股的代码，键名对应的值是一个数组
 * {
 *      股票代码    股票名称    现价   涨跌幅(已经乘过100，单位为"%")
 *      "000001": ["平安银行", 9.42, 0.11]
 *      "000002": ["万科A",    32.2  2.3]
 *      ...
 * }
 */
exports.getSelfSelectStock = (userName, callback) => {
    userDB.getSelfSelectStock(userName, (err, docs) => {
        if (err)
            callback(err, null);
        else {
            let stocks = {};
            let count = 0;
            docs["selfSelectStock"].forEach(t => {
                RTTool.obtainRTInfoByCode(t["stockCode"], (err, stockRTInfo) => {
                    if (err)
                        callback(err, null);
                    else {
                        let info = [];
                        info.push(t["stockName"]);
                        info.push(stockRTInfo["now_price"]);
                        info.push(stockRTInfo["change_rate"]);
                        stocks[t["stockCode"]] = info;
                    }
                    count++;
                    if (count === docs["selfSelectStock"].length)
                        callback(null, stocks);
                });
            });
        }
    });
};


/**
 * 保存策略
 * @param userName 用户名
 * @param strategy 形如：
 * {
    strategyName: String
    beginDate: Date,
    endDate: Date,
    stockPoolCondition: {},
    rank:{},
    filter:{},
    tradeModel:{},
    envSpecDay:Number,
    markNormal:Number,// 正常情况下 情况下的分数
    markHS:Number,// high and same 情况下的分数
    markHO:Number,// high and opposite 情况下的分数
    markLS:Number,// low and same 情况下的分数
    markLO:Number,// low and opposite 情况下的分数
 * }
 * stockPoolCondition {stockPoolConditionVO} 股票池选择条件
 * rank:("r1"、"r2"这些key值随便取什么都可以，只要不重复)
 *                   策略名称  升序／降序   观察期   权重
 * eg : {  "r1":    ["MA",    "asd",      10,     0.4],
 *         "r2"  :  ["MOM"    "des",      20,     0.6]
 *       }
 * filter:("f1"、"f2"这些key值随便取什么都可以，只要不重复)
 *                 筛选指标              比较符     值
 * eg : {  "f1":   ["volume",           ">",     1000000],
 *         "f2":   ["turnOverRate",     "<",     0.05]
 *       }
 * tradeModel {TradeModelVO} 交易模型
 * envSpecDay {Number}  市场观察期
 * @param callback (err, docs) => {}
 *
 * 如果保存成功，则docs为SUCCESS
 * 如果存在重名的策略，则docs为DUPLICATED
 */
exports.saveStrategy = (userName, strategy, callback) => {
    userDB.getAllStrategy(userName, (err, docs) => {
        if (err)
            callback(err, false);
        else {
            let has = false;
            for (let i = 0; i < docs["strategy"].length; i++) {
                if (docs["strategy"][i]["strategyName"] === strategy["strategyName"]) {
                    has = true;
                    break;
                }
            }
            if (has === false) {
                userDB.saveStrategy(userName, strategy, (err) => {
                    callback(err, 'SUCCESS');
                });
            }
            else {
                // 此策略名称已经存在
                if(strategy.overwrite){

                }else {
                    callback(null, 'DUPLICATED');
                }
            }
        }
    });
};


/**
 * 获得用户所有的已保存策略
 * @param userName
 * @param callback
 */
exports.getAllStrategy = (userName, callback) => {
    userDB.getAllStrategy(userName, (err, docs) => {
        if (err)
            callback(err, null);
        else {
            callback(null, docs);
        }
    });
};


/**
 * 加载某个策略
 * @param userName 用户名称
 * @param strategyName 策略名称
 * @param callback (err, strategy) => {}
 * strategy形如:
 * {
    strategyName: String
    beginDate: Date,
    endDate: Date,
    stockPoolCondition: {},
    rank:{},
    filter:{},
    tradeModel:{},
    envSpecDay:Number,
    markNormal:Number,// 正常情况下 情况下的分数
    markHS:Number,// high and same 情况下的分数
    markHO:Number,// high and opposite 情况下的分数
    markLS:Number,// low and same 情况下的分数
    markLO:Number,// low and opposite 情况下的分数
 * }
 * stockPoolCondition {stockPoolConditionVO} 股票池选择条件
 * rank:("r1"、"r2"这些key值随便取什么都可以，只要不重复)
 *                   策略名称  升序／降序   观察期   权重
 * eg : {  "r1":    ["MA",    "asd",      10,     0.4],
 *         "r2"  :  ["MOM"    "des",      20,     0.6]
 *       }
 * filter:("f1"、"f2"这些key值随便取什么都可以，只要不重复)
 *                 筛选指标              比较符     值
 * eg : {  "f1":   ["volume",           ">",     1000000],
 *         "f2":   ["turnOverRate",     "<",     0.05]
 *       }
 * tradeModel {TradeModelVO} 交易模型
 * envSpecDay {Number}  市场观察期
 */
exports.loadStrategy = (userName, strategyName, callback) => {
    userDB.getAllStrategy(userName, (err, docs) => {
        if (err)
            callback(err, null);
        else {
            let strategy = null;
            for (let i = 0; i < docs["strategy"].length; i++) {
                if (docs["strategy"][i]["strategyName"] === strategyName) {
                    strategy = docs["strategy"][i];
                    break;
                }
            }
            callback(null, strategy);
        }
    });
};