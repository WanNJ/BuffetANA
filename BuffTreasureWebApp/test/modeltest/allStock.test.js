/**
 * Created by slow_time on 2017/5/9.
 */
let allStockDB = require('../../models/allStock').allStockDB;
let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
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
});