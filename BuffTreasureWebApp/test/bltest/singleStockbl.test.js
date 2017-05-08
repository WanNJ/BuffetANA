/**
 * Created by slow_time on 2017/5/8.
 */
let singleStockbl = require('../../bl/singleStockbl');
let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/formal');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

describe('singleStockbl', function() {
    describe('#getDailyData()', function() {
        it('should show all information of 000001', function(done) {
            singleStockbl.getDailyData('000001', (err, all_day_data) => {
                if (err) {
                    done(err);
                }
                else {
                    console.log(all_day_data);
                    done();
                }
            });
        });
    });
});