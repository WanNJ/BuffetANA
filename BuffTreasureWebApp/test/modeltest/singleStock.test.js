/**
 * Created by slow_time on 2017/5/7.
 */
let singleStockDB = require('../../models/singleStock.js').singleStockDB;
let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/formal');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

/*
 * 通过mocha test/modeltest/singleStock.test.js进行测试
 * 代码为000001的股票一共有6396条数据
 */
describe('singleStockDB', function() {


    describe('#getStockInfoInRangeDate()', function() {
        it('1', function(done) {
            singleStockDB.getStockInfoInRangeDate('000001' ,new Date("2016-01-06"), new Date("2016-01-13"), function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
    });
    describe('#getStockInfoInRangeDate()', function() {
        it('1', function(done) {
            singleStockDB.getStockInfoInRangeDate('000002' ,new Date("2016-01-06"), new Date("2016-01-13"), function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
    });
    describe('#getStockInfoInRangeDate()', function() {
        it('1', function(done) {
            singleStockDB.getStockInfoInRangeDate('000004' ,new Date("2016-01-06"), new Date("2016-01-13"), function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
    });
    // describe('#getStockInfoByDate()', function() {
    //     it('should obtain 6396 records', function(done) {
    //         singleStockDB.getStockInfoByDate(new Date('2017-04-28'), function (err, docs) {
    //             if (err) {
    //                 done(err);
    //             }
    //             else {
    //                 console.log(docs);
    //                 done();
    //             }
    //         });
    //     });
    // });
    // describe('#getStocksUntilHavingByDate()', function() {
    //     it('should obtain 6396 records', function(done) {
    //         singleStockDB.getStocksUntilHavingByDate(new Date('2017-01-01'), function (err, docs) {
    //             if (err) {
    //                 done(err);
    //             }
    //             else {
    //                 console.log(docs);
    //                 done();
    //             }
    //         });
    //     });
    // });
});