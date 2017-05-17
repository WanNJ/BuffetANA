let express = require('express');
let singleStockService = require('../bl/singleStockbl');
let router = express.Router();

/* GET users listing. */
router.post('/getDailyKLine', function(req, res, next) {
    let re = /(.*)\(([0-9]+)\)/;
    let result = re.exec(req.body.query);
    let stockCode = result[2];

    singleStockService.getDailyData(stockCode, (err, docs) => {
        if(err)
            render('error');

        res.send(docs);
    });
});

router.post('/getWeeklyKLine', function(req, res, next) {
    let re = /(.*)\(([0-9]+)\)/;
    let result = re.exec(req.body.query);
    let stockCode = result[2];

    singleStockService.getWeeklyData(stockCode, (err, docs) => {
        if(err)
            render('error');

        res.send(docs);
    });
});

router.post('/getMonthlyKLine', function(req, res, next) {
    let re = /(.*)\(([0-9]+)\)/;
    let result = re.exec(req.body.query);
    let stockCode = result[2];

    singleStockService.getMonthlyData(stockCode, (err, docs) => {
        if(err)
            render('error');

        res.send(docs);
    });
});

module.exports = router;