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
f
mongoose.connect('mongodb://localhost/allInfo');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

describe('#shiit()', function() {
    it('temp', function(done) {

        let stockPoolConditionVO = new StockPoolConditionVO('创业板',[],[],false);

        let tradeModelVO  = new TradeModelVO(10,4);
        // console.log(new Date())


        let rank = {
            'MA' : ["asd",   10, 1]
            ,'MOM' : ["asd",   10, 1]
            , 'RSI' : ["asd", 10, 1]
            , 'KDJ_K' : ["asd", 10, 1]
            , 'KDJ_D' : ["asd", 10, 1]
        };

        let filter = {
            "volume" :      [">",     0],
                   }
        strategyToolPickle.getPickleData(new Date("2015-01-01"),new Date("2017-04-28"),
            stockPoolConditionVO, rank, filter, tradeModelVO, 3, (err,data) => {
            if (err) {
                console.log('wrong');
                done(err);
            }
            else {
                //console.log();
                console.log(data['HighAndOpposite'])
                console.log('\n');
                done();
            }
        });
    });

});
