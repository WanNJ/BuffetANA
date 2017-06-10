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
                strategy: [],
                message: []
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
 * docs是数组格式
 * [
 *        股票名称    股票代码
 *      ["平安银行", "000001"],
 *      ["万科A",    "000002"],
 *      ...
 * ]
 */
exports.getSelfSelectStock = (userName, callback) => {
    userDB.getSelfSelectStock(userName, (err, docs) => {
        if (err)
            callback(err, null);
        else {
            let stocks = [];
            docs["selfSelectStock"].forEach(t => {
                stocks.push([t["stockName"], t["stockCode"]]);
            });
            callback(null, stocks);
        }
    });
};


/**
 * 保存策略
 * @param userName 用户名
 * @param strategy 形如：
 * {
    strategyName: String
    overwrite: Bool,    //是否覆盖重名
    beginDate: Date,
    endDate: Date,
    stockPoolCondition: {},
    rank:{},
    filter:{},
    tradeModel:{},
    marketObserve:Number,
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
 * marketObserve {Number}  市场观察期
 * @param callback (err, docs) => {}
 *
 * 如果保存成功，则docs为SUCCESS
 * 如果存在重名的策略，则docs为DUPLICATED
 */
exports.saveStrategy = (userName, strategy, callback) => {
    userDB.getAllStrategy(userName, (err, docs) => {
        if (strategy.overwrite==="true") {
            if (err)
                callback(err, false);
            else {
                for (let i = 0; i < docs["strategy"].length; i++) {
                    if (docs["strategy"][i]["strategyName"] === strategy["strategyName"]) {
                        docs["strategy"][i] = strategy;
                        break;
                    }
                }
                userDB.overrideStrategy(userName, docs["strategy"], (err) => {
                    if (err)
                        callback(err, false);
                    else
                        callback(null, true);
                });
            }
        }
        else {
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
            let strategy = {};
            docs["strategy"].forEach(t => {
                strategy[t["strategyName"]] = [];
                strategy[t["strategyName"]].push(t["markNormal"]);
                strategy[t["strategyName"]].push(t["markHS"]);
                strategy[t["strategyName"]].push(t["markHO"]);
                strategy[t["strategyName"]].push(t["markLS"]);
                strategy[t["strategyName"]].push(t["markLO"]);
            });
            callback(null, strategy);
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


/**
 * 获得未读消息
 * @param userName 用户名称
 * @param callback (err, unreadMessages) => {}
 * unreadMessages是一个未读消息数组，如果没有未读消息则数组长度为0，如果有未读消息，那么消息类型是JSON，如下所示
 * {
 *      "time":       Date类型，消息的生成时间
 *      "isRead":     true/false,
 *      "type":      'analysis1'/'analysis2'/'analysis3'/'thumbs_up'
 *      "toString":  '你的xxx股票分析结果已出，请点击查看'/'xxx赞了你的评论'
 *      "content":   '000001'/
 *                    {
 *                          "CR": [0.29, '低']              风险系数
 *                          "upOrDown": ['54.29', '1']
 *                    }
 * }
 */
exports.getUnreadMessage = (userName, callback) => {
    userDB.getAllMessage(userName, (err, messageQueue) => {
        if (err)
            callback(err, null);
        else {
            let unreadMessages = [];
            messageQueue["message"].forEach(m => {
                if (!m["isRead"])
                    unreadMessages.push(m);
            });
            callback(null, unreadMessages);
        }
    });
};