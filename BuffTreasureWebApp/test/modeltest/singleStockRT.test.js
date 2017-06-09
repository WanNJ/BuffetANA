/**
 * Created by wshwbluebird on 2017/6/9.
 */
/**
 * Created by wshwbluebird on 2017/5/28.
 */
let RTDB = require('../../models/stockRTInfo').stockRTInfoDB;

let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
let it = require("mocha").it;
let describe = require("mocha").describe;
mongoose.connect('mongodb://localhost/allInfo');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});


describe('RTDB', function() {

    describe('#getAllRTInfo()', function() {
        it('打印当前所有的实时数据', function(done) {
            RTDB.getAllRTInfo(function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs)
                    done();
                }
            });
        });
    });

});