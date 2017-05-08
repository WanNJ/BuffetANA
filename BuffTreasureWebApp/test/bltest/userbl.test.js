/**
 * Created by wshwbluebird on 2017/5/8.
 */
import {describe, it} from "mocha";
let userbl = require('../../bl/userbl');
let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
var assert = require("assert");
mongoose.connect('mongodb://localhost/formal');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});


/*
 *  add by wsw
 *  目前这个测试  只能我自己跑  需要设计所有人都能跑的测试方法
 *  仿照田原的代码对 userbl进行测试
 *  结果仿照断言
 */
describe('userbl', function() {
    describe('#login()', function() {
        it('just a test', function(done) {
            userbl.login('wsw15','123',function (err, doc) {
                if (err) {
                    done(err);
                }
                else {
                    //使用断言
                    assert.equal(doc,true,'反应不正确');
                    console.log("correct")
                    done();
                }
            });
        });
    });
});