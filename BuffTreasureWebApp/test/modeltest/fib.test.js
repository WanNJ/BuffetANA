/**
 * Created by slow_time on 2017/5/14.
 */
describe('singleStockDB', function() {
    describe('#getStockInfoByCode()', function () {
        it('should obtain 6396 records', function () {
            for (let i = 0; i < 1000000; i++) {
                console.log(fib(20));
            }
        });
    });
});

function fib(i) {
    if (i === 0 || i === 1) {
        return 1;
    }
    else {
        return fib(i-1) + fib(i-2);
    }
}