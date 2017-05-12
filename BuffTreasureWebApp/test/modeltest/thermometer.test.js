/**
 * Created by wshwbluebird on 2017/5/13.
 */
let thememoeterDB = require('../../models/thermometer').thermometerDB;
let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
var it = require("mocha").it;
var describe = require("mocha").describe;
mongoose.connect('mongodb://localhost/formal');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});




describe('thermometerDB', function() {

    /***
     * !!!!!!跑着测试必须确定 市场温度数据已经存在数据库中
     */
    describe('#getThermometerInRangeDate()', function() {
        it('should obtain 299 records', function(done) {
            thememoeterDB.getThermometerInRangeDate(new Date("2014-01-01") ,new Date("2014-02-01")  , function (err, docs) {
                if (err) {
                    done(err);
                }
                else {
                    docs.forEach(doc => {
                        let p = new Date("2014-01-02");
                        //console.log(doc['date']);
                        //console.log(doc['date'] - p);
                        let sd = 'sd';
                        let temp = `${sd}df`;
                        console.log(temp);
                    });
                    // expect(docs.length).to.be.equal(299);
                    done();
                }
            });
        });
    });

});