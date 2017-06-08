/**
 * Created by wshwbluebird on 2017/5/8.
 */
let userbl = require('../../bl/userbl');
let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/allInfo');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});


describe('userbl', function() {
    describe('#signUp()', function() {
        it('just a test', function(done) {
            userbl.signUp('slowtime','123456', '151250134@smail.nju.edu.cn',function (err, doc) {
                if (err) {
                    done(err);
                }
                else {
                    //使用断言
                    console.log(doc);
                    done();
                }
            });
        });
    });
    describe('#addToSelfSelectStock1()', function() {
        it('just a test', function(done) {
            userbl.addToSelfSelectStock('slowtime',{ "stockCode" : "300002", "stockName" : "神州泰岳" },function (err, doc) {
                if (err) {
                    done(err);

                }
                else {
                    if (err)
                        console.log(err);
                    else
                        console.log(doc);
                    done();
                }
            });
        });
    });
    describe('#getSelfSelectStock()', function() {
        it('just a test1', function(done) {
            userbl.getSelfSelectStock('slowtime', function (err, doc) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(doc);
                    done();
                }
            });
        });
    });
    describe('#saveStrategy()', function() {
        it('save a strategy', function(done) {
            let strategy = {};
            strategy["strategyName"] = "test_strategy2";
            strategy["beginDate"] = new Date('2015-01-01');
            strategy["endDate"] = new Date('2017-03-17');
            strategy["stockPoolCondition"] = {
                "stockPool": "沪深300",
                "benches": [],
                "industries": [],
                "excludeST": false
            };
            strategy["rank"] = {
                "MA" : ["asc", 10, 1],
                "MOM" : ["desc", 9, 1]
                // ,"RSI" : ["asd", 10, 1],
                // "KDJ_K" : ["asd", 10, 1],
                // "KDJ_D" : ["asd", 10, 1]
            };
            strategy["filter"] = {
                "turnOverRate" : ['<', 1]
            };
            strategy["tradeModel"] = {
                "holdingDays": 2,
                "holdingNums": 2

            };
            strategy["envSpecDay"] = 10;
            strategy["markNormal"] = 76;
            strategy["markHS"] = 30;
            strategy["markHO"] = 40;
            strategy["markLS"] = 80;
            strategy["markLO"] = 60;
            userbl.saveStrategy('slowtime', strategy, function (err, doc) {
                if (err) {
                    done(err);
                }
                else {
                    if (err)
                        console.log(err);
                    else
                        console.log(doc);
                    done();
                }
            });
        });
    });
    describe('#getAllStrategy()', function() {
        it('just a test1', function(done) {
            userbl.getAllStrategy('slowtime', function (err, doc) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(doc);
                    done();
                }
            });
        });
    });
});