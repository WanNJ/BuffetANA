﻿let express = require('express');
// 路径解析器
let path = require('path');
let favicon = require('serve-favicon');
let logger = require('morgan');

let gzippo = require('gzippo');

// session解析
let session = require('express-session');

let bodyParser = require('body-parser');
// MongoDB
let mongoose = require('mongoose');

let index = require('./routes/index');
let users = require('./routes/users');
let singleStock = require('./routes/singleStock');
let market = require('./routes/market');

let app = express();

let realTimeTool = require('./bl/realtime/singleStockRT');

let singleStockbl = require('./bl/singleStockbl');

let strategyRTTool = require('./bl/realtime/strategyRT');
// 视图引擎设置
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

// 把项目小图标放在 '/public' 目录下
app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));

app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
// Session中间件
app.use(session({
    name: 'BuffSession',
    secret: 'JackWan',          // 用来对session id相关的cookie进行签名
    // TODO SESSION的存储方式
    // store: new FileStore(),     // 本地存储session（文本文件，也可以选择其他store，比如redis的）
    saveUninitialized: false,   // 是否自动保存未初始化的会话，建议false
    resave: false,              // 是否每次都重新保存会话，建议false
    cookie: {
        httpOnly: true,         // 是否不允许客户端通过JS访问Cookie
        maxAge: 24 * 60 * 60 * 1000  // 有效期，单位是毫秒
    }
}));

app.use( (req, res, next) => {
    if(req.session.user) {
        res.locals.user = req.session.user;
    }
    next();
});

// 静态文件加载，所有public目录下的文件现在可以访问，访问静态资源文件时，express.static 中间件会根据目录添加的顺序查找所需的文件
app.use(express.static(path.join(__dirname, 'public')));
// 压缩为GZIP，提高性能
// app.use(gzippo.staticGzip(path.join(__dirname, 'public')));

// 加载处理请求的路由模块
app.use('/', index);
app.use('/user', users);
app.use('/market', market);
app.use('/single-stock', singleStock);

// 404 ERROR
app.use((req, res, next) => {
    let err = new Error('Not Found');
    err.status = 404;
    next(err);
});

// 错误处理
app.use((err, req, res, next) => {
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};

    // 提交错误页
    res.status(err.status || 500);
    res.render('error');
});

// 为了解决DeprecationWarning: Mongoose: mpromise (mongoose's default promise library) is deprecated

mongoose.Promise = global.Promise;
// 数据库连接 MongoDB
mongoose.connect('mongodb://localhost/latestInfo');

mongoose.connection.on('open', function () {
    console.log('Connected to Mongoose');
});

process.on('exit', () => {
   mongoose.disconnect();
});

// setInterval(realTimeTool.updateAllStockRTInfo, 300000, (err, isOK) => {
//     if (err)
//         console.log("Update real time info failed");
//     else
//         console.log("Update real time info succeed");
// });

// setInterval(realTimeTool.updateHotBoard, 300000, (err, isOK) => {
//     if (err)
//         console.log("Update hot board info failed");
//     else
//         console.log("Update hot board info succeed");
// });

// setInterval(singleStockbl.updateHotStocks, 300000, (err, isOK) => {
//     if (err)
//         console.log("Update hot stocks info failed");
//     else
//         console.log("Update hot stocks info succeed");
// });

// setInterval(strategyRTTool.WriteRTStockToDB, 300000, (err) => {
//     if (err)
//         console.log("Update RT stocks info failed");
//     else
//         console.log("Update RT stocks info succeed");
// });



module.exports = app;