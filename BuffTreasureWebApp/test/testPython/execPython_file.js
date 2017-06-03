const exec = require('child_process').exec;
let arg1 = 'hello';
let arg2 = 'world';
const filename = 'test_py.py';

// 与命令行中执行python所敲的命令一样，如果你有alias，把python3换成对应的alias就可以了
exec('python3' + ' ' + filename + ' ' + arg1 + ' ' + arg2, function(err, stdout, stderr){
    if(err) {
        console.log('stderr',err);
    }
    if(stdout) {
        console.log(stdout);
    }

});

