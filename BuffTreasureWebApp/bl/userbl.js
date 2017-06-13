/**
 * Created by slow_time on 2017/5/4.
 */
let userDB = require('../models/user.js').userDB;
let allStockDB = require('../models/allstock').allStockDB;
const md5 = require('md5');

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
      else if (doc["password"] !== md5(password))
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
                password : md5(password),
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
    strategyName: String,
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
 }
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
 * ！！！！！！！！！！！！！为小人提供的接口！！！！！！！！！
 * 获得未读消息的数目
 * @param userName
 * @param callback (err, count) => {}
 * count为未读消息的条目数，没有未读消息则为0
 */
exports.getUnreadMessageCount = (userName, callback) => {
    userDB.getAllMessage(userName, (err, messageQueue) => {
        if (err)
            callback(err, null);
        else {
            let count = 0;
            messageQueue["message"].forEach(m => {
                if (!m["isRead"])
                    count++;
            });
            callback(null, count);
        }
    });
};

/**
 * ！！！！！！！！！！！！！！！这个是供生成消息列表界面的调用的接口！！！！！！！！！！！！！！！
 * 获得未读和已读消息列表
 * @param userName 用户名称
 * @param callback (err, readMessages, unReadMessages) => {}
 * readMessages和unreadMessages分别是已读和未读消息数组，如果没有相应类型的消息则对应的消息数组长度为0，如果有消息，那么消息类型是JSON，如下所示
 * {
 *      "time":       Date类型，消息的生成时间，必须得记录，作为这条消息的唯一标识，用于后续的标记为已读
 *      "type":      'SVM'/'NN'/'CNN'/'thumbs_up'/'thumbs_up'/'error'
 *      "codeOrName":   '000001'
 *      "stockName": '平安银行'
 * }
 */
exports.getAllMessages = (userName, callback) => {
    userDB.getAllMessage(userName, (err, messageQueue) => {
        if (err)
            callback(err, null);
        else {
            let readMessages = [];
            let unreadMessages = [];
            messageQueue["message"].forEach(m => {
                if (!m["isRead"])
                    unreadMessages.push({
                        "time": m["time"],
                        "type": m["type"],
                        "codeOrName": m["codeOrName"],
                        "stockName": m["stockName"]
                    });
                else
                    readMessages.push({
                        "time": m["time"],
                        "type": m["type"],
                        "codeOrName": m["codeOrName"],
                        "stockName": m["stockName"]
                    });
            });
            readMessages.sort((a, b) => a["time"] > b["time"]);
            unreadMessages.sort((a, b) => a["time"] > b["time"]);
            readMessages.forEach(t => t["time"] = t["time"].toLocaleString());
            unreadMessages.forEach(t => t["time"] = t["time"].toLocaleString());
            callback(null, readMessages, unreadMessages);
        }
    });
};

/**
 * 将未读消息标记为已读
 * @param userName 用户名称
 * @param time 这条未读消息的生成日期
 * @param callback (err) => {}
 */
exports.markAsRead = (userName, time, callback) => {
    userDB.getAllMessage(userName, (err, messageQueue) => {
        for (let i = 0; i < messageQueue["message"].length; i++) {
            if (messageQueue["message"][i]["time"].toLocaleString() === time) {
                messageQueue["message"][i]["isRead"] = true;
                break;
            }
        }
        userDB.overrideMessage(userName, messageQueue["message"], (err) => {
            callback(err);
        });
    });
};


/**
 * ！！！！！！！！！！！！！！！这个是供画图调用的接口！！！！！！！！！！！！！！！
 * 获得具体某一个消息的内容
 * @param userName
 * @param time
 * @param callback (err, messageContent) => {}
 * messageContent的内容
 * {
 *      "time":       Date类型，消息的生成时间，必须得记录，作为这条消息的唯一标识，用于后续的标记为已读
 *      "type":      'SVM'/'NN'/'CNN'/'thumbs_up'/'thumbs_down'/'error'
 *      "codeOrName":   '000001'
 *      "stockName":    '平安银行'
 *      "content":    {JSON} 根据type类型的不同，内容不同，具体详见不同的接口
 * }
 * SVM:
 * content: {
 *                  // 个股分析时传的参数
                    code: '000001',   // 分析的股票代码
                    open: 9.8, // 分析的股票的现价
                    holdingDays: 5, // 持仓天数
                    // 分析的结果
                    relatedCode: '000017',  // 最相关股票代码
                    relatedName: '深中华A',  // 最相关股票名称
                    correlation: 0.83, // 相关系数
                    profitRate: 0.10, // 预测盈利率(未乘100)
                    base: Array, // 散点图的X轴(名称：基准股价（后复权）)
                    compare: Array, // 散点图的Y轴（名称：最相关股价（后复权））
                    CR: [0.29, "低"], // 风险系数
                    upOrDown:  ["54.29", "1"], // 可信度（单位为%），1表示涨，0表示跌
 *           }
 * CNN:
 * content: {
 *                  // 个股分析时传的参数
                    code: '000001',   // 分析的股票代码
                    holdingDays: 5, // 持仓天数
                    iterationNum: 4, // 迭代次数
                    isMarket: false,  // 是否考略市场环境 (true为考虑, false为不考虑)
                    learningWay: 'ALL', // 学习方式（只有两种：'All'， 'batch'）
                    // 分析的结果
                    process:      随着学习次数的增加 模型准确率在测试集上的表现
                           [ 0,
                             0,
                             0,
                             0,
                             0,
                             0.3799999952316284,
                             0.5099999904632568,
                             0.5099999904632568,
                             0.5099999904632568,
                             0.47999998927116394,
                             0.47999998927116394,
                             0.47999998927116394,
                             0.47999998927116394,
                             0.47999998927116394,
                             0.47999998927116394,
                             0.47999998927116394,
                             0.49000000953674316,
                             0.47999998927116394,
                             0.47999998927116394,
                             0.47999998927116394,
                             0.47999998927116394,
                             0.49000000953674316,
                             0.49000000953674316,
                             0.49000000953674316,
                             0.5,
                             0.5,
                             0.5,
                             0.5,
                             0.550000011920929,
                             0.5299999713897705,
                             0.5799999833106995,
                             0.6000000238418579,
                             0.6399999856948853,
                             0.6000000238418579,
                             0.5400000214576721,
                             0.6200000047683716,
                             0.6200000047683716,
                             0.6399999856948853,
                             0.6499999761581421,
                             0.6200000047683716,
                             0.6200000047683716,
                             0.6899999976158142,
                             0.6600000262260437,
                             0.699999988079071,
                             0.75,
                             0.7099999785423279,
                             0.6499999761581421,
                             0.6899999976158142,
                             0.7599999904632568,
                             0.7300000190734863,
                             0.8100000023841858,
                             0.7099999785423279,
                             0.7099999785423279,
                             0.75,
                             0.7799999713897705,
                             0.75,
                             0.699999988079071,
                             0.6800000071525574,
                             0.7099999785423279,
                             0.6700000166893005 ],
                    less10: 2.2016003603519962e-11,        涨幅小于-10%的概率
                    "10-7_5":  0.0000048666679504094645,    涨幅为-10%~7.5% 的概率
                    "7_5-5": 0.0002716298622544855,        涨幅为-7.5%~-5% 的概率
                    "5-2_5": 0.03460276871919632,          涨幅为5%~2.5% 的概率
                    "0-2_5": 0.056888386607170105          涨幅为0%~2.5% 的概率
                    "2_5-0":  0.9082311391830444,           涨幅为-2.5%~-0% 的概率
                    "2_5-5":  0.00000115536408884509,       涨幅为3.5%~5% 的概率
                    "5-7_5": 2.3238451163365426e-8,        涨幅为5%~7.5% 的概率
                    "7_5-10": 5.135467138345229e-12,       涨幅为7.5%~10% 的概率
                    more10: 1.6069167330101664e-8,         涨幅大于10% 的概率
                    accuracy: 0.7200000286102295,  测试结果取概率最大时，在测试集上的准确度
                }
 * NN:
 * content: {
 *                  // 个股分析时传的参数
                    code:  '000001',   // 分析的股票代码
                    holdingDays: 5, // 持仓天数
                    // 分析的结果
                    less10: "0.0142207"   涨幅小于-10%   的概率
                    "10-5": "0.0352829"    涨幅为-10%~-5% 的概率
                    "5-0": "0.493942"     涨幅为 -5%~0   的概率
                    "0-5": "0.396603"      涨幅为 0~5%    的概率
                    "5-10": "0.0341518"    涨幅为 5%~10%  的概率
                    more10: "0.0257998"  涨幅大于 10%   的概率
                    accuracy: "0.435"    测试结果取概率最大时，在测试集上的准确度
             }
 */
exports.getOneMessageContent = (userName, time, callback) => {
    userDB.getAllMessage(userName, (err, messageQueue) => {
        if (err)
            callback(err, null);
        else {
            let messageContent = {};
            for (let i = 0; i < messageQueue["message"].length; i++) {
                if (messageQueue["message"][i]["time"].toLocaleString() === time) {
                    messageContent = messageQueue["message"][i];
                    break;
                }
            }
            callback(null, messageContent);
        }
    });
};