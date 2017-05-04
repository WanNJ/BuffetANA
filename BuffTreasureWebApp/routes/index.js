let express = require('express');
let router = express.Router();

/* GET HOME PAGE 'next'在下面的工作中可能会用到 */
router.get('/', function (req, res, next) {
    res.render('index');
});

/* GET Other Menu Pages 'next'在下面的工作中可能会用到 */
router.get('/market', function (req, res, next) {
    res.render('market');
});

router.get('/single-stock', function (req, res, next) {
    res.render('single-stock');
});

router.get('/comparison', function (req, res, next) {
    res.render('comparison');
});

router.get('/marketthermometer', function (req, res, next) {
    res.render('marketthermometer');
});

router.get('/about-us', function (req, res, next) {
    res.render('about-us');
});

router.route('/sign-in')
    .get(function (req, res, next) {
        res.render('sign-in');
    })
    .post(function (req, res, next) {
        //TODO 登录页面判断用户名、密码逻辑
        res.redirect(302, '/myaccount');
    });

router.route('/sign-up')
    .get(function (req, res, next) {
        res.render('sign-up');
    })
    .post(function (req, res, next) {
        //TODO 注册页面判断逻辑
        res.redirect(302, '/sign-in');
    });


module.exports = router;