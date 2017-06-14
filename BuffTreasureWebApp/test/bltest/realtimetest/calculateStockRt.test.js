/**
 * Created by wshwbluebird on 2017/6/10.
 */
let calculateStockRt = require('../../../bl/realtime/strategyRT');
let it = require("mocha").it;
let describe = require("mocha").describe;
let mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/latestInfo');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

describe('calculateRT', function() {
    describe('#calculateStockRt()', function() {
        it('打印计算出来的股票推荐', function(done) {
            calculateStockRt.calculateStockRt((err, docs) => {
                if (err) {
                    done(err);
                }
                else {
                    console.log(docs);
                    done();
                }
            });
        });
    });

    describe('#WriteRTStockToDB()', function() {
        it('保存推荐的股票', function(done) {
            calculateStockRt.WriteRTStockToDB((err) => {
                if (err) {
                    done(err);
                }
                else {
                    console.log('finish');
                    done();
                }
            });
        });
    });

});