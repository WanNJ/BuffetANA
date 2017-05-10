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
    it('should show all industries', function(done) {
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