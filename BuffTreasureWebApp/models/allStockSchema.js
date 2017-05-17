/**
 * Created by slow_time on 2017/5/17.
 */
const mongoose = require('mongoose');

let Schema = mongoose.Schema;

let allStockSchema = new Schema({
    code: String,
    name: String,
    industry: String,
    bench: Array
}, {collection: 'allstocks'});

module.exports =  mongoose.model('allstocks', allStockSchema);