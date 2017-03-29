var express = require('express');
var router = express.Router();

/* GET HOME PAGE 'next'在下面的工作中可能会用到 */
router.get('/', function(req, res, next) {
  res.render('index');
});


/* GET Other Menu Pages 'next'在下面的工作中可能会用到 */
router.get('/market', function(req, res, next) {
    res.render('market');
});

router.get('/single-stock', function(req, res, next) {
    res.render('single-stock');
});

router.get('/comparison', function(req, res, next) {
    res.render('comparison');
});

router.get('/marketthermometer', function(req, res, next) {
    res.render('marketthermometer');
});

router.get('/about-us', function(req, res, next) {
    res.render('about-us');
});

module.exports = router;
