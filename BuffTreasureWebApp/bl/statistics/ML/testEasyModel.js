/**
 * Created by wshwbluebird on 2017/6/3.
 */
const exec = require('child_process').exec;
const testModel = './bl/statistics/ML/testModel.py';

exports.NNPredict ={
    EasyPredict : (code,holdingPeriod,callback)=>{
        console.time('NN')
        exec('python3' + ' ' + testModel + ' ' + code + ' ' + holdingPeriod, function(err, stdout, stderr){
            if(err) {
                console.log('stderr',err);
                callback(err)
            }
            if(stdout) {
                let ans = JSON.parse(stdout)
                console.timeEnd('NN')
                callback(err,ans)
            }

        });

    }
}

