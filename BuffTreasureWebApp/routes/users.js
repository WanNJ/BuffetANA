let express = require('express');
let strategyBl = require('../bl/strategy/strategybl');
let strategyRT = require('../bl/strategy/strategyRT');
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

function sendDirectly(res, err, docs) {
    if (err) {
        console.error(err);
    } else {
        res.send(docs);
    }
}

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
    singleStockRT.getHotBoard((err, docs) => sendDirectly(res, err, docs));
});

router.get('/quantitative-analysis/stockRecommend/hotStocks', function (req, res, next) {
    singleStockbl.getHotStocks((err, docs) => sendDirectly(res, err, docs));
});

router.get('/quantitative-analysis/stockRecommend/highScore', function (req, res, next) {
    strategyRT.getRccomandStockHighScore((err, docs) => sendDirectly(res, err, docs));
});

router.get('/quantitative-analysis/stockRecommend/profit', function (req, res, next) {
    strategyRT.getRccomandStockProfit((err, docs) => sendDirectly(res, err, docs));
});

router.get('/quantitative-analysis/stockRecommend/winRate', function (req, res, next) {
    strategyRT.getRccomandStockWinRate((err, docs) => sendDirectly(res, err, docs));
});

router.get('/quantitative-analysis/stockRecommend/antiRiskAbility', function (req, res, next) {
    strategyRT.getRccomandStockAntiRiskAbility((err, docs) => sendDirectly(res, err, docs));
});

router.get('/quantitative-analysis/strategyRecommend', function (req, res, next) {
    console.log("begin");
    strategyRT.getRtStrategyALL((err,docs) => {
        if(err){
            console.error(err);
        }else {
            console.log("send");
            res.render('User/strategyRec',docs);
        }
    });
    // res.render('User/strategyRec',{
    //     "HighAndSame": ['id'  ,'HighAndSame综合得分' ,'HighAndSame盈利能力' ,'HighAndSame绝对收益' ,'HighAndSame选股能力' ,'HighAndSame抗风险能力' ,'HighAndSame稳定性'],
    //     "HighAndOpposite": ['id' ,'HighAndOpposite综合得分' ,'HighAndOpposite盈利能力' ,'HighAndOpposite绝对收益' ,'HighAndOpposite选股能力' ,'HighAndOpposite抗风险能力' ,'HighAndOpposite稳定性'],
    //     "LowAndSame": ['id' ,'LowAndSame综合得分' ,'LowAndSame盈利能力' ,'LowAndSame绝对收益' ,'LowAndSame选股能力' ,'LowAndSame抗风险能力' ,'LowAndSame稳定性'],
    //     "LowAndOpposite": ['id' ,'LowAndOpposite综合得分' ,'LowAndOpposite盈利能力' ,'LowAndOpposite绝对收益' ,'LowAndOpposite选股能力' ,'LowAndOpposite抗风险能力' ,'LowAndOpposite稳定性'],
    //     "Normal": ['id' ,'Normal综合得分' ,'Normal盈利能力' ,'Normal绝对收益' ,'Normal选股能力' ,'Normal抗风险能力' ,'Normal稳定性'],
    // });
});

router.get('/quantitative-analysis/choose', function (req, res, next) {
    if(req.query.strategyID !== undefined) {
        res.locals.strategyID = req.query.strategyID;
    }

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
    userbl.getAllStrategy(req.session.user, (err, docs) => sendDirectly(res, err, docs));
});

router.get('/quantitative-analysis/strategy/:name', function (req, res, next) {
    userbl.loadStrategy(req.session.user, req.params.name, (err, docs) => sendDirectly(res, err, docs));
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

router.get('/stock-analysis', function (req, res, next) {
    res.render("User/stock-analysis");
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

router.get('/:id/analysisResult/SVM', (req, res, next) => {
    let result = {
        code: '601166',
        name: '兴业银行',
        correlation: 0.6680266834659534,
        profitRate: 0.01064398379685759,
        base: [1039.1063,
            1027.7,
            1035.6842,
            1049.3718,
            1058.4972,
            1055.0751,
            1047.0903,
            1025.4186,
            1031.122,
            1039.106,
            1043.6687,
            1042.528,
            1051.6532,
            1083.5909,
            1104.1217,
            1085.8717,
            1089.2932,
            1083.5897,
            1084.7307,
            1072.1836,
            1072.1836,
            1075.6049,
            1076.7451,
            1077.8853,
            1074.463,
            1080.1663,
            1082.4476,
            1077.885,
            1077.885,
            1074.4627,
            1073.3217,
            1072.1807,
            1072.1807,
            1069.8991,
            1044.8057,
            1048.2275,
            1033.3992,
            1040.2434,
            1035.681,
            1035.681,
            1044.8063,
            1043.6654,
            1031.1185,
            1033.3993,
            1032.2585,
            1033.3991,
            1034.5399,
            1040.2433,
            1043.6647,
            1041.3833,
            1034.5393,
            1036.8204,
            1032.2585,
            1036.821,
            1035.6805,
            1034.5402,
            1041.3837,
            1053.9303,
            1050.5082,
            1043.6641,
            1044.8049,
            1045.9458,
            1043.6646,
            1042.5239,
            1034.5392,
            1041.3826,
            1039.101,
            1040.2419,
            1043.6633,
            1034.5385,
            1042.5231,
            1047.0852,
            1051.6473,
            1052.7883,
            1052.7883,
            1050.5069,
            1047.0854,
            1053.9291,
            1067.6165,
            1077.8817,
            1080.1625,
            1097.2712,
            1098.4123,
            1097.2722,
            1089.2873,
            1094.9908,
            1089.2881,
            1079.0227,
            1082.4443,
            1081.3033,
            1085.8653,
            1100.6928,
            1083.5836,
            1074.4588,
            1072.1777,
            1055.0689,
            1055.0689,
            1049.3663,
            1039.1003,
            1044.8029],
        compare: [57.226,
            57.4471,
            56.968,
            57.0417,
            57.4471,
            56.968,
            57.5207,
            57.0786,
            56.9312,
            56.6732,
            57.1523,
            57.3734,
            57.5945,
            57.8893,
            58.3314,
            59.6211,
            60.5055,
            59.5106,
            59.1789,
            59.0315,
            59.1052,
            58.921,
            59.2895,
            59.1789,
            58.8473,
            58.6999,
            58.7367,
            59.1789,
            59.2158,
            59.1052,
            59.1421,
            59.1052,
            58.6999,
            58.9209,
            58.9947,
            58.8473,
            58.1103,
            58.2209,
            57.9629,
            58.4419,
            58.5893,
            58.4419,
            58.9578,
            58.8841,
            58.6262,
            58.9947,
            58.8841,
            58.9578,
            58.8473,
            59.0684,
            59.1052,
            58.921,
            58.9947,
            59.2895,
            58.8842,
            59.2158,
            59.1789,
            59.4,
            59.8422,
            60.1739,
            59.9159,
            59.6579,
            59.6948,
            59.9527,
            60.1002,
            60.6529,
            60.4318,
            60.7266,
            60.2844,
            59.9896,
            60.395,
            59.658,
            60.2844,
            60.0264,
            60.0264,
            59.9527,
            60.0264,
            59.9158,
            59.8053,
            60.2106,
            60.5054,
            61.4266,
            61.3161,
            62.2741,
            62.0531,
            62.5321,
            62.0531,
            62.2741,
            62.1636,
            62.311,
            62.2742,
            62.0531,
            62.3847,
            63.3796,
            63.7481,
            62.8637,
            63.2691,
            61.2056,
            60.8002,
            60.5054]
    };
    let finalResult = [];
    for(let i = 0; i < result.base.length; i++) {
        finalResult.push([result.base[i], result.compare[i]]);
    }

    res.locals.stockName = result.name;
    res.locals.stockCode = result.code;
    res.locals.correlation = Math.round(result.correlation * 100)/100;
    res.locals.profitRate = Math.round(result.profitRate * 10000)/100;

    res.locals.data = finalResult;
    res.render('User/SingleStockAnalysis/svm');
});

router.get('/:id/analysisResult/NN', (req, res, next) => {

    let data = {
        "time": {"$date": 1497063463279},
        "isRead": false,
        "type": "analysis1",
        "toString": "你的代码为000001的股票分析结果已出，请点击查看",
        "content": {
            "CR": [0.29, "低"],
            "upOrDown": ["54.29", "1"],
            "5-75": 2.819275832735002E-4,
            "0-25": 8.666887879371643E-5,
            "75-5": 0.12415958195924759,
            "75-10": 3.8002902874723077E-4,
            "10-75": 0.3716028332710266,
            "more10": 0.012712066993117332,
            "5-25": 0.047767940908670425,
            "25-5": 1.1517480743350461E-4,
            "25-0": 0.003162472043186426,
            "less10": 0.439731240272522
        }
    };
});

router.get('/:id/analysisResult/CNN', (req, res, next) => {
    let data = {
        "time": {"$date": 1497063933805},
        "isRead": false,
        "type": "analysis2",
        "toString": "你的代码为000002的股票分析结果已出，请点击查看",
        "content": {
            "CR": [0.29, "低"],
            "upOrDown": ["54.29", "1"],
            "process": [0.019999999552965164, 0.30000001192092896, 0.30000001192092896, 0.2800000011920929, 0.30000001192092896, 0.30000001192092896, 0.30000001192092896, 0.27000001072883606, 0.14000000059604645, 0.14000000059604645, 0.009999999776482582, 0.019999999552965164, 0.019999999552965164, 0.09000000357627869, 0.28999999165534973, 0.3100000023841858, 0.28999999165534973, 0.28999999165534973, 0.30000001192092896, 0.3100000023841858, 0.4099999964237213, 0.5199999809265137, 0.5199999809265137, 0.5699999928474426, 0.5600000023841858, 0.5699999928474426, 0.6100000143051147, 0.6100000143051147, 0.5799999833106995, 0.6600000262260437, 0.7099999785423279, 0.6899999976158142, 0.7200000286102295, 0.7400000095367432, 0.7200000286102295, 0.6600000262260437, 0.7599999904632568, 0.7900000214576721, 0.7900000214576721, 0.7900000214576721],
            "5-7_5": 2.819275832735002E-4,
            "0-2_5": 8.666887879371643E-5,
            "7_5-5": 0.12415958195924759,
            "accuracy": 0.7900000214576721,
            "7_5-10": 3.8002902874723077E-4,
            "10-7_5": 0.3716028332710266,
            "more10": 0.012712066993117332,
            "5-2_5": 0.047767940908670425,
            "2_5-5": 1.1517480743350461E-4,
            "2_5-0": 0.003162472043186426,
            "less10": 0.439731240272522
        }
    };
});
module.exports = router;
