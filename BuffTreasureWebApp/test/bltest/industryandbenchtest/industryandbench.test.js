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
                console.log("sdf");
                if (err) {
                    done(err);
                }
                else {
                    console.log(all_industry);
                    console.log(all_industry.size);
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
                    console.log(all_bench.size);
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
    describe('#getBoardsByCode()', function() {
        it('000001 的板块是"bench" : ["深投系", "海通系", "沪深300", "大盘", "转融券", "中证100", "汇金持股", "低市净率", "股权投资", "深成500", "广东自贸区", "操盘手80", "深证100", "深股通", "破净股", "银行", "金融、保险业", "广东", "银行", "证金持股", "融资融券"]', function(done) {
            industrybl.getBoardsByCode('000001', (err, benches) => {
                if (err) {
                    done(err);
                }
                else {
                    console.log(benches);
                    let test = ["深投系", "海通系", "沪深300", "大盘", "转融券", "中证100", "汇金持股", "低市净率", "股权投资", "深成500", "广东自贸区", "操盘手80", "深证100", "深股通", "破净股", "银行", "金融、保险业", "广东", "银行", "证金持股", "融资融券"];
                    for (let i = 0; i < test.length; i++) {
                        expect(benches[i]).to.be.equal(test[i]);
                    }
                    done();
                }
            });
        });
    });
});