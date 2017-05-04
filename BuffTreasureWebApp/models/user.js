/**
 * Created by slow_time on 2017/5/4.
 */
var mongoose = require('mongoose');

var Schema = mongoose.Schema;

// 建立user模型
var userSchema = new Schema({
    username : String,
    password : String,
    email : String
}, {collection : 'user'});

var User = mongoose.model('user', userSchema);

exports.userDB = {
    getPasswordByName: (name, callback) => {
        User.find({username : name}, (err, docs) => {
            callback(err, docs)
        });
    },

    registerUser: (user, callback) => {
        var newUser = new User(user);

        newUser.save((err, data) => {
            callback(err, data);
        });
    }
}
