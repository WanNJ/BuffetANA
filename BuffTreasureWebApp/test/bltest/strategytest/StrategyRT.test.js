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
