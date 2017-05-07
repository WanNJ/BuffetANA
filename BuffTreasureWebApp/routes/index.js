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
                    res.locals.message = "您好！" + req.body.username + "\n恭喜您登录成功！";
                    res.render('simpleHints');
                }
            }
        });
    });

router.get('/logout', function(req, res, next){
    // 备注：这里用的 session-file-store 在destroy 方法里，并没有销毁cookie
    // 所以客户端的 cookie 还是存在，导致的问题 --> 退出登陆后，服务端检测到cookie
    // 然后去查找对应的 session 文件，报错
    // session-file-store 本身的bug

    req.session.destroy(function(err) {
        if(err){
            res.json({ret_code: 2, ret_msg: '退出登录失败'});
            return;
        }

        //TODO 清空缓存
        res.clearCookie(req.session.name);
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