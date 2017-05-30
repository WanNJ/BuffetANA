/**
 * Created by slow_time on 2017/5/8.
 */
let singleStockbl = require('../../bl/singleStockbl');
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

describe('singleStockbl', function() {
    describe('#getDailyData()', function() {
        it('should show all information of 000001 per day', function(done) {
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
    describe('#getWeeklyData()', function() {
        it('should show all information of 000001 per week', function(done) {
            singleStockbl.getWeeklyData('300122', (err, all_week_data) => {
                if (err) {
                    done(err);
                }
                else {
                    console.log(all_week_data);
                    done();
                }
            });
        });
    });
    describe('#getMonthlyData()', function() {
        it('should show all information of 000001 per month', function(done) {
            singleStockbl.getMonthlyData('300122', (err, all_month_data) => {
                if (err) {
                    done(err);
                }
                else {
                    console.log(all_month_data);
                    done();
                }
            });
        });
    });
    // describe('#getTwoDayInfo()', function() {
    //     it('should show two day information', function(done) {
    //         singleStockbl.getTwoDayInfo(new Date("2017-04-28"), (err, two_month_data) => {
    //             if (err) {
    //                 done(err);
    //             }
    //             else {
    //                 console.log(two_month_data[0].length);
    //                 console.log(two_month_data[1].length);
    //                 done();
    //             }
    //         });
    //     });
    // });
});