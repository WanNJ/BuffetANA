let express = require('express');
let strategyBl = require('../bl/strategy/strategybl');
let strategyRT = require('../bl/strategy/strategyRT');
let singleStockRT = require('../bl/realtime/singleStockRT');
let thememometerRT = require('../bl/realtime/thememometerRT');
let singleStockbl = require('../bl/singleStockbl');
let storeMap = require('../bl/functionMap/storeMap');
let industrybl = require('../bl/industryandbench/industrybl');
let userbl = require('../bl/userbl');
let singleStockService = require('../bl/singleStockbl');
let router = express.Router();
let StockPoolConditionVO = require('../vo/StockPoolConditionVO').StockPoolConditionVO;
let TradeModelVO = require('../vo/TradeModelVO').TradeModelVO;

/**
 * 用于存储用户的回测进度和结果
 * @type {{}}  * e.g:某时刻时的值:
 {
 "user1": {"process": 20},
 "user2": {"process": 100, "strategyResult": ...},
 }
 */
let strategyJSON = {};
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
    thememometerRT.getCurrentThermometor((err,thermometer)=>{
        let data=thermometer;
        thememometerRT.getCurrentENV((err,currentENV)=>{
            let ENVMap={
                "HighAndSame": "温度高，持续上涨",
                "HighAndOpposite": "温度高，开始反转",
                "LowAndOpposite": "温度低，持续下降",
                "LowAndSame": "温度低，开始回暖",
            };
            data.currentENV=ENVMap[currentENV];
            res.render('User/marketthermometer',data);
        });
    });

    // res.render('User/marketthermometer',{
    //     limitUp: 7,
    //     limitDown: 1,
    //     halfLimitUp: 42,
    //     halfLimitDown: 4,
    //     temperature: 87.5,
    //     lastLimitUp: '0.00000',
    //     lastLimitDown: '-0.26000',
    //     lastTurnOver: '62.00000',//换手率前50只股票的赚钱效应
    //     moneyEffect: '37.78990', //赚钱效应
    //     currentENV: '低温反转',
    // });
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
    strategyRT.getRtStrategyALL((err, docs) => {
        if (err) {
            console.error(err);
        } else {
            res.render('User/strategyRec', docs);
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
    if (req.query.strategyID !== undefined) {
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

router.post('/quantitative-analysis/getStrategy', function (req, res, next) {
    strategyRT.getStragtegyByID(req.body.strategyID, (err, docs) => sendDirectly(res, err, docs))
    // res.send({
    //     strategyName: "strategyName",
    //     beginDate: "2016/01/01",
    //     endDate: "2017/01/01",
    //     stockPool: "自选股票池",
    //     benches: ["板块1", "板块2"],
    //     industries: ["行业1", "行业2"],
    //     excludeST: "off",
    //     rank: JSON.stringify({
    //         "r1": ["MA", "asd", 10, 0.4],
    //         "r2": ["MOM", "des", 20, 0.6],
    //     }),
    //     filter: JSON.stringify({
    //         "f1": ["volume", ">", 1000000],
    //         "f2": ["turnOverRate", "<", 0.05],
    //     }),
    //     reserveDays: "3",
    //     numberOfStock: "4",
    //     marketObserve: "5",
    //     dynamic_hold: "on",
    //     maxWinRate: "6",
    //     maxLoseRate: "7",
    //     markNormal: "80",// 正常情况下 情况下的分数
    //     markHS: "80",// high and same 情况下的分数
    //     markHO: "80",// high and opposite 情况下的分数
    //     markLS: "80",// low and same 情况下的分数
    //     markLO: "80",// low and opposite 情况下的分数
    // });
});

router.get('/quantitative-analysis/allStrategy', function (req, res, next) {
    userbl.getAllStrategy(req.session.user, (err, docs) => sendDirectly(res, err, docs));
});

router.get('/quantitative-analysis/strategy/:name', function (req, res, next) {
    userbl.loadStrategy(req.session.user, req.params.name, (err, docs) => sendDirectly(res, err, docs));
});

router.get('/quantitative-analysis/state', function (req, res, next) {
    if (strategyJSON[req.session.user]) {
        res.send("上一次回测还没完成，请先等待其完成");
    } else {
        res.send("avaliable");
    }
});

router.post('/quantitative-analysis/loading', function (req, res, next) {
    if (strategyJSON[req.session.user]) {
        res.send("上一次回测还没完成，请先等待其完成");
    } else {
        req.session.strategyData = req.body;

        let body = req.body;

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

        strategyJSON[req.session.user]={
            "process": 0,
        };

        console.log(beginDate, endDate, stockPoolCdtVO, rank, filter, tradeModelVo, envSpyDay);
        strategyBl.getBackResults(beginDate, endDate, stockPoolCdtVO, rank, filter, tradeModelVo, envSpyDay, (process) => {
            strategyJSON[req.session.user].process = process;
        }, (err, data) => {
            let finalResult = splitStrategyResult(data);
            console.log(finalResult);

            if (err) {
                console.error(err);
            } else {
                strategyJSON[req.session.user].strategyResult = finalResult;
                strategyJSON[req.session.user].process = 100;
            }
        });
        res.render('User/quantitative-loading');
    }
});

router.get('/quantitative-analysis/process', function (req, res, next) {
    let process = strategyJSON[req.session.user].process;
    res.send(process.toString());
});

router.get('/quantitative-analysis/result', function (req, res, next) {
    let finalResult = strategyJSON[req.session.user].strategyResult;
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
    delete strategyJSON[req.session.user];
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

router.get('/:id/getUnreadMsgNum', (req, res, next) => {
    if (req.params.id !== req.session.user) {
        res.send('NOT_QUALIFIED');
    }
    else {
        userbl.getUnreadMessageCount(req.params.id, (err, count) => {
            if (err)
                res.send('ERROR');
            else {
                res.send(count.toString());
            }
        });
    }
});

router.post('/:id/markAsRead', (req, res, next) => {
    if (req.params.id !== req.session.user) {
        res.send('NOT_QUALIFIED');
    }
    else {
        userbl.markAsRead(req.params.id, req.body.time, (err) => {
            if (err)
                res.send('ERROR');
            else
                res.send('SUCCESS');
        });
    }
});

router.get('/:id/allMsg', (req, res, next) => {
    if (req.params.id !== req.session.user) {
        res.send('NOT_QUALIFIED');
    }
    else {
        userbl.getAllMessages(req.params.id, (err, readMessages, unReadMessages) => {
            if (err)
                res.send('ERROR');
            else {
                res.locals.username = req.params.id;
                res.locals.unreadMsg = unReadMessages;
                res.locals.readMsg = readMessages;

                res.render('User/messages');
            }
        });
    }
});

router.get('/:id/msg-result/:type', (req, res, next) => {
    if (req.params.id !== req.session.user) {
        res.send('NOT_QUALIFIED');
    }
    else {
        userbl.getOneMessageContent(req.params.id, req.query.time, (err, msg) => {
            if (err)
                res.send('ERROR');
            else {
                res.locals.username = req.params.id;
                res.locals.time = msg.time;
                res.locals.stockCode = msg.codeOrName;
                res.locals.stockName = msg.stockName;

                if (req.params.type === 'svm') {
                    let finalResult = [];
                    for (let i = 0; i < msg.content.base.length; i++) {
                        finalResult.push([msg.content.base[i], msg.content.compare[i]]);
                    }

                    res.locals.open = msg.content.open;
                    res.locals.holdingDays = msg.content.holdingDays;
                    res.locals.correlation = Math.round(msg.content.correlation * 100) / 100;
                    res.locals.profitRate = Math.round(msg.content.profitRate * 10000) / 100;

                    res.locals.relatedCode = msg.content.relatedCode;
                    res.locals.relatedName = msg.content.relatedName;

                    res.locals.CR = msg.content.CR[0];
                    res.locals.CRString = msg.content.CR[1];
                    res.locals.reliablity = msg.content.upOrDown[0];
                    res.locals.isUp = msg.content.upOrDown[1];

                    res.locals.data = finalResult;

                    res.render('User/SingleStockAnalysis/svm');
                }
                else if (req.params.type === 'cnn') {
                    res.locals.line_y_data = msg.content.process;
                    res.locals.pie_data = [
                        msg.content["less10"],
                        msg.content["10-7_5"],
                        msg.content["7_5-5"],
                        msg.content["5-2_5"],
                        msg.content["2_5-0"],
                        msg.content["0-2_5"],
                        msg.content["2_5-5"],
                        msg.content["5-7_5"],
                        msg.content["7_5-10"],
                        msg.content["more10"]
                    ];
                    res.locals.accuracy = Math.round(msg.content.accuracy * 10000) / 100;
                    res.locals.holdingDays = msg.content.holdingDays;
                    res.locals.iterationNum = msg.content.iterationNum;
                    if(msg.content.isMarket)
                        res.locals.isMarket = '是';
                    else
                        res.locals.isMarket = '否';

                    if(msg.content.learningWay === 'ALL')
                        res.locals.learningWay = '全部';
                    else
                        res.locals.learningWay = '分批';

                    res.render('User/SingleStockAnalysis/cnn');
                }
                else if (req.params.type === 'rfc') {
                    res.locals.pie_data = [
                        parseFloat(msg.content["down"]),
                        parseFloat(msg.content["up"]),
                        parseFloat(msg.content["smooth"])
                    ];
                    res.locals.holdingDays = msg.content.holdingDays;
                    res.locals.accuracy = Math.round(msg.content.accuracy * 10000) / 100;

                    let bar_x_data = [];
                    let bar_y_data = [];

                    for(let i = msg.content.importance.length - 1; i >= 0; i--) {
                        bar_y_data.push(new String(msg.content.importance[i][0]));
                        bar_x_data.push(msg.content.importance[i][1]);
                    }

                    console.log();

                    res.locals.bar_x_data = bar_x_data;
                    res.locals.bar_y_data = bar_y_data;

                    res.render('User/SingleStockAnalysis/rfc');
                }
            }
        });
    }
});

module.exports = router;