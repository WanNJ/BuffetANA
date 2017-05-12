/**
 * Created by wshwbluebird on 2017/5/9.
 */

let thermometerbl = require("../../../bl/thermometer/thermometerbl");

let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');

mongoose.connect('mongodb://localhost/formal');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});


describe('#getDailyEnvironment()', function() {
    it('giv one  thermometerbl', function(done) {
        thermometerbl.getDailyEnvironment(new Date("2016-01-21"), (err,data) => {
            if (err) {
                console.log('wrong')
                done(err);
            }
            else {
                console.log(data);
                done();
            }
        });
    });
});

describe('#getEachDayEnvironmentByFormation()', function() {
    it('print the list', function(done) {
        thermometerbl.getEachDayEnvironmentByFormation(new Date("2016-01-01"),new Date("2016-04-01"),3 , (err,data) => {
            if (err) {
                console.log('wrong')
                done(err);
            }
            else {
                console.log(data);
                done();
            }
        });
    });
});

/**
 * 提示  在命令行里输入 测试  mocha -t 300000 thermometer.test.js
 * 必须在命令行里设置  不然两秒钟不够
 */
describe('#WriteDailyEnvironmentRange()', function() {
    it('write to db', function(done) {
        thermometerbl.WriteDailyEnvironmentRange(new Date("2009-01-01"),new Date("2017-04-28"), () => {
                //this.setTimeout(1500);
                console.log('finish');
                done();
        });
    });
});

