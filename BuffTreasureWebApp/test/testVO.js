/**
 * Created by slow_time on 2017/5/9.
 */
var it = require("mocha").it;
var describe = require("mocha").describe;
let StockPoolConditionVO = require("../vo/StockPoolConditionVO").StockPoolConditionVO;
let expect = require('chai').expect;

describe('singleStockDB', function() {
    describe('#getStockInfoByCode()', function() {
        it('should obtain 6396 records', function() {
            let a = new StockPoolConditionVO('沪深300', null, null, true);
            expect(a.stockPool).to.be.equal('沪深300');
        });
    });
});