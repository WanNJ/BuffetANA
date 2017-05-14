/**
 * Created by wshwbluebird on 2017/5/13.
 */

let strategyParabl = require("../../../bl/strategy/strategyParameter");

let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');

mongoose.connect('mongodb://localhost/formal');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

let StockPoolConditionVO = require("../../../vo/StockPoolConditionVO").StockPoolConditionVO;

describe('#calculateMAValue()', function() {
    it('temp', function(done) {
        strategyParabl.calculateMAValue('000001' , new Date("2015-01-01"),new Date("2017-02-01"),20 , (err,data) => {
            if (err) {
                console.log('wrong')
                done(err);
            }
            else {
                console.log(data)
                done();
            }
        });
    });
});
