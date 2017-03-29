var express = require('express');
// 路径解析器
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
// Cookie解析器
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var index = require('./routes/index');
var users = require('./routes/users');

var app = express();

// 视图引擎设置
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

// 把项目小图标放在 '/public' 目录下
app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));

app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

// Cookie解析器
app.use(cookieParser());

// 静态文件加载，所有public目录下的文件现在可以访问，访问静态资源文件时，express.static 中间件会根据目录添加的顺序查找所需的文件
app.use(express.static(path.join(__dirname, 'public')));

// 加载处理请求的路由模块
app.use('/', index);
app.use('/market', index);
app.use('/single-stock', index);
app.use('/comparison', index);
app.use('/marketthermometer', index);
app.use('/about-us', index);

app.use('/users', users);

// 404 ERROR
app.use(function(req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});

// 错误处理
app.use(function(err, req, res, next) {
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};

    // 提交错误页
    res.status(err.status || 500);
    res.render('error');
});

module.exports = app;
