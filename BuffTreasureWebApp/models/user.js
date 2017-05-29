/**
 * Created by slow_time on 2017/5/4.
 */


const User = require('./userSchema');

exports.userDB = {
    getPasswordByName: (name, callback) => {
        User.findOne({username : name}, ['password'], (err, doc) => {
            callback(err, doc)
        });
    },

    registerUser: (user, callback) => {
        let newUser = new User(user);

        newUser.save((err, data) => {
            callback(err, data);
        });
    },


    /**
     * 添加某只股票进某个用户的自选股列表
     * @param userName
     * @param stock
     * @param callback
     */
    addToSelfSelectStock: (userName, stock, callback) => {
        User.updateOne({username : userName}, {$push: {selfSelectStock: stock}}, (err) => {
            if (err)
                callback(err, false);
            else
                callback(null, true);
        });
    },

    /**
     * 获得某个用户的所有自选股
     * @param userName
     * @param callback
     */
    getSelfSelectStock: (userName, callback) => {
        User.findOne({username: userName}, ['selfSelectStock'], (err, stocks) => {
            callback(err, stocks);
        });
    },

    /**
     * 保存策略
     * @param userName {String} 用户名
     * @param strategy 策略的具体参数
     * @param callback
     */
    saveStrategy: (userName, strategy, callback) => {
        User.updateOne({username : userName}, {$push: {strategy: strategy}}, (err) => {
            if (err)
                callback(err, false);
            else
                callback(null, true);
        });
    },

    /**
     * 获得某个用户所有已保存的策略
     * @param userName
     * @param callback
     */
    getAllStrategy: (userName, callback) => {
        User.findOne({username: userName}, ['strategy'], (err, strategy) => {
            callback(err, strategy);
        });
    }
};
