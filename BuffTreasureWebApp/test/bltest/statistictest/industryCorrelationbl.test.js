/**
 * Created by slow_time on 2017/5/29.
 */
let statistics = require('../../../bl/statistics/industryCorrelationbl');
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

describe('industryCorrelationbl', function() {
    describe('#getIndustryCorrelationResult()', function () {
        it('should show the correlation of 000001', function (done) {
            statistics.getIndustryCorrelationResult('000001', 2,(err, industryCorrelationVO) => {
                if (err) {
                    done(err);
                }
                else {
                    console.log(industryCorrelationVO);
                    done();
                }
            });
        });
    });
});