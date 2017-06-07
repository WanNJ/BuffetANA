/**
 * Created by slow_time on 2017/6/6.
 */
const request = require("superagent");


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
exports.obtainRTInfoByCode = (code, callback) => {
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
};

