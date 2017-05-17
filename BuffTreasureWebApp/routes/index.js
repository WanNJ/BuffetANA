let express = require('express');
let userService = require('../bl/userbl');
let router = express.Router();

router.use((req, res, next) => {
    if (req.session.alertType !== undefined) {
        res.locals.alertType = req.session.alertType;
        res.locals.alertMessage = req.session.alertMessage;

        req.session.alertType = undefined;
        req.session.alertMessage = undefined;
    }
    next();
});

router.get('/', function (req, res, next) {
    res.render('index');
});

/* GET Other Menu Pages 'next'在下面的工作中可能会用到 */
router.get('/market', function (req, res, next) {
    res.render('market');
});

router.get('/single-stock', function (req, res, next) {
    if(req.session.singleStock)
        res.locals.singleStock = req.session.singleStock;

    res.render('single-stock');
});

router.get('/about-us', function (req, res, next) {
    res.render('about-us');
});

router.route('/sign-in')
    .get(function (req, res, next) {
        res.render('sign-in');
    })
    .post(function (req, res, next) {
        userService.login(req.body.username, req.body.password, (err, status) => {
            if (err)
                res.render('error');

            if (status === false) {
                res.locals.alertType = "alert-danger";
                res.locals.alertMessage = "账号或密码错误，请重试";

                res.render('sign-in');
            } else {
                let sess = req.session;

                if (sess.user) {
                    //重复登录，理论上不可能，但要防止攻击
                    req.session.alertType = "alert-warning";
                    req.session.alertMessage = "请勿重复登录！";
                    res.redirect('/');
                } else {
                    //登录成功
                    req.session.user = req.body.username;

                    req.session.alertType = "alert-success";
                    req.session.alertMessage = "恭喜您登录成功！";
                    res.redirect('/');
                }
            }
        });
    });

router.get('/logout', function (req, res, next) {
    req.session.destroy(function (err) {
        if (err)
            res.render('error');

        res.clearCookie("BuffSession");
        res.redirect('/');
    });
});

router.route('/sign-up')
    .get(function (req, res, next) {
        res.render('sign-up');
    })
    .post(function (req, res, next) {
        /** @namespace req.body.confirm_password */
        if(req.body.password !== req.body.confirm_password){
            res.locals.alertType = "alert-danger";
            res.locals.alertMessage = "两次输入的密码不一致！";
            res.render('sign-up');
        }

        userService.signUp(req.body.username, req.body.password, req.body.email, (err, status) => {
            if (err)
                res.render('error');

            if (status === false) {
                //重名渲染页面
                res.locals.alertType = "alert-warning";
                res.locals.alertMessage = "该用户名已存在，请更换您的用户名！";
                res.render('sign-up');
            } else {
                req.session.user = req.body.username;

                //注册成功渲染页面
                req.session.alertType = "alert-success";
                req.session.alertMessage = "恭喜您注册成功！系统已经帮你自动登录！";
                res.redirect('/');
            }
        });
    });


module.exports = router;