/**
 * Created by slow_time on 2017/5/4.
 */
const mongoose = require('mongoose');

const Schema = mongoose.Schema;

// 建立user模型
const userSchema = new Schema({
    username : String,
    password : String,
    email : String
}, {collection : 'user'});

const User = mongoose.model('user', userSchema);

exports.userDB = {
    getPasswordByName: (name, callback) => {
        User.findOne({username : name}, (err, doc) => {
            callback(err, doc)
        });
    },

    registerUser: (user, callback) => {
        let newUser = new User(user);

        newUser.save((err, data) => {
            callback(err, data);
        });
    }
};
