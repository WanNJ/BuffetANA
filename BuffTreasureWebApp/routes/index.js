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
                res.locals.message = "账号或密码错误！";
                res.render('simpleHints');
            } else {
                //TODO SESSION
                let sess = req.session;

                if(sess.user){
                    console.log(sess);
                    res.locals.message = sess.user + "，您已经登录过了！";
                    res.render('simpleHints');
                } else {
                    //TODO 渲染登录成功页面
                    req.session.user = req.body.username;
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
                //TODO 数据库链接错误页面渲染
                res.locals.message = err.message;
                res.locals.error = req.app.get('env') === 'development' ? err : {};
                // 提交错误页
                res.status(err.status || 500);
                res.render('error');
            }
            if(status === false) {
                //TODO 重名渲染页面
                res.locals.message = "重名！";
                res.render('simpleHints');
            }else {
                //TODO 注册成功渲染页面
                res.locals.message = "恭喜您注册成功！";
                res.render('simpleHints');
            }
        });
    });


module.exports = router;