/**
 * Created by wshwbluebird on 2017/6/10.
 */

const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const hotBoardSchema = new Schema({
    "now": Number,
    "hot": Array
}, {
    collection: 'hot',
    versionKey: false
});

module.exports =  mongoose.model('hot', hotBoardSchema);