/**
 * Created by wshwbluebird on 2017/6/5.
 */
/**
 * Created by slow_time on 2017/5/29.
 */
let predict = require('../../../../bl/statistics/ML/testEasyModel').NNPredict;
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

describe('testEasyModelbl', function() {
    describe('#EasyPredict()', function () {
        it('should show the result of 00000t NN predicr', function (done) {
            predict.EasyPredict('000001', 10,(err, result) => {
                if (err) {
                    done(err);
                }
                else {
                    console.log(result);
                    done();
                }
            });
        });
    });
});