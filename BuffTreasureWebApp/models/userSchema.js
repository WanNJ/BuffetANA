/**
 * Created by slow_time on 2017/5/17.
 */
const mongoose = require('mongoose');

const Schema = mongoose.Schema;

// 建立user模型
const userSchema = new Schema({
    username : String,
    password : String,
    email : String,
    selfSelectStock : Array    // 自选股
}, {
    collection: 'user',
    versionKey: false
});

module.exports = mongoose.model('user', userSchema);