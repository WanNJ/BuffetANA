/**
 * Created by slow_time on 2017/5/9.
 */
let allStockDB = require('../../models/allStock').allStockDB;
let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
let it = require("mocha").it;
let describe = require("mocha").describe;
mongoose.connect('mongodb://localhost/formal');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

describe('allStockDB', function() {
    describe('#getStocksByBench()', function() {
        it('should obtain 299 records', function(done) {
            allStockDB.getStocksByBench(['沪深300'], function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    docs.forEach(doc => {
                        console.log(doc['code']);
                    });
                    expect(docs.length).to.be.equal(299);
                    done();
                }
            });
        });
    });
    describe('#getHS300StockCodeAndName()', function() {
        it('should obtain 299 records', function(done) {
            allStockDB.getHS300StockCodeAndName(function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    docs.forEach(doc => {
                        console.log(doc['code']);
                    });
                    expect(docs.length).to.be.equal(299);
                    done();
                }
            });
        });
    });
    describe('#getHSAStockCodeAndName()', function() {
        it('should all be A', function(done) {
            allStockDB.getHSAStockCodeAndName(function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    docs.forEach(doc => {
                        console.log(doc['code']);
                    });
                    done();
                }
            });
        });
    });
    describe('#getHSAStockCodeAndName()', function() {
        it('should all be A', function(done) {
            allStockDB.getHSAStockCodeAndName(function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    docs.forEach(doc => {
                        console.log(doc['code']);
                    });
                    done();
                }
            });
        });
    });
    describe('#getSMEBoardCodeAndName()', function() {
        it('should all be SME', function(done) {
            allStockDB.getSMEBoardCodeAndName(function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    docs.forEach(doc => {
                        console.log(doc['code']);
                    });
                    console.log(docs.length);
                    done();
                }
            });
        });
    });
    describe('#getSMEBoardCodeAndName()', function() {
        it('should all be SME', function(done) {
            allStockDB.getSMEBoardCodeAndName(function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    docs.forEach(doc => {
                        console.log(doc['code']);
                    });
                    console.log(docs.length);
                    done();
                }
            });
        });
    });
    describe('#getGEMBoardStockCodeAndName()', function() {
        it('should all be GEM', function(done) {
            allStockDB.getGEMBoardStockCodeAndName(function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    docs.forEach(doc => {
                        console.log(doc['code']);
                    });
                    console.log(docs.length);
                    done();
                }
            });
        });
    });
    describe('#getGEMBoardStockCodeAndName()', function() {
        it('should all be GEM', function(done) {
            allStockDB.getGEMBoardStockCodeAndName(function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    docs.forEach(doc => {
                        console.log(doc['code']);
                    });
                    console.log(docs.length);
                    done();
                }
            });
        });
    });
    describe('#getDIYStockPool()', function() {
        it('should all be DIY', function(done) {
            allStockDB.getDIYStockPool(['银行', '房地产', '电子元器件'], ['广东', '深投系', '银行'], function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    docs.forEach(doc => {
                        console.log(doc['code']);
                    });
                    console.log(docs.length);
                    done();
                }
            });
        });
    });
    describe('#getStocksByBench()', function() {
        it('should all be DIY', function(done) {
            allStockDB.getStocksByBench(['银行', '房地产', '电子元器件'], function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    docs.forEach(doc => {
                        console.log(doc['code']);
                    });
                    console.log(docs.length);
                    done();
                }
            });
        });
    });
});