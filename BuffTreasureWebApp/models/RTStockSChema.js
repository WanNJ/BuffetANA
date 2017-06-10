/**
 * Created by wshwbluebird on 2017/6/10.
 */

const mongoose = require('mongoose');

let Schema = mongoose.Schema;

let RTStockSchema = new Schema({
    strategyScore: [],
    chooseStockAbility:[],
    profitAbility:[],
    antiRiskAbility:[],
    dummyID : Number

}, {collection: 'RTStock'});

module.exports =  mongoose.model('RTStock', RTStockSchema);