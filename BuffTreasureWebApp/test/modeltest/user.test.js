/**
 * Created by slow_time on 2017/5/29.
 */
/**
 * Created by wshwbluebird on 2017/5/8.
 */
let user = require('../../models/user').userDB;
let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/allInfo');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

describe('user', function() {
    describe('#getSelfSelectStock', function () {
        it('just a test', function (done) {
            user.getSelfSelectStock('slowtime', function (err, doc) {
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