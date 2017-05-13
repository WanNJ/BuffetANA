/**
 * Created by wshwbluebird on 2017/5/13.
 */

let strategyToolbl = require("../../../bl/strategy/strategyTool");

let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');

mongoose.connect('mongodb://localhost/formal');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});


describe('#divideDaysByThermometer()', function() {
    it('temp', function(done) {
        strategyToolbl.divideDaysByThermometer(new Date("2014-01-01"),new Date("2016-02-01"),5,5, (err,data) => {
            if (err) {
                console.log('wrong')
                done(err);
            }
            else {
                console.log('Normal:  '+ data['Normal'].length);
                console.log('HighAndSame:  '+ data['HighAndSame'].length);
                console.log('LowAndSame:  '+ data['LowAndSame'].length);
                console.log('HighAndOpposite:  '+ data['HighAndOpposite'].length);
                console.log('LowAndOpposite:  '+ data['LowAndOpposite'].length);
                done();
            }
        });
    });
});



