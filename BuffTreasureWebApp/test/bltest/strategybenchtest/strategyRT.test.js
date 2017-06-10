/**
 * Created by wshwbluebird on 2017/5/14.
 */


let strategyRT = require("../../../bl/strategy/strategyPredict");
let StockPoolConditionVO = require("../../../vo/StockPoolConditionVO").StockPoolConditionVO;
let TradeModelVO = require("../../../vo/TradeModelVO").TradeModelVO;


let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/allInfo');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

describe('#shit11111()', function() {
    it('tem11p', function(done) {

        // let stockPoolConditionVO = new StockPoolConditionVO('沪深300',[],[],false);
        //
        //let tradeModelVO  = new TradeModelVO(10,4);
        // console.log(new Date())


        let rank = {
            'r1' : ["MA","asd",   10, 1]
            ,'r2' : ["MOM","asd",   10, 1]
            // , 'r3' : ["RSI","asd", 10, 1]
            // , 'r4' : ["KDJ_K","asd", 10, 1]
            // , 'r5' : ["KDJ_D","asd", 10, 1]
        };

        let filter = {
            "f1" :      ["volume", ">",     0],
        }

        let stockPoolConditionVO = new StockPoolConditionVO('沪深300',[],[],false);

        let tradeModelVO  = new TradeModelVO(10, 4);
        // // console.log(new Date())
        //
        //
        // let rank = {
        //     "r1" : ["MA", "asd", 10, 1],
        //     "r2" : ["MOM", "asd", 10, 1]
        //     , 'r3' : ["RSI","asd", 10, 1]
        //     , 'r4' : ["KDJ_K","asd", 10, 1]
        //     , 'r5' : ["KDJ_D","asd", 10, 1]
        // };
        // let filter = {
        //     'f1' : ["volume", '>', 0]
        // };
        strategyRT.getRTPickleData(
            stockPoolConditionVO, rank, filter ,(err,data) => {
                if (err) {
                    console.log('wrong');
                    done(err);
                }
                else {
                    //console.log();
                    console.log(data)
                    console.log('\n');
                    done();
                }
            });
    });

});
