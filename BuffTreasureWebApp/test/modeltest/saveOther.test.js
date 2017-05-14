/**
 * Created by slow_time on 2017/5/14.
 */
let strategyDB = require('../../models/savedb/other');
let allStockDB = require('../../models/allstock').allStockDB;
let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
// let it = require("mocha").it;
// let describe = require("mocha").describe;
mongoose.connect('mongodb://localhost/formal');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

describe('saveOtherDB', function() {
    describe('#saveOtherDB()', function () {
        it('should obtain 299 records', function (done) {
            strategyDB.saveOtherDB(function (err, doc) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(doc);
                    done();
                }
            });
            // allStockDB.getAllStockCodeAndName((err, docs) => {
            //     docs.forEach(doc => {
            //         console.log(doc["code"]);
            //
            //     });
            //     done();
            //
            // });
        });
    });
});