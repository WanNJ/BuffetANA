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

});