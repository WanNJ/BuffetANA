/**
 * Created by slow_time on 2017/5/7.
 */
let allStockbl = require('../../bl/allStockbl');
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
describe('allStockbl', function() {
    describe('#getAllStockCodeAndName()', function() {
        it('should show all stocks', function(done) {
            allStockbl.getAllStockCodeAndName(function (err, allstocks) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(allstocks);
                    done();
                }
            });
        });
    });
});