const exec = require('child_process').exec;
let arg1 = 'hello';
let arg2 = 'world';
const filename = 'test_py.py';

exec('python3' + ' ' + filename + ' ' + arg1 + ' ' + arg2, function(err, stdout, stderr){
    if(err) {
        console.log('stderr',err);
    }
    if(stdout) {
        console.log(stdout);
    }

});


