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
                stockPoolConditionVO:new StockPoolConditionVO("沪深300",null,null,false),
                rank:{
                    "r1" : ["MOM" ,"asc", 10, 1],
                    "r2" : ["MOM","desc", 8, 1],
                    "r6" : ["MOM","desc", 8, 1]
                },
                filter:{},
                tradeModelVO:new TradeModelVO(10,10),
                envSpecDay:5,
                markNormal:{
                    "profitAbility" : 18,
                    "stability" : 18,
                    "chooseStockAbility" : 18,
                    "absoluteProfit" : 18,
                    "antiRiskAbility" :18,
                    "strategyScore" : 72 },
                markHO:{
                    "profitAbility" : 18,
                    "stability" : 18,
                    "chooseStockAbility" : 18,
                    "absoluteProfit" : 18,
                    "antiRiskAbility" :18,
                    "strategyScore" : 72 },
                markHS:{
                    "profitAbility" : 18,
                    "stability" : 18,
                    "chooseStockAbility" : 18,
                    "absoluteProfit" : 18,
                    "antiRiskAbility" :18,
                    "strategyScore" : 72 },
                markLS:{
                    "profitAbility" : 18,
                    "stability" : 18,
                    "chooseStockAbility" : 18,
                    "absoluteProfit" : 18,
                    "antiRiskAbility" :18,
                    "strategyScore" : 72 },
                markLO:{
                    "profitAbility" : 18,
                    "stability" : 18,
                    "chooseStockAbility" : 18,
                    "absoluteProfit" : 18,
                    "antiRiskAbility" :18,
                    "strategyScore" : 72 },
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
                    console.log(docs)
                    done();
                }
            });
        });
    });


    describe('#getBestStrategyByEnv()', function() {
        it('should save successfully', function(done) {
            strategyDB.getBestStrategyByEnv('HighAndSame','strategyScore',1,function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs)
                    done();
                }
            });
        });
    });

    describe('#getAllStrategy()', function() {
        it('should save successfully', function(done) {
            strategyDB.getAllStrategy(function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs)
                    done();
                }
            });
        });
    });

});