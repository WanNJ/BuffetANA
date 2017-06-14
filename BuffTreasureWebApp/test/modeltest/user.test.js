/**
 * Created by slow_time on 2017/5/29.
 */
/**
 * Created by wshwbluebird on 2017/5/8.
 */
let user = require('../../models/user').userDB;
let expect = require('chai').expect;

// 数据库连接 MongoDB
// MongoDB
let mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/latestInfo');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

describe('user', function() {
    describe('#getSelfSelectStock', function () {
        it('just a test', function (done) {
            user.getSelfSelectStock('slowtime', function (err, doc) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(doc);
                    done();
                }
            });
        });
    });
    describe('#addUnreadMessage', function () {
        it('just a thumbs_up test', function (done) {
            let message = {
                time: new Date(),
                isRead: false,
                type: 'thumbs_up',
                codeOrName: '000001',
                stockName: '平安银行',
                content: {}
            };
            user.addUnreadMessage('wnj', message, function (err, isOK) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(isOK);
                    done();
                }
            });
        });
        it('just a analysis1 test', function (done) {
            let message = {
                time: new Date(),
                isRead: false,
                type: 'analysis1',
                codeOrName: '你的代码为000001的股票分析结果已出，请点击查看',
                content: {
                    "CR": [0.29, '低'],
                    "upOrDown": ['54.29', '1'],
                    "5-7_5": 0.0002819275832735002,
                    "0-2_5": 0.00008666887879371643,
                    "7_5-5": 0.12415958195924759,
                    "7_5-10": 0.00038002902874723077,
                    "10-7_5": 0.3716028332710266,
                    "more10": 0.012712066993117332,
                    "5-2_5": 0.047767940908670425,
                    "2_5-5": 0.00011517480743350461,
                    "2_5-0": 0.003162472043186426,
                    "less10": 0.439731240272522
                    }
            };
            user.addUnreadMessage('ty', message, function (err, isOK) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(isOK);
                    done();
                }
            });
        });
        it('just a analysis2 test', function (done) {
            let message = {
                time: new Date(),
                isRead: false,
                type: 'analysis2',
                codeOrName: '你的代码为000002的股票分析结果已出，请点击查看',
                content: {
                    "CR": [0.29, '低'],
                    "upOrDown": ['54.29', '1'],
                    "process":
                        [ 0.019999999552965164,
                            0.30000001192092896,
                            0.30000001192092896,
                            0.2800000011920929,
                            0.30000001192092896,
                            0.30000001192092896,
                            0.30000001192092896,
                            0.27000001072883606,
                            0.14000000059604645,
                            0.14000000059604645,
                            0.009999999776482582,
                            0.019999999552965164,
                            0.019999999552965164,
                            0.09000000357627869,
                            0.28999999165534973,
                            0.3100000023841858,
                            0.28999999165534973,
                            0.28999999165534973,
                            0.30000001192092896,
                            0.3100000023841858,
                            0.4099999964237213,
                            0.5199999809265137,
                            0.5199999809265137,
                            0.5699999928474426,
                            0.5600000023841858,
                            0.5699999928474426,
                            0.6100000143051147,
                            0.6100000143051147,
                            0.5799999833106995,
                            0.6600000262260437,
                            0.7099999785423279,
                            0.6899999976158142,
                            0.7200000286102295,
                            0.7400000095367432,
                            0.7200000286102295,
                            0.6600000262260437,
                            0.7599999904632568,
                            0.7900000214576721,
                            0.7900000214576721,
                            0.7900000214576721 ],
                    "5-7_5": 0.0002819275832735002,
                    "0-2_5": 0.00008666887879371643,
                    "7_5-5": 0.12415958195924759,
                    "accuracy": 0.7900000214576721,
                    "7_5-10": 0.00038002902874723077,
                    "10-7_5": 0.3716028332710266,
                    "more10": 0.012712066993117332,
                    "5-2_5": 0.047767940908670425,
                    "2_5-5": 0.00011517480743350461,
                    "2_5-0": 0.003162472043186426,
                    "less10": 0.439731240272522
                }
            };
            user.addUnreadMessage('ty', message, function (err, isOK) {
                if (err) {
                    done(err);
                }
                else {
                    console.log(isOK);
                    done();
                }
            });
        });
    });
});