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

let forumDB = require('../../models/forum').forumDB;

/*
 * 通过mocha test/modeltest/singleStock.test.js进行测试
 * 代码为000001的股票一共有6396条数据
 */
describe('pressGood', function() {
    describe('#pressGood()', function() {
        it('点赞', function(done) {
            forumDB.pressGood('000001',"NJUBBE", function (err, docs) {
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
        it('点赞', function(done) {
            forumDB.pressBad('000002',"NJUrr", function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log('bad');
                    done();
                }
            });
        });
    });

    describe('#getForumInfo()', function() {
        it('获取', function(done) {
            forumDB.getForumInfo('000002',"NJUrt", function (err, docs) {
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