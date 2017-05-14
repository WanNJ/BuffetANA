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

let StockPoolConditionVO = require("../../../vo/StockPoolConditionVO").StockPoolConditionVO;

describe('#divideDaysByThermometer()', function() {
    it('temp', function(done) {
        strategyToolbl.divideDaysByThermometer(new Date("2015-01-01"),new Date("2017-02-01"),5,5, (err,data) => {
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





describe('#getChoosedStockList()', function() {
    it('应该正常执行 打印选中的股票列表', function(done) {
        strategyToolbl.getChoosedStockList(new StockPoolConditionVO("自选股票池",['asd'],['asd'],true), (err,data) => {
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

    it('应该正常执行 打印选中的股票列表2', function(done) {
        strategyToolbl.getChoosedStockList(new StockPoolConditionVO("随机500",[],[],true), (err,data) => {
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

    it('应该抛出异常 wrong1', function(done) {
        strategyToolbl.getChoosedStockList(['abc'], (err,data) => {
            if (err) {
                console.log('wrong1')
                done();
            }
            else {
                done();
            }
        });
    });

    it('应该抛出异常 wrong2', function(done) {
        strategyToolbl.getChoosedStockList(new StockPoolConditionVO("xxxxxx0",[],[],false), (err,data) => {
            if (err) {
                console.log('wrong2')
                done();
            }
            else {
                console.log(data);
                done();
            }
        });
    });
});

