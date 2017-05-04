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

        let sess = req.session;
        let user = userService.isUserValid(req.body.name, req.body.password);

        if(user){
            req.session.regenerate(function(err) {
                if(err){
                    //TODO 渲染登录失败页面
                    return res.json({ret_code: 2, ret_msg: '登录失败'});
                }

                //TODO 渲染登录成功页面
                req.session.name = user.name;
                res.json({ret_code: 0, ret_msg: '登录成功'});
            });
        }else{
            //TODO 渲染密码错误页面
            res.json({ret_code: 1, ret_msg: '账号或密码错误'});
        }
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
        //TODO 注册页面判断逻辑
        res.redirect(302, '/sign-in');
    });


module.exports = router;