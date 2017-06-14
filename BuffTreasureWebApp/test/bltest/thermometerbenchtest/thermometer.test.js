/**
 * Created by wshwbluebird on 2017/5/9.
 */

let thermometerbl = require("../../../bl/thermometer/thermometerbl");

let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');

mongoose.connect('mongodb://localhost/allInfo');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});


// describe('#getDailyEnvironment()', function() {
//     it('giv one  thermometerbl', function(done) {
//         thermometerbl.getDailyEnvironment(new Date("2016-05-10"), (err,data) => {
//             if (err) {
//                 console.log('wrong')
//                 done(err);
//             }
//             else {
//                 console.log(data);
//                 done();
//             }
//         });
//     });
// });

// describe('#getEachDayEnvironmentByFormation()', function() {
//     it('print the list', function(done) {
//         thermometerbl.getEachDayEnvironmentByFormation(new Date("2010-01-01"),new Date("2016-04-01"),10 , (err,data) => {
//             if (err) {
//                 console.log('wrong')
//                 done(err);
//             }
//             else {
//                 console.log(data);
//                 done();
//             }
//         });
//     });
// });

/**
 * 这个是 跑市场温度的!!!!!!!!
 *
 * 提示  在命令行里输入 测试  mocha -t 300000 thermometer.test.js
 * 必须在命令行里设置  不然两秒钟不够
 */
describe('#WriteDailyEnvironmentRange()', function() {
    it('write to db', function(done) {
        thermometerbl.WriteDailyEnvironmentRange(new Date("2005-01-01"),new Date("2017-05-10"), () => {
                //this.setTimeout(1500);
                console.log('finish');
                done();
        });
    });
});

