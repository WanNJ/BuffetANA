/**
 * Created by wshwbluebird on 2017/5/8.
 */
let userbl = require('../../bl/userbl');
let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost/latestInfo');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});


describe('userbl', function() {
    describe('#signUp()', function() {
        it('just a test', function(done) {
            userbl.signUp('ty','123456', '151250134@smail.nju.edu.cn',function (err, doc) {
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
    describe('#login()', function() {
        it('just a test', function(done) {
            userbl.login('时间很慢','123456',function (err, doc) {
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
            userbl.addToSelfSelectStock('ty',{ "stockCode" : "000001", "stockName" : "平安银行" },function (err, doc) {
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
            strategy["strategyName"] = "test_strategy1";
            strategy["overwrite"] = true;
            strategy["beginDate"] = new Date('2015-01-01');
            strategy["endDate"] = new Date('2017-03-17');
            strategy["stockPoolCondition"] = {
                "stockPool": "沪深300",
                "benches": [],
                "industries": [],
                "excludeST": false
            };
            strategy["rank"] = {
                "r1" : ["MA", "asc", 10, 1],
                "r2" : ["MOM", "desc", 9, 1]
                // ,"RSI" : ["asd", 10, 1],
                // "KDJ_K" : ["asd", 10, 1],
                // "KDJ_D" : ["asd", 10, 1]
            };
            strategy["filter"] = {
                "f1" : ["turnOverRate", '<', 1]
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
            userbl.saveStrategy('ty', strategy, function (err, doc) {
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
    describe('#loadStrategy()', function() {
        it('just a test1', function(done) {
            userbl.loadStrategy('slowtime', 'test_strategy', function (err, doc) {
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
    describe('#getAllMessages()', function() {
        it('just a te', function(done) {
            userbl.getAllMessages('wsw', function (err, doc1, doc2) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(doc1);
                    console.log(doc2);
                    done();
                }
            });
        });
    });
    describe('#getUnreadMessageCount()', function() {
        it('获得ty用户的所有未读消息', function(done) {
            userbl.getUnreadMessageCount('wnj', function (err, count) {
                if (err) {
                    done(err);
                }
                else {
                    expect(count).to.be.equal(5);
                    console.log(count);
                    done();
                }
            });
        });
    });
    describe('#markAsRead()', function() {
        it('将ty用户的2017-06-10T02:43:55.408Z这一时间的未读消息标记为已读', function(done) {
            userbl.markAsRead('wnj', new Date('2017-06-12T14:19:42.022Z'), function (err) {
                if (err) {
                    done(err);
                }
                else {
                    done();
                }
            });
        });
    });
    describe('#getOneMessageContent()', function() {
        it('将ty用户的2017-06-10T02:43:55.408Z这一时间的未读消息标记为已读', function(done) {
            userbl.getOneMessageContent('wnj', new Date('2017-06-12T14:23:09.574Z'), function (err, message) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(message);
                    done();
                }
            });
        });
    });
});