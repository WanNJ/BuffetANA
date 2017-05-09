let express = require('express');
let singleStockService = require('../bl/singleStockbl');
let router = express.Router();

/* GET users listing. */
router.post('/search', function(req, res, next) {
    let re = /(.*)\(([0-9]+)\)/;
    let result = re.exec(req.body.query_text);
    let stockCode = result[2];

    console.log(stockCode);
    singleStockService.getDailyData(stockCode, (err, docs) => {
        if(err)
            render('error');


    });
});

router.post('/ajax/getSearchResult', function(req, res, next) {

});

module.exports = router;