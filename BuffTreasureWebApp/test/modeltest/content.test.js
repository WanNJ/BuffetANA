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
        it('增加评论', function(done) {
            contentDB.addContent('000001',"NJUBBD",'不怎么样02', function (err, docs) {
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

    describe('#pressGood()', function() {
        it('点赞', function(done) {
            contentDB.pressGood('5938efdddf3ab9029c1b4e44',"NJUBBE", function (err, docs) {
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

    describe('#pressBad()', function() {
        it('点差', function(done) {
            contentDB.pressBad('5938efdddf3ab9029c1b4e44',"NJUBBE", function (err, docs) {
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