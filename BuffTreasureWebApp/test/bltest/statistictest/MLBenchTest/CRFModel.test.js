/**
 * Created by wshwbluebird on 2017/6/5.
 */
/**
 * Created by slow_time on 2017/5/29.
 */
let predict = require('../../../../bl/statistics/ML/testRFCModel').RFCPredict;
let expect = require('chai').expect;


describe('rfcPredict', function() {
    describe('#betterPredict()', function () {
        it('should show the result of 000001 NN predicr', function (done) {
            predict.rfcPredict('000001', 5,(err, result) => {
                if (err) {
                    done(err);
                }
                else {
                    console.log(result);
                    done();
                }
            });
        });
    });
});