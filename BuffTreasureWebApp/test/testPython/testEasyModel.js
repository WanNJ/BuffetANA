/**
 * Created by wshwbluebird on 2017/6/3.
 */
const exec = require('child_process').exec;
let arg1 = '000001';
let arg2 = '10';
const filename = 'testModel.py';

//令行中执行python所敲的命令一样，如果你有alias，把python3换成对应的alias就可以了
exec('python3' + ' ' + filename + ' ' + arg1 + ' ' + arg2, function(err, stdout, stderr){
    if(err) {
        console.log('stderr',err);
    }
    if(stdout) {
        ans = JSON.parse(stdout)
        console.log(ans[0]['0-2.5'])
    }

});
