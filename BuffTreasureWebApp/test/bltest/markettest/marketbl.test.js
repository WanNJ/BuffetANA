/**
 * Created by slow_time on 2017/6/7.
 */
let marketbl = require('../../../bl/market/marketbl');

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
let describe = require("mocha").describe;
let it = require("mocha").it;
mongoose.connect('mongodb://localhost/allInfo');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

describe('marketbl', function() {
    describe('#getDailyMarketIndex()', function() {
        it('显示沪深300日线指数', function(done) {
            marketbl.getDailyMarketIndex('sh000300', (err, marketIndex) => {
                if (err) {
                    done(err);
                }
                else {
                    console.log(marketIndex);
                    done();
                }
            });
        });
    });
    describe('#getWeeklyMarketIndex()', function() {
        it('显示沪深300周线指数', function(done) {
            marketbl.getWeeklyMarketIndex('sz399300', (err, marketIndex) => {
                if (err) {
                    done(err);
                }
                else {
                    console.log(marketIndex);
                    done();
                }
            });
        });
    });
    describe('#getMonthlyMarketIndex()', function() {
        it('显示上证月线指数', function(done) {
            marketbl.getMonthlyMarketIndex('sh000001', (err, marketIndex) => {
                if (err) {
                    done(err);
                }
                else {
                    console.log(marketIndex);
                    done();
                }
            });
        });
    });
    describe('#getMarketRTInfo()', function() {
        it('显示上证指数实时的信息', function(done) {
            marketbl.getMarketRTInfo('sh000001', (err, marketRTInfo) => {
                if (err) {
                    done(err);
                }
                else {
                    console.log(marketRTInfo);
                    done();
                }
            });
        });
    });
});