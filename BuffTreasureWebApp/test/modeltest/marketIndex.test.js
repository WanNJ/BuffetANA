/**
 * Created by slow_time on 2017/6/7.
 */
/**
 * Created by slow_time on 2017/5/7.
 */
let singleStockDB = require('../../models/singleStock.js').singleStockDB;
let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
let describe = require("mocha").describe;
let it = require("mocha").it;
mongoose.connect('mongodb://localhost/allInfo');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

let marketIndex = require('../../models/marketIndex').marketIndexDB;

/*
 * 通过mocha test/modeltest/singleStock.test.js进行测试
 * 代码为000001的股票一共有6396条数据
 */
describe('marketIndex', function() {
    describe('#getMarketIndexByCode()', function() {
        it('显示上证指数', function(done) {
            marketIndex.getMarketIndexByCode('sh000001', function (err, docs) {
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
});