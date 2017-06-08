/**
 * Created by wshwbluebird on 2017/6/8.
 */

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
let describe = require("mocha").describe;
let it = require("mocha").it;
mongoose.connect('mongodb://localhost/allInfo');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

let contentDB = require('../../models/content').contentDB;

/*
 * 通过mocha test/modeltest/singleStock.test.js进行测试
 * 代码为000001的股票一共有6396条数据
 */
describe('测试conten', function() {
    describe('#addContent()', function() {
        it('点赞', function(done) {
            contentDB.addContent('000001',"NJUBBE",'不怎么样', function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log('good');
                    done();
                }
            });
        });
    });

});