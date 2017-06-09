/**
 * Created by wshwbluebird on 2017/6/9.
 */

let thermoeterBL = require('../../../bl/realtime/thememometerRT');
let it = require("mocha").it;
let describe = require("mocha").describe;
let mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/allInfo');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

describe('thermoeterBL', function() {
    describe('#getCurrentThermometor()', function() {
        it('打印当前的市场温度环境', function(done) {
            thermoeterBL.getCurrentThermometor( (err, docs) => {
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
});