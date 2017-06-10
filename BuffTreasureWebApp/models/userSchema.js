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
    selfSelectStock : Array,    // 自选股
    strategy: Array,            // 保存的策略
    message: Array              // 消息队列
}, {
    collection: 'user',
    versionKey: false
});

module.exports = mongoose.model('user', userSchema);