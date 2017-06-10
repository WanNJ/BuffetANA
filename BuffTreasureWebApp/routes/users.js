let express = require('express');
let strategyBl = require('../bl/strategy/strategybl');
let singleStockRT = require('../bl/realtime/singleStockRT');
let singleStockbl = require('../bl/singleStockbl');
let storeMap = require('../bl/functionMap/storeMap');
let industrybl = require('../bl/industryandbench/industrybl');
let userbl = require('../bl/userbl');
let singleStockService = require('../bl/singleStockbl');
let router = express.Router();
let StockPoolConditionVO = require('../vo/StockPoolConditionVO').StockPoolConditionVO;
let TradeModelVO = require('../vo/TradeModelVO').TradeModelVO;

let sendMessage = (res, err, docs) => {
    if (err) {
        throw err;
    }
    else {
        result = "";
        docs.forEach((i) => {
            result += i + ",";
        });
        if (result.length > 0) {
            result = result.substr(0, result.length - 1);
        }
        res.send(result);
    }
};

function splitStrategyResult(rawData) {
    let strategyScores = rawData[0].strategyEstimateResult;

    let normal_historyData = rawData[0].historyTradeRecord;
    let highAndSame_historyData = rawData[1].historyTradeRecord;
    let highAndOpp_historyData = rawData[2].historyTradeRecord;
    let lowAndSame_historyData = rawData[3].historyTradeRecord;
    let lowAndOpp_historyData = rawData[4].historyTradeRecord;

    let backDetails = [];

    let base_Dates = rawData[0].baseDayRatePiece.date;
    let normal_Dates = rawData[0].strategyDayRatePiece.date;
    let highAndSame_Dates = rawData[1].strategyDayRatePiece.date;
    let highAndOpp_Dates = rawData[2].strategyDayRatePiece.date;
    let lowAndSame_Dates = rawData[3].strategyDayRatePiece.date;
    let lowAndOpp_Dates = rawData[4].strategyDayRatePiece.date;

    let base_profitRate = rawData[0].baseDayRatePiece.profitRate;
    let normal_profitRate = rawData[0].strategyDayRatePiece.profitRate;
    let highAndSame_profitRate = rawData[1].strategyDayRatePiece.profitRate;
    let highAndOpp_profitRate = rawData[2].strategyDayRatePiece.profitRate;
    let lowAndSame_profitRate = rawData[3].strategyDayRatePiece.profitRate;
    let lowAndOpp_profitRate = rawData[4].strategyDayRatePiece.profitRate;

    let normal_pieData = [];
    let highAndSame_pieData = [];
    let highAndOpp_pieData = [];
    let lowAndSame_pieData = [];
    let lowAndOpp_pieData = [];

    for (let i = 0; i < 5; i++) {
        backDetails.push(rawData[i].backDetail);
    }

    normal_pieData.push({value: rawData[0].profitDistributePie.green0, name: '收益 -3.5% ~ 0'});
    normal_pieData.push({value: rawData[0].profitDistributePie.green35, name: '收益 -7.5% ~ -3.5%'});
    normal_pieData.push({value: rawData[0].profitDistributePie.green75, name: '收益小于 -7.5%'});
    normal_pieData.push({value: rawData[0].profitDistributePie.red0, name: '收益 0 ~ 3,5%'});
    normal_pieData.push({value: rawData[0].profitDistributePie.red35, name: '收益 3.5% ~ 7.5%'});
    normal_pieData.push({value: rawData[0].profitDistributePie.red75, name: '收益大于 7.5%'});

    highAndSame_pieData.push({value: rawData[1].profitDistributePie.green0, name: '收益 -3.5% ~ 0'});
    highAndSame_pieData.push({value: rawData[1].profitDistributePie.green35, name: '收益 -7.5% ~ -3.5%'});
    highAndSame_pieData.push({value: rawData[1].profitDistributePie.green75, name: '收益小于 -7.5%'});
    highAndSame_pieData.push({value: rawData[1].profitDistributePie.red0, name: '收益 0 ~ 3,5%'});
    highAndSame_pieData.push({value: rawData[1].profitDistributePie.red35, name: '收益 3.5% ~ 7.5%'});
    highAndSame_pieData.push({value: rawData[1].profitDistributePie.red75, name: '收益大于 7.5%'});

    highAndOpp_pieData.push({value: rawData[2].profitDistributePie.green0, name: '收益 -3.5% ~ 0'});
    highAndOpp_pieData.push({value: rawData[2].profitDistributePie.green35, name: '收益 -7.5% ~ -3.5%'});
    highAndOpp_pieData.push({value: rawData[2].profitDistributePie.green75, name: '收益小于 -7.5%'});
    highAndOpp_pieData.push({value: rawData[2].profitDistributePie.red0, name: '收益 0 ~ 3,5%'});
    highAndOpp_pieData.push({value: rawData[2].profitDistributePie.red35, name: '收益 3.5% ~ 7.5%'});
    highAndOpp_pieData.push({value: rawData[2].profitDistributePie.red75, name: '收益大于 7.5%'});

    lowAndSame_pieData.push({value: rawData[3].profitDistributePie.green0, name: '收益 -3.5% ~ 0'});
    lowAndSame_pieData.push({value: rawData[3].profitDistributePie.green35, name: '收益 -7.5% ~ -3.5%'});
    lowAndSame_pieData.push({value: rawData[3].profitDistributePie.green75, name: '收益小于 -7.5%'});
    lowAndSame_pieData.push({value: rawData[3].profitDistributePie.red0, name: '收益 0 ~ 3,5%'});
    lowAndSame_pieData.push({value: rawData[3].profitDistributePie.red35, name: '收益 3.5% ~ 7.5%'});
    lowAndSame_pieData.push({value: rawData[3].profitDistributePie.red75, name: '收益大于 7.5%'});

    lowAndOpp_pieData.push({value: rawData[3].profitDistributePie.green0, name: '收益 -3.5% ~ 0'});
    lowAndOpp_pieData.push({value: rawData[3].profitDistributePie.green35, name: '收益 -7.5% ~ -3.5%'});
    lowAndOpp_pieData.push({value: rawData[3].profitDistributePie.green75, name: '收益小于 -7.5%'});
    lowAndOpp_pieData.push({value: rawData[3].profitDistributePie.red0, name: '收益 0 ~ 3,5%'});
    lowAndOpp_pieData.push({value: rawData[3].profitDistributePie.red35, name: '收益 3.5% ~ 7.5%'});
    lowAndOpp_pieData.push({value: rawData[3].profitDistributePie.red75, name: '收益大于 7.5%'});

    return {
        strategyScores: strategyScores,
        normal_historyDatas: normal_historyData,
        highAndSame_historyDatas: highAndSame_historyData,
        highAndOpp_historyDatas: highAndOpp_historyData,
        lowAndSame_historyDatas: lowAndSame_historyData,
        lowAndOpp_historyDatas: lowAndOpp_historyData,

        backDetails: backDetails,

        base_Dates: base_Dates,
        normal_Dates: normal_Dates,
        highAndSame_Dates: highAndSame_Dates,
        highAndOpp_Dates: highAndOpp_Dates,
        lowAndSame_Dates: lowAndSame_Dates,
        lowAndOpp_Dates: lowAndOpp_Dates,

        base_profitRates: base_profitRate,
        normal_profitRates: normal_profitRate,
        highAndSame_profitRates: highAndSame_profitRate,
        highAndOpp_profitRates: highAndOpp_profitRate,
        lowAndSame_profitRates: lowAndSame_profitRate,
        lowAndOpp_profitRates: lowAndOpp_profitRate,

        normal_pieDatas: normal_pieData,
        highAndSame_pieDatas: highAndSame_pieData,
        highAndOpp_pieDatas: highAndOpp_pieData,
        lowAndSame_pieDatas: lowAndSame_pieData,
        lowAndOpp_pieDatas: lowAndOpp_pieData
    };
}

router.use(function (req, res, next) {
    let sess = req.session;

    if (sess.user) {
        next();
    } else {
        req.session.alertType = "alert-danger";
        req.session.alertMessage = "您还未登录，请先登录后再使用该功能！";
        res.redirect('/sign-in');
    }
});

router.get('/comparison', function (req, res, next) {
    res.render('User/comparison');
});

router.get('/marketthermometer', function (req, res, next) {
    res.render('User/marketthermometer');
});

router.get('/quantitative-analysis', function (req, res, next) {
    res.render('User/quantitative-analysis');
});

router.get('/quantitative-analysis/stockRecommend', function (req, res, next) {
    res.render('User/stockRec');

});

router.get('/quantitative-analysis/stockRecommend/hotBoard', function (req, res, next) {
    singleStockRT.getHotBoard((err,docs) => {
        if(err){
            throw err;
        }else {
            res.send(docs);
        }
    });
});

router.get('/quantitative-analysis/stockRecommend/hotStocks', function (req, res, next) {
    singleStockbl.getHotStocks((err,docs) => {
        if(err){
            throw err;
        }else {
            res.send(docs);
        }
    });
});

router.get('/quantitative-analysis/strategyRecommend', function (req, res, next) {
    res.render('User/strategyRec');
});

router.get('/quantitative-analysis/choose', function (req, res, next) {
    res.render('User/quantitative-choose');
});

router.get('/quantitative-analysis/chmap', function (req, res, next) {
    res.send(storeMap.chmap);
});

router.get('/quantitative-analysis/allIndustries', function (req, res, next) {
    industrybl.getAllIndustries((err, docs) => sendMessage(res, err, docs));
});

router.get('/quantitative-analysis/allBoards', function (req, res, next) {
    industrybl.getAllBoards((err, docs) => sendMessage(res, err, docs));
});

router.get('/quantitative-analysis/allStrategy', function (req, res, next) {
    userbl.getAllStrategy(req.session.user, (err, docs) => {
        if (err) {
            throw err;
        } else {
            res.send(docs);
        }
    });
});

router.get('/quantitative-analysis/strategy/:name', function (req, res, next) {
    userbl.loadStrategy(req.session.user, req.params.name, (err, docs) => {
        if (err) {
            throw err;
        } else {
            res.send(docs);
        }
    });
});

router.post('/quantitative-analysis/loading', function (req, res, next) {
    req.session.strategyData = req.body;
    res.render('User/quantitative-loading');
});

router.get('/quantitative-analysis/process', function (req, res, next) {
    res.send("100");
});

router.get('/quantitative-analysis/result', function (req, res, next) {
    let body = req.session.strategyData;

    let beginDate = new Date(body.beginDate);
    let endDate = new Date(body.endDate);

    let excludeST = body.excludeST === 'on';

    let benches = body.benches === "" ? null : body.benches.split(",");
    let industries = body.industries === "" ? null : body.industries.split(",");
    let stockPoolCdtVO = new StockPoolConditionVO(body.stockPool, benches, industries, excludeST);
    let rank = JSON.parse(body.rank);
    let filter = JSON.parse(body.filter);
    let tradeModelVo = body.dynamic_hold === "on" ?
        new TradeModelVO(Number(body.reserveDays), Number(body.numberOfStock), Number(body.maxWinRate), Number(body.maxLoseRate)) :
        new TradeModelVO(Number(body.reserveDays), Number(body.numberOfStock));
    let envSpyDay = Number(body.marketObserve);

    console.log(beginDate, endDate, stockPoolCdtVO, rank, filter, tradeModelVo, envSpyDay);
    strategyBl.getBackResults(beginDate, endDate, stockPoolCdtVO, rank, filter, tradeModelVo, envSpyDay, (err, data) => {
        let finalResult = splitStrategyResult(data);
        console.log(finalResult);

        if (err) {
            //TODO
        } else {
            res.render('User/quantitative-result', {
                strategyScores: finalResult.strategyScores,

                normal_historyDatas: finalResult.normal_historyDatas,
                highAndSame_historyDatas: finalResult.highAndSame_historyDatas,
                highAndOpp_historyDatas: finalResult.highAndOpp_historyDatas,
                lowAndSame_historyDatas: finalResult.lowAndSame_historyDatas,
                lowAndOpp_historyDatas: finalResult.lowAndOpp_historyDatas,

                backDetails: finalResult.backDetails,

                base_Dates: finalResult.base_Dates,
                normal_Dates: finalResult.normal_Dates,
                highAndSame_Dates: finalResult.highAndSame_Dates,
                highAndOpp_Dates: finalResult.highAndOpp_Dates,
                lowAndSame_Dates: finalResult.lowAndSame_Dates,
                lowAndOpp_Dates: finalResult.lowAndOpp_Dates,

                base_profitRates: finalResult.base_profitRates,
                normal_profitRates: finalResult.normal_profitRates,
                highAndSame_profitRates: finalResult.highAndSame_profitRates,
                highAndOpp_profitRates: finalResult.highAndOpp_profitRates,
                lowAndSame_profitRates: finalResult.lowAndSame_profitRates,
                lowAndOpp_profitRates: finalResult.lowAndOpp_profitRates,

                normal_pieDatas: finalResult.normal_pieDatas,
                highAndSame_pieDatas: finalResult.highAndSame_pieDatas,
                highAndOpp_pieDatas: finalResult.highAndOpp_pieDatas,
                lowAndSame_pieDatas: finalResult.lowAndSame_pieDatas,
                lowAndOpp_pieDatas: finalResult.lowAndOpp_pieDatas
            });
        }
    });
});

router.post('/quantitative-analysis/save', function (req, res, next) {
    let body = req.body;
    if (body["benches[]"] !== undefined) {
        body["benches"] = body["benches[]"];
        delete body["benches[]"];
    }
    if (body["industries[]"] !== undefined) {
        body["industries"] = body["industries[]"];
        delete body["industries[]"];
    }
    let userName = req.session.user;

    userbl.saveStrategy(userName, body, (err, docs) => {
        if (err) {
            throw err;
        } else {
            res.send(docs);  //docs可能的值： 'SUCCESS' 'DUPLICATED' true false
        }
    });
});

router.get('/:id/personalStocks', (req, res, next) => {
    if (req.session.user === req.params.id) {
        userbl.getSelfSelectStock(req.params.id, (err, docs) => {
            if (err)
                res.render('error');
            else {
                res.locals.stockNames = docs;
                let promises = docs.map(t => new Promise((resolve, reject) => {
                    singleStockService.getRTInfo(t[1], (err, docs) => {
                        if (err)
                            reject(err);
                        else
                            resolve(docs);
                    });
                }));
                Promise.all(promises).then(results => {
                    res.locals.stockInfos = results;
                    res.render('User/personalStocks');
                }).catch(err => {
                    res.render('error');
                });
            }
        });
    } else {
        res.render('error');
    }
});

module.exports = router;
