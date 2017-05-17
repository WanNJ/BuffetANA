let express = require('express');
let router = express.Router();

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

/* GET users listing. */
router.get('/:id', function(req, res, next) {
    console.log('Request URL:', req.originalUrl);
    res.send('User requests to get: ' + req.params.id);
});

module.exports = router;
