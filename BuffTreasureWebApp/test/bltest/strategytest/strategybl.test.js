/**
 * Created by slow_time on 2017/5/16.
 */
let strategy = require('../../../bl/strategy/strategybl');


let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
let TradeModelVO = require("../../../vo/TradeModelVO.js").TradeModelVO;
let StockPoolConditionVO = require("../../../vo/StockPoolConditionVO.js").StockPoolConditionVO;
mongoose.connect('mongodb://localhost/formal');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

describe('strategybl', function() {
    describe('#getBackResults()', function () {
        it('should', function (done) {
            let stockPoolConditionVO = new StockPoolConditionVO('沪深300',[],[],false);

            let tradeModelVO  = new TradeModelVO(10,2);
            // console.log(new Date())


            let rank = {
                "MA" : ["asd", 10, 1],
                "MOM" : ["asd", 10, 1]
                // ,"RSI" : ["asd", 10, 1],
                // "KDJ_K" : ["asd", 10, 1],
                // "KDJ_D" : ["asd", 10, 1]
            };
            strategy.getBackResults(new Date('2015-01-01'), new Date('2017-04-28'), stockPoolConditionVO, rank, {}, tradeModelVO, 10, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    // let i = 1;
                    // docs.forEach(doc => {
                    //     console.log(i);
                    //     console.log(doc["strategyDayRatePiece"]);
                    //     i++;
                    // });
                    done();
                }
            });
        });
    });
});