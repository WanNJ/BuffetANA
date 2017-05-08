let express = require('express');
let userService = require('../bl/userbl');
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
    .post(function(req, res, next){
        userService.login(req.body.username, req.body.password, (err, status) => {
            if(err) {
                //TODO 数据库链接错误页面渲染
                res.locals.message = err.message;
                res.locals.error = req.app.get('env') === 'development' ? err : {};
                // 提交错误页
                res.status(err.status || 500);
                res.render('error');
            }

            if(status === false) {
                //TODO 渲染密码错误页面
                req.locals.alertType = "alert-danger";
                res.locals.message = "账号或密码错误，请重试";
                res.redirect('/sign-in');
            } else {
                let sess = req.session;

                if(sess.user){
                    req.locals.alertType = "alert-warning";
                    req.locals.alertMessage = "您已经过了，请勿重复登录。";
                    res.redirect('/');
                } else {
                    //TODO 渲染登录成功页面
                    req.session.user = req.body.username;
                    req.locals.alertType = "alert-success";
                    req.locals.alertMessage = "恭喜您登录成功！";
                    res.redirect('/');
                }
            }
        });
    });

router.get('/logout', function(req, res, next){
    req.session.destroy(function(err) {
        if(err){
            res.locals.message = err.message;
            res.locals.error = req.app.get('env') === 'development' ? err : {};

            // 提交错误页
            res.status(err.status || 500);
            res.render('error');
        }

        res.clearCookie("BuffSession");

        req.locals.alertType = "alert-success";
        req.locals.alertMessage = "已成功登出！";
        res.redirect('/');
    });
});

router.route('/sign-up')
    .get(function (req, res, next) {
        res.render('sign-up');
    })
    .post(function (req, res, next) {
        userService.signUp(req.body.username, req.body.password, req.body.email, (err, status) => {
            if(err) {
                res.locals.message = err.message;
                res.locals.error = req.app.get('env') === 'development' ? err : {};
                // 提交错误页
                res.status(err.status || 500);
                res.render('error');
            }
            if(status === false) {
                //TODO 重名渲染页面
                req.locals.alertType = "alert-danger";
                req.locals.alertMessage = "该用户名已存在，请更换您的用户名！";
                res.redirect('/sign-up');
            }else {
                req.session.user = req.body.username;

                //TODO 注册成功渲染页面
                req.locals.alertType = "alert-success";
                res.locals.alertMessage = "恭喜您注册成功！";
                res.redirect('/');
            }
        });
    });


module.exports = router;