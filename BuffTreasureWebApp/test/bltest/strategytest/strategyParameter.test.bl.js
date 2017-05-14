/**
 * Created by slow_time on 2017/5/14.
 */

let strategyPara = require('../../../bl/strategy/strategyParameter');
let stockDB = require('../../../models/allstock');

let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
let it = require("mocha").it;
let describe = require("mocha").describe;
mongoose.connect('mongodb://localhost/formal');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});



describe('strategyParameter', function() {
    describe('#calculateRSIValue()', function () {
        it('should', function (done) {
            strategyPara.calculateRSIValue('000001', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    // console.log(docs);
                    done();
                }
            });
        });
    });
    describe('#calculateMACDValue()', function () {
        it('should', function (done) {
            strategyPara.calculateMACDValue('000001', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    // console.log(docs);
                    done();
                }
            });
        });
    });
    describe('#calculateMACD_DIFValue()', function () {
        it('should', function (done) {
            strategyPara.calculateMACD_DIFValue('000001', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    // console.log(docs);
                    done();
                }
            });
        });
    });
    describe('#calculateMACD_DEAValue()', function () {
        it('should', function (done) {
            strategyPara.calculateMACD_DEAValue('000001', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    // console.log(docs);
                    done();
                }
            });
        });
    });
    describe('#calculateRSVValue()', function () {
        it('should', function (done) {
            strategyPara.calculateRSVValue('000001', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    // console.log(docs);
                    done();
                }
            });
        });
    });
    describe('#calculateKDJ_KValue()', function () {
        it('should', function (done) {
            strategyPara.calculateKDJ_KValue('000001', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    // console.log(docs);
                    done();
                }
            });
        });
    });
    describe('#calculateKDJ_DValue()', function () {
        it('should', function (done) {
            strategyPara.calculateKDJ_DValue('000001', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    // console.log(docs);
                    done();
                }
            });
        });
    });
    describe('#calculateKDJ_JValue()', function () {
        it('should', function (done) {
            strategyPara.calculateKDJ_JValue('000001', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    // console.log(docs);
                    done();
                }
            });
        });
    });
    describe('#calculateMOMValue()', function () {
        it('all GME', function (done) {

        });
        it('000001', function (done) {
            strategyPara.calculateMOMValue('000001', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000001', function (done) {
            strategyPara.calculateMOMValue('000001', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000002', function (done) {
            strategyPara.calculateMOMValue('000002', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000002', function (done) {
            strategyPara.calculateMOMValue('000002', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000004', function (done) {
            strategyPara.calculateMOMValue('000004', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000004', function (done) {
            strategyPara.calculateMOMValue('000004', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000005', function (done) {
            strategyPara.calculateMOMValue('000005', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000005', function (done) {
            strategyPara.calculateMOMValue('000005', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000006', function (done) {
            strategyPara.calculateMOMValue('000006', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000006', function (done) {
            strategyPara.calculateMOMValue('000006', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000007', function (done) {
            strategyPara.calculateMOMValue('000007', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000007', function (done) {
            strategyPara.calculateMOMValue('000007', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000008', function (done) {
            strategyPara.calculateMOMValue('000008', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000008', function (done) {
            strategyPara.calculateMOMValue('000008', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000009', function (done) {
            strategyPara.calculateMOMValue('000009', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000009', function (done) {
            strategyPara.calculateMOMValue('000009', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000010', function (done) {
            strategyPara.calculateMOMValue('000010', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000010', function (done) {
            strategyPara.calculateMOMValue('000010', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000011', function (done) {
            strategyPara.calculateMOMValue('000011', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000011', function (done) {
            strategyPara.calculateMOMValue('000011', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000012', function (done) {
            strategyPara.calculateMOMValue('000012', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000012', function (done) {
            strategyPara.calculateMOMValue('000012', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000014', function (done) {
            strategyPara.calculateMOMValue('000014', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
        it('000014', function (done) {
            strategyPara.calculateMOMValue('000014', new Date('2015-01-01'), new Date('2017-04-28'), 20, function (err, docs) {
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