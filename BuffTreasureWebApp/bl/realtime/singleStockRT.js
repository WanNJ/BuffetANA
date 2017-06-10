/**
 * Created by slow_time on 2017/6/6.
 */
const request = require("superagent");
const allStockDB = require('../../models/allstock').allStockDB;
const stockRTDB = require('../../models/stockRTInfo').stockRTInfoDB;
const exec = require('child_process').exec;

/**
 * 获得个股实时的信息
 * @param code
 * @param callback (err, stockRTInfo) => {}
 *
 * ！！！！！！！！！！！！！注意括号中的单位，没有提到单位的属性，就是不用加单位！！！！！！！！！！！！！！！
 *
 * stockRTInfo形如
 * {
                    "now_price": 现价
                    "change_price": 涨跌额
                    "change_rate": 涨跌幅（已经乘了100，单位为"%"）
                    "yesterday_close": 昨收
                    "today_open": 今开
                    "high": 最高
                    "low": 最低
                    "volume": 成交量（单位为"万手"）
                    "volume_of_transaction": 成交额（单位为"万"）
                    "marketValue": 总市值（单位为"亿"）
                    "floatMarketValue": 流通市值（单位为"亿"）
                    "turnOverRate": 换手率（已经乘了100，单位为"%"）
                    "PB_ratio": 市净率
                    "amplitude": 振幅（已经乘了100，单位为"%"）
                    "PE_ratio": 市盈率
  }
 */
exports.obtainRTInfoByCode = obtainRTInfo;


/**
 * 更新每只股票的实时数据
 * @param callback (err, isOK) => {}
 * isOK 是布尔类型的值，更新成功为true，失败为false
 */
exports.updateAllStockRTInfo = (callback) => {
    allStockDB.getAllStockCodeAndName((err, allCodesAndNames) => {
        // 通过爬虫获得实时信息
        let promises = allCodesAndNames.map(t => new Promise((resolve, reject) => {
            obtainRTInfo(t["code"], (err, stockRTInfo) => {
                if (err)
                    reject(err);
                else {
                    stockRTInfo["code"] = t["code"];
                    resolve(stockRTInfo);
                }
            });
        }));
        Promise.all(promises).then(results => {
            let stockRTInfo = results.map(info => new Promise((resolve, reject) => {
                stockRTDB.updateRTInfo(info["code"], info, (err, isSucceed) => {
                    if (err)
                        reject(err);
                    else
                        resolve(isSucceed);
                });
            }));
            // 将实时信息保存进数据库
            Promise.all(stockRTInfo).then(results => {
                let isOK = true;
                for (let i = 0; i < results.length; i++) {
                    if (results[i] === false) {
                        isOK = false;
                        break;
                    }
                }
                callback(null, isOK)
            }).catch((err) => {
                callback(err, false);
            });
        }).catch((err) => {
            callback(err, false);
        });
    });
};


/**
 * 获得热门板块
 * @param callback (err, hot_boards) => {}
 * hot_boards形如:
 * { '公共交通': [ '11.565', '3.491' ],
     '电子设备': [ '24.264', '3.272' ],
     '工程机械': [ '9.957', '2.382' ],
     '铁路运输': [ '7.720', '1.604' ],
     '工程建筑': [ '11.281', '1.533' ]
  }
 */
exports.getHotBoard = (callback) => {
    exec('python3' + ' ../bl/realtime/hot_board.py', function(err, stdout, stderr){
        if(err) {
            callback(err, null);
        }
        if(stdout) {
            let boards = {};
            stdout.split(';').forEach(t => {
                let s = t.split(',');
                // 为了去除末尾的\n
                let temp = s[2].replace('\n', '');
                temp = temp.replace('\r', '');
                boards[s[0]] = [s[1], temp];
            });
            callback(null, boards);
        }
    });
};

/**
 * =========================================以下是私有方法=========================================
 */

/**
 * 获得个股实时的信息
 * @param code
 * @param callback (err, stockRTInfo) => {}
 *
 * ！！！！！！！！！！！！！注意括号中的单位，没有提到单位的属性，就是不用加单位！！！！！！！！！！！！！！！
 *
 * stockRTInfo形如
 * {
                    "now_price": 现价
                    "change_price": 涨跌额
                    "change_rate": 涨跌幅（已经乘了100，单位为"%"）
                    "yesterday_close": 昨收
                    "today_open": 今开
                    "high": 最高
                    "low": 最低
                    "volume": 成交量（单位为"万手"）
                    "volume_of_transaction": 成交额（单位为"万"）
                    "marketValue": 总市值（单位为"亿"）
                    "floatMarketValue": 流通市值（单位为"亿"）
                    "turnOverRate": 换手率（已经乘了100，单位为"%"）
                    "PB_ratio": 市净率
                    "amplitude": 振幅（已经乘了100，单位为"%"）
                    "PE_ratio": 市盈率
  }
 */
function obtainRTInfo(code, callback) {
    if (code.startsWith('60') || code.startsWith('900'))
        code = 'sh' + code;
    else
        code = 'sz' + code;
    let url = 'http://web.ifzq.gtimg.cn/appstock/app/minute/query?_var=min_data_' + code + '&code=' + code + '&r=0.5755017222238814';
    request.get(url)
        .end(function (err, res) {
            if (err)
                callback(err, null);
            else {
                let jd = JSON.parse(res.text.substr(18));
                let stockRTInfo = {
                    "now_price": jd['data'][code]['qt'][code][3],
                    "change_price": jd['data'][code]['qt'][code][31],
                    "change_rate": jd['data'][code]['qt'][code][32],
                    "yesterday_close": jd['data'][code]['qt'][code][4],
                    "today_open": jd['data'][code]['qt'][code][5],
                    "high": jd['data'][code]['qt'][code][33],
                    "low": jd['data'][code]['qt'][code][34],
                    "volume": jd['data'][code]['qt'][code][36],
                    "volume_of_transaction": jd['data'][code]['qt'][code][37],
                    "marketValue": jd['data'][code]['qt'][code][45],
                    "floatMarketValue": jd['data'][code]['qt'][code][44],
                    "turnOverRate": jd['data'][code]['qt'][code][38],
                    "PB_ratio": jd['data'][code]['qt'][code][46],
                    "amplitude": jd['data'][code]['qt'][code][43],
                    "PE_ratio": jd['data'][code]['qt'][code][39]
                };
                callback(null, stockRTInfo);
            }
        });
}

