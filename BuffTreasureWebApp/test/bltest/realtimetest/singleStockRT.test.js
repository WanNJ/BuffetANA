/**
 * Created by slow_time on 2017/6/6.
 */
let realTimeTool = require('../../../bl/realtime/singleStockRT');
let it = require("mocha").it;
let describe = require("mocha").describe;

describe('singleStockRT', function() {
    describe('#obtainRTInfoByCode()', function() {
        it('should show the RTInfo Of 600171', function(done) {
            realTimeTool.obtainRTInfoByCode('600171', (err, RTInfo) => {
                if (err) {
                    done(err);
                }
                else {
                    console.log(RTInfo);
                    done();
                }
            });
        });
    });
});