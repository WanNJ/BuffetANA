/**
 * Created by wshwbluebird on 2017/6/10.
 */



let strategyRT = require("../../../bl/strategy/strategyRT");



let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/allInfo');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

describe('#getRccomandStockHighScore()', function() {
    it('获得高分的股票', function(done) {

        strategyRT.getRccomandStockHighScore((err,data) => {
                if (err) {
                    console.log('wrong');
                    done(err);
                }
                else {
                    //console.log();
                    console.log(data)
                    done();
                }
            });
    });



});

describe('#getRccomandStockAntiRiskAbility()', function() {
    it('获得抗风险高分的股票', function(done) {

        strategyRT.getRccomandStockAntiRiskAbility((err,data) => {
            if (err) {
                console.log('wrong');
                done(err);
            }
            else {
                //console.log();
                console.log(data)
                done();
            }
        });
    });



});

describe('#getRccomandStockProfit()', function() {
    it('获得收益率高分的股票', function(done) {

        strategyRT.getRccomandStockProfit((err,data) => {
            if (err) {
                console.log('wrong');
                done(err);
            }
            else {
                //console.log();
                console.log(data)
                done();
            }
        });
    });



});

describe('#getRccomandStockWinRate()', function() {
    it('选股能力', function(done) {

        strategyRT.getRccomandStockWinRate((err,data) => {
            if (err) {
                console.log('wrong');
                done(err);
            }
            else {
                //console.log();
                console.log(data)
                done();
            }
        });
    });



});

describe('#getRtStrategyALL()', function() {
    it('选择策略', function(done) {

        strategyRT.getRtStrategyALL((err,data) => {
            if (err) {
                console.log('wrong');
                done(err);
            }
            else {
                //console.log();
                console.log(data)
                done();
            }
        });
    });



});



