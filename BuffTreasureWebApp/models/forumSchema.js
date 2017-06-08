/**
 * Created by wshwbluebird on 2017/6/8.
 */
const mongoose = require('mongoose');

let Schema = mongoose.Schema;

let forumSchema = new Schema({
    code: String,
    good:[],
    bad:[],
    content_id:[]

}, {collection: 'forums'});

module.exports =  mongoose.model('forums', forumSchema);