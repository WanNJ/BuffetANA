/**
 * Created by wshwbluebird on 2017/6/10.
 */

let forumbl = require('../../../bl/forum/forumbl').forumbl;
let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/allInfo');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

describe('forumbl', function() {
    describe('#getAllStockComment()', function() {
        it('打印000002 号股票的信息', function(done) {
            forumbl.getAllStockComment('000001','NJUty',(err, doc) => {
                if (err) {
                    done(err);
                }
                else {
                    console.log(doc);
                    done();
                }
            });
        });
    });

    describe('#pressStockBad()', function() {
        it('打印000002 号股票的信息', function(done) {
            forumbl.pressStockBad('000004','NJUty',(err, doc) => {
                if (err) {
                    done(err);
                }
                else {

                    done();
                }
            });
        });
    });

    describe('#commentStock()', function() {
        it('评论', function(done) {
            forumbl.commentStock('000002','NJUty','iiiiiiiiii',(err, doc) => {
                if (err) {
                    done(err);
                }
                else {

                    done();
                }
            });
        });
    });

});