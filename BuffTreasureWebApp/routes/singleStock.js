let express = require('express');
let allStockBl = require('../bl/allStockbl');
let singleStockService = require('../bl/singleStockbl');
let singleStockPredict = require('../bl/statistics/singleStockPredict');
let industryService = require('../bl/industryandbench/industrybl');
let userBl = require('../bl/userbl');
let comment = require('../bl/forum/forumbl').forumbl;
let router = express.Router();

router.get('/getAllStockList', (req, res, next) => {
    allStockBl.getAllStockCodeAndName((err, allStocks) => {
        if (err)
            res.render('error');
        else
            res.send(allStocks);
    });
});

router.get('/getStockComments', (req, res, next) => {
    comment.getAllStockComment(req.query.stockCode, req.session.user, (err, comments) => {
        if (err)
            res.render('error');
        else {
            res.locals.overall_like = comments.like;
            res.locals.overall_dislike = comments.dislike;
            res.locals.overall_clickAble = comments.clickAble;
            res.locals.allComments = comments.contents;
            res.render('Components/comment');
        }
    });
});

router.get('/getHotStockList', (req, res, next) => {
    singleStockService.getHotStocks((err, infos) => {
        if (err)
            res.render('error');
        else
            res.send(infos);
    });
});

router.get('/getStockIndustry', (req, res, next) => {
    industryService.getIndustryByCode(req.query.stockCode, (err, indus) => {
        if (err)
            res.render('error');
        else
            res.send(indus);
    });
});

router.get('/getStockBoard', (req, res, next) => {
    industryService.getBoardsByCode(req.query.stockCode, (err, boards) => {
        if (err)
            res.render('error');
        else
            res.send(boards);
    });
});

router.get('/getRTInfo', (req, res, next) => {
    singleStockService.getRTInfo(req.query.stockCode, (err, infos) => {
        if (err)
            res.render('error');
        else
            res.send(infos);
    });
});

router.get('/enterprise', (req, res, next) => {
    singleStockService.getCompanyInfo(req.query.stockCode, (err, infos) => {
        res.locals.companyInfo = infos;

        console.log(infos);

        if (err)
            res.render('error');
        else {
            res.render('enterprise');
        }
    });
});

router.post('/getDailyKLine', (req, res, next) => {
    let re = /([0-9]+)\((.*)\)/;
    let result = re.exec(req.body.query);
    let stockCode = result[1];

    singleStockService.getDailyData(stockCode, (err, docs) => {
        if (err)
            res.render('error');
        else
            res.send(docs);
    });
});

router.post('/getWeeklyKLine', (req, res, next) => {
    let re = /([0-9]+)\((.*)\)/;
    let result = re.exec(req.body.query);
    let stockCode = result[1];

    singleStockService.getWeeklyData(stockCode, (err, docs) => {
        if (err)
            res.render('error');
        else
            res.send(docs);
    });
});

router.post('/getMonthlyKLine', (req, res, next) => {
    let re = /([0-9]+)\((.*)\)/;
    let result = re.exec(req.body.query);
    let stockCode = result[1];

    singleStockService.getMonthlyData(stockCode, (err, docs) => {
        if (err)
            render('error');
        else
            res.send(docs);
    });
});

router.post('/addToPersonalStock', (req, res, next) => {
    let sess = req.session;

    if (sess.user) {
        userBl.addToSelfSelectStock(sess.user, {
            'stockCode': req.body.stockCode,
            'stockName': req.body.stockName
        }, (err, result) => {
            if (err)
                res.render('error');
            res.send(result);
        });
    } else {
        res.send('SIGN_ERROR');
    }
});

router.post('/pressStockGood', (req, res, next) => {
    comment.pressStockGood(req.body.stockCode, req.session.user, (err) => {
        if(err)
            res.send('ERROR');
        else
            res.send('SUCCESS');
    });
});

router.post('/pressStockBad', (req, res, next) => {
    comment.pressStockBad(req.body.stockCode, req.session.user, (err) => {
        if(err)
            res.send('ERROR');
        else
            res.send('SUCCESS');
    });
});

router.post('/pressContentGood', (req, res, next) => {
    comment.pressContentGood(req.body.content_id, req.session.user, (err) => {
        if(err)
            res.send('ERROR');
        else
            res.send('SUCCESS');
    });
});

router.post('/pressContentBad', (req, res, next) => {
    comment.pressContentBad(req.body.content_id, req.session.user, (err) => {
        if(err)
            res.send('ERROR');
        else
            res.send('SUCCESS');
    });
});

router.post('/commentStock', (req, res, next) => {
    comment.commentStock(req.body.stockCode, req.session.user, req.body.content, (err) => {
        if(err)
            res.send('ERROR');
        else
            res.send('SUCCESS');
    });
});

router.post('/analysisStock', (req, res, next) => {//TODO:接口还没敲定
    let model = req.body.model;
    if(model==="SVM"){
        singleStockPredict.SVMAnalyze(req.session.user, req.body.stockCode, req.body.stockName, Number(req.body.openPrice),
            Number(req.body.holdingDays), req.body.time, (err, isOK) => {

        });
        req.session.alertType = "alert-success";
        req.session.alertMessage = "SVM分析中，分析完毕后在网页右上角会有提示";
        res.send("SVMAnalyzing");
    }else  if(model==="RFC"){
        singleStockPredict.RFCAnalyze(req.session.user, req.body.stockCode, req.body.stockName,
            Number(req.body.holdingDays), req.body.time, (err, isOK) => {

        });
        req.session.alertType = "alert-success";
        req.session.alertMessage = "RFC分析中，分析完毕后在网页右上角会有提示";
        res.send("RFCAnalyzing");
    }else  if(model==="CNN"){
        if(req.body.advancedOptions==="on"){
            singleStockPredict.CNNAnalyze(req.session.user, req.body.stockCode, req.body.stockName,
                Number(req.body.holdingDays),req.body.isMarket==="on"? true:false,Number(req.body.iterationNum),
                req.body.learningWay, req.body.time, (err, isOK) => {

            });
        }else {
            singleStockPredict.CNNAnalyze(req.session.user, req.body.stockCode, req.body.stockName,
                Number(req.body.holdingDays),undefined,undefined,undefined, req.body.time, (err, isOK) => {

            });
        }
        req.session.alertType = "alert-success";
        req.session.alertMessage = "CNN分析中，分析完毕后在网页右上角会有提示";
        res.send("CNNAnalyzing");
    }else {
        req.session.alertType = "alert-warning";
        req.session.alertMessage = "不能识别分析模型:"+model;
        res.send("unrecognize model:"+model);
    }
});

module.exports = router;