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

        let stockPoolConditionVO = new StockPoolConditionVO('创业板',[],[],false);

        let tradeModelVO  = new TradeModelVO(20,2);
        // console.log(new Date())

        let rank = {
            'MA' : ["asd",   1, 3]
        };
        strategyToolPickle.getPickleData(new Date("2015-01-01"),new Date("2017-01-01"),
            stockPoolConditionVO, rank, {}, tradeModelVO, 3, (err,data) => {
            if (err) {
                console.log('wrong');
                done(err);
            }
            else {
                //console.log();
                data['Normal'].forEach(t => console.log(t.backDatas))
                console.log('\n');
               // console.log('asdsasssasdsasssasdsasssasdsasssasdsasss')
               // console.log(data['Normal'][1]);
                done();
            }
        });
    });

});
