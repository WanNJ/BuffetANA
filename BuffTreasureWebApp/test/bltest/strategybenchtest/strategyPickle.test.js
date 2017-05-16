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

describe('#shiit()', function() {
    it('temp', function(done) {

        let stockPoolConditionVO = new StockPoolConditionVO('沪深300',[],[],false);

        let tradeModelVO  = new TradeModelVO(10,10);
        // console.log(new Date())

        let rank = {
            'MA' : ["asd",   10,     0.4]
        };
        strategyToolPickle.getPickleData(new Date("2015-01-01"),new Date("2017-01-01"),
            stockPoolConditionVO, rank, {}, tradeModelVO, 3, (err,data) => {
            if (err) {
                console.log('wrong');
                done(err);
            }
            else {
                //console.log(data['Normal']);
                done();
            }
        });
    });

});
