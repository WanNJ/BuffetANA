/**
 * Created by slow_time on 2017/6/3.
 */
/**
 * Created by slow_time on 2017/5/29.
 */
let statistics = require('../../../bl/statistics/singleStockPredict');
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

describe('singleStockPredict', function() {
    describe('#SVMAnalyze()', function () {
        it('SVM', function (done) {
            statistics.SVMAnalyze('wnj', '000001', '平安银行', 8.78, 5, new Date(), (err, isOK) => {
                if (err)
                    done(err);
                else {
                    console.log(isOK);
                    done();
                }
            });
        });
        it('CNN', function (done) {
            statistics.CNNAnalyze('wnj', '000001', '平安银行', 1, false, 4, 'ALL', new Date(), (err, isOK) => {
                if (err)
                    done(err);
                else {
                    console.log(isOK);
                    done();
                }
            });
        });
        it('NN', function (done) {
            statistics.NNAnalyze('wnj', '000002', '万科 A', 5, new Date(), (err, isOK) => {
                if (err)
                    done(err);
                else {
                    console.log(isOK);
                    done();
                }
            });
        });
        // it('should show the correlation of 000001', function (done) {
        //     statistics.getCoefficientOfRisk('000001', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        //
        // });
        // it('should show the correlation of 000002', function (done) {
        //     statistics.getCoefficientOfRisk('000002', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        //
        // });
        // it('should show the correlation of 000004', function (done) {
        //     statistics.getCoefficientOfRisk('000004', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        //
        // });
        // it('should show the correlation of 000005', function (done) {
        //     statistics.getCoefficientOfRisk('000005', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        //
        // });
        // it('should show the correlation of 000006', function (done) {
        //     statistics.getCoefficientOfRisk('000006', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000007', function (done) {
        //     statistics.getCoefficientOfRisk('000007', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000008', function (done) {
        //     statistics.getCoefficientOfRisk('000008', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000009', function (done) {
        //     statistics.getCoefficientOfRisk('000009', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000010', function (done) {
        //     statistics.getCoefficientOfRisk('000010', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000011', function (done) {
        //     statistics.getCoefficientOfRisk('000011', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000012', function (done) {
        //     statistics.getCoefficientOfRisk('000012', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000014', function (done) {
        //     statistics.getCoefficientOfRisk('000014', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000016', function (done) {
        //     statistics.getCoefficientOfRisk('000016', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000017', function (done) {
        //     statistics.getCoefficientOfRisk('000017', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000018', function (done) {
        //     statistics.getCoefficientOfRisk('000018', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000019', function (done) {
        //     statistics.getCoefficientOfRisk('000019', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000020', function (done) {
        //     statistics.getCoefficientOfRisk('000020', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000021', function (done) {
        //     statistics.getCoefficientOfRisk('000021', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000022', function (done) {
        //     statistics.getCoefficientOfRisk('000022', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000023', function (done) {
        //     statistics.getCoefficientOfRisk('000023', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000025', function (done) {
        //     statistics.getCoefficientOfRisk('000025', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000026', function (done) {
        //     statistics.getCoefficientOfRisk('000026', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000027', function (done) {
        //     statistics.getCoefficientOfRisk('000027', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000028', function (done) {
        //     statistics.getCoefficientOfRisk('000028', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000029', function (done) {
        //     statistics.getCoefficientOfRisk('000029', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000030', function (done) {
        //     statistics.getCoefficientOfRisk('000030', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000031', function (done) {
        //     statistics.getCoefficientOfRisk('000031', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000032', function (done) {
        //     statistics.getCoefficientOfRisk('000032', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000033', function (done) {
        //     statistics.getCoefficientOfRisk('000033', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000034', function (done) {
        //     statistics.getCoefficientOfRisk('000034', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000035', function (done) {
        //     statistics.getCoefficientOfRisk('000035', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000036', function (done) {
        //     statistics.getCoefficientOfRisk('000036', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000037', function (done) {
        //     statistics.getCoefficientOfRisk('000037', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000038', function (done) {
        //     statistics.getCoefficientOfRisk('000038', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000039', function (done) {
        //     statistics.getCoefficientOfRisk('000039', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
        // it('should show the correlation of 000040', function (done) {
        //     statistics.getCoefficientOfRisk('000040', (err, CV) => {
        //         if (err) {
        //             done(err);
        //         }
        //         else {
        //             console.log(CV);
        //             done();
        //         }
        //     });
        // });
    });
});