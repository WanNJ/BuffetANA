/**
 * Created by wshwbluebird on 2017/5/14.
 */


let strategyToolPickle = require("../../../bl/strategy/strategyPickle");
let StockPoolConditionVO = require("../../../vo/StockPoolConditionVO").StockPoolConditionVO;
let TradeModelVO = require("../../../vo/TradeModelVO").TradeModelVO;


let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');

mongoose.connect('mongodb://localhost/formal');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

describe('#divideDaysByThermometer()', function() {
    it('temp', function(done) {

        let stockPoolConditionVO = new StockPoolConditionVO('中小板',[],[],false);

        let tradeModelVO  = new TradeModelVO(10,10);

        let rank = {
            'MA' : ["asd",   10,     0.4]
        }
        strategyToolPickle.getPickleData(new Date("2015-01-01"),new Date("2016-01-01"),
            stockPoolConditionVO, rank, {}, tradeModelVO, 3, (err,data) => {
            if (err) {
                console.log('wrong')
                done(err);
            }
            else {
                console.log(data);
                done();
            }
        });
    });
});
