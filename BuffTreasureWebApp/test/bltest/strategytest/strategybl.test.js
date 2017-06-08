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
mongoose.connect('mongodb://localhost/allInfo');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

describe('strategybl', function() {
    describe('#getBackResults()', function () {
        it('没有动态持仓', function (done) {
            let stockPoolConditionVO = new StockPoolConditionVO('沪深300',[],[],false);

            let tradeModelVO  = new TradeModelVO(2, 2, 20);
            // console.log(new Date())


            let rank = {
                "r1" : ["MA", "asc", 10, 1],
                "r2" : ["MOM", "desc", 9, 1]
                // ,"RSI" : ["asd", 10, 1],
                // "KDJ_K" : ["asd", 10, 1],
                // "KDJ_D" : ["asd", 10, 1]
            };
            let filter = {
                "f1" : ["turnOverRate", '<', 1]
            };
            strategy.getBackResults(new Date('2015-01-01'), new Date('2017-03-17'), stockPoolConditionVO, rank, filter, tradeModelVO, 10, function (err, docs) {
                if (err) {
                    console.log(err.message);
                    done(err);
                }
                else {
                    console.log(docs[0]);
                    // console.log(docs[0]["strategyEstimateResult"]);

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
        it('动态持仓', function (done) {
            let stockPoolConditionVO = new StockPoolConditionVO('沪深300',[],[],false);

            let tradeModelVO  = new TradeModelVO(2, 2, 0.2, 0.1);
            // console.log(new Date())


            let rank = {
                "r1" : ["MA", "asc", 10, 1],
                "r2" : ["MOM", "desc", 9, 1]
                // ,"RSI" : ["asd", 10, 1],
                // "KDJ_K" : ["asd", 10, 1],
                // "KDJ_D" : ["asd", 10, 1]
            };
            let filter = {
                "f1" : ["turnOverRate", '<', 1]
            };
            strategy.getBackResults(new Date('2015-01-01'), new Date('2017-03-17'), stockPoolConditionVO, rank, filter, tradeModelVO, 10, function (err, docs) {
                if (err) {
                    console.log(err.message);
                    done(err);
                }
                else {
                    console.log(docs[0]);
                    // console.log(docs[0]["strategyEstimateResult"]);

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