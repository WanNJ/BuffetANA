/**
 * Created by wshwbluebird on 2017/5/9.
 */

let thermometerbl = require("../../../bl/thermometer/thermometer");

let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/formal');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});


describe('#getDailyEnvironment()', function() {
    it('should show all industries', function(done) {
        thermometerbl.getDailyEnvironment(new Date("2016-01-06"), (err,doc) => {
            if (err) {
                console.log("not in test")
                done(err);
            }
            else {
                console.log("finish");
                done();
            }
        });
    });
});