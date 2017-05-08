/**
 * Created by slow_time on 2017/5/8.
 */
let industrybl = require('../../../bl/industryandbench/industrybl');
let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/formal');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

describe('industrybl', function() {
    describe('#getAllIndustries()', function() {
        it('should show all industries', function(done) {
            industrybl.getAllIndustries((err, all_industry) => {
                if (err) {
                    done(err);
                }
                else {
                    console.log(all_industry);
                    done();
                }
            });
        });
    });
    describe('#getAllBoards()', function() {
        it('should show all benches', function(done) {
            industrybl.getAllBoards((err, all_bench) => {
                if (err) {
                    done(err);
                }
                else {
                    console.log(all_bench);
                    done();
                }
            });
        });
    });
    describe('#getIndustryByCode()', function() {
        it('000001 的行业是银行', function(done) {
            industrybl.getIndustryByCode('000001', (err, industry) => {
                if (err) {
                    done(err);
                }
                else {
                    expect(industry).to.be.equal('银行');
                    done();
                }
            });
        });
    });
});