/**
 * Created by slow_time on 2017/5/4.
 */
let userDB = require('../models/user.js').userDB;
let allStockDB = require('../models/allstock').allStockDB;

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
                selfSelectedStock : []
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
 * @param callback
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
                            callback(null, 'DUPLICATED');
                        else {
                            userDB.addToSelfSelectStock(userName, stock, (err, isOK) => {
                                callback(err, 'SUCCESS');
                            });
                        }
                    }
                });
            }
            else {
                callback(null, 'NOTEXSIT')
            }
        }
    });
};


/**
 * 获得某一用户的所有自选股票
 * @param userName
 * @param callback
 */
exports.getSelfSelectStock = (userName, callback) => {
    userDB.getSelfSelectStock(userName, (err, docs) => {
        if (err)
            callback(err, null);
        else {
            let stocks = {};
            docs["selfSelectStock"].forEach(t => {
                stocks[t["stockCode"]] = t["stockName"];
            });
            callback(null, stocks);
        }
    });
};