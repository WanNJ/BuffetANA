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
mongoose.connect('mongodb://localhost/allInfo');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

describe('#shit()', function() {
    it('temp', function(done) {

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
        strategyToolPickle.getPickleData(new Date("2015-01-01"),new Date("2017-04-28"),
            stockPoolConditionVO, rank, filter, tradeModelVO, 3, (err,data) => {
            if (err) {
                console.log('wrong');
                done(err);
            }
            else {
                //console.log();
                console.log(data['HighAndOpposite'][0].backDatas)
                console.log('\n');
                done();
            }
        });
    });

});
