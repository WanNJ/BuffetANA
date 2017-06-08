/**
 * Created by wshwbluebird on 2017/6/8.
 */
const mongoose = require('mongoose');

let Schema = mongoose.Schema;

let contentSchema = new Schema({
    code: String,
    userID: String,
    content:String,
    date:Date,
    good:[],
    bad:[]
}, {collection: 'contents'});

module.exports =  mongoose.model('contents',contentSchema)