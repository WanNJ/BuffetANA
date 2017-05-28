/**
 * Created by wshwbluebird on 2017/5/28.
 */
let strategyDB = require('../../models/strategy').strategyDB;
let StockPoolConditionVO = require('../../vo/StockPoolConditionVO').StockPoolConditionVO
let TradeModelVO = require('../../vo/TradeModelVO').TradeModelVO

let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
let it = require("mocha").it;
let describe = require("mocha").describe;
mongoose.connect('mongodb://localhost/allInfo');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});




describe('thermometerDB', function() {

    /***
     * 测试是否可以保存
     */
    describe('#saveStrategy()', function() {
        it('should save successfully', function(done) {
            let strategy = {
                beginDate:new Date('2006-06-05'),
                endDate:new Date('2012-06-05'),
                stockPoolConditionVO:new StockPoolConditionVO('qwe',['we','we'],['qwe','wqew','qwe'],true),
                rank:{
                    "MA" : ["asc", 10, 1],
                    "MOM" : ["desc", 9, 1]
                    ,"RSI" : ["asd", 10, 1],
                     "KDJ_K" : ["asd", 10, 1],
                     "KDJ_D" : ["asd", 10, 1]
                },
                filter:{ "turnOverRate" : ['<', 1]},
                tradeModelVO:new TradeModelVO(10,10),
                envSpecDay:5,
                markNormal:60,
                markHO:60,
                markHS:60,
                markLS:60,
                markLO:60
            }

            strategyDB.saveStrategy(strategy , function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    done();
                }
            });
        });
    });

    describe('#getRandomStrategy()', function() {
        it('should save successfully', function(done) {
            let strategy = {
                beginDate:new Date('2006-06-05'),
                endDate:new Date('2012-06-05'),
                stockPoolConditionVO:new StockPoolConditionVO('qwe',['we','we'],['qwe','wqew','qwe'],true)
            }

            strategyDB.getRandomStrategy(function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs['stockPoolConditionVO'].stockPool)
                    done();
                }
            });
        });
    });

});