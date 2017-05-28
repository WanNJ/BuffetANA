/**
 * Created by wshwbluebird on 2017/5/28.
 */
let strategySchema = require('./strategySchema');

exports.strategyDB ={
    saveStrategy :(strategy,callback)=>{
        let newStrategy = new strategySchema(strategy);
        newStrategy.save((err, data) => {
            callback(err, data);
        });
    },

    getRandomStrategy: (callback) => {
        strategySchema.findOne((err, doc) => {
            callback(err, doc)
        });
    },

    getAllStrategy: (callback) => {
        strategySchema.find((err, doc) => {
            callback(err, doc)
        });
    },
}