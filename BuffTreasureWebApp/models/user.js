/**
 * Created by slow_time on 2017/5/4.
 */


const User = require('./userSchema')

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
    }
};
