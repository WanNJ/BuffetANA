/**
 * Created by wshwbluebird on 2017/6/5.
 */
/**
 * Created by slow_time on 2017/5/29.
 */
let predict = require('../../../../bl/statistics/ML/testCNNModel').CNNPredict;
let expect = require('chai').expect;


describe('betterPredict', function() {
    describe('#betterPredict()', function () {
        it('should show the result of 00000t NN predicr', function (done) {
            predict.betterPredict('000001', 1,4,false ,'2006-04-05','batch',(err, result) => {
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