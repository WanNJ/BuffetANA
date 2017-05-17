/**
 * Created by wshwbluebird on 2017/5/8.
 */
let userbl = require('../../bl/userbl');
let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/formal');

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
    describe('#addToSelfSelectStock()', function() {
        it('just a test', function(done) {
            userbl.addToSelfSelectStock('slowtime',{"stockCode" : "000001", "stockName" : "平安银行"},function (err, doc) {
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
        it('just a test', function(done) {
            userbl.getSelfSelectStock('slowtime', function (err, doc) {
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
});