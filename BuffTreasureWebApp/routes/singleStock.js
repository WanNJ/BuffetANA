let express = require('express');
let allStockBl = require('../bl/allStockbl');
let singleStockService = require('../bl/singleStockbl');
let industryService = require('../bl/industryandbench/industrybl');
let userBl = require('../bl/userbl');
let router = express.Router();

router.get('/getAllStockList', (req, res, next) => {
    allStockBl.getAllStockCodeAndName((err, allStocks) => {
        if (err)
            res.render('error');
        else
            res.send(allStocks);
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

module.exports = router;