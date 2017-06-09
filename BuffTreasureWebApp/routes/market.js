let express = require('express');
let marketService = require('../bl/market/marketbl');
let router = express.Router();


router.get('/getRTInfo', (req, res, next) => {
    marketService.getMarketRTInfo(req.query.stockCode, (err, infos) => {
        if (err)
            res.render('error');
        else
            res.send(infos);
    });
});

router.post('/getDailyKLine', (req, res, next) => {
    marketService.getDailyMarketIndex(req.body.stockCode, (err, docs) => {
        if (err)
            res.render('error');
        else
            res.send(docs);
    });
});

router.post('/getWeeklyKLine', (req, res, next) => {
    marketService.getWeeklyMarketIndex(req.body.stockCode, (err, docs) => {
        if (err)
            res.render('error');
        else
            res.send(docs);
    });
});

router.post('/getMonthlyKLine', (req, res, next) => {
    marketService.getMonthlyMarketIndex(req.body.stockCode, (err, docs) => {
        if (err)
            render('error');
        else
            res.send(docs);
    });
});

module.exports = router;