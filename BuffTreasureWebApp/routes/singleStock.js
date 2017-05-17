let express = require('express');
let allStockBl = require('../bl/allStockbl');
let singleStockService = require('../bl/singleStockbl');
let router = express.Router();

router.get('/getAllStockList', (req, res, next) => {
    allStockBl.getAllStockCodeAndName((err, allStocks) => {
        if (err)
            res.render('error');

        res.send(allStocks);
    });
});

router.post('/getDailyKLine', (req, res, next) => {
    let re = /([0-9]+)\((.*)\)/;
    let result = re.exec(req.body.query);
    let stockCode = result[1];

    singleStockService.getDailyData(stockCode, (err, docs) => {
        if (err)
            res.render('error');

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

        res.send(docs);
    });
});

module.exports = router;