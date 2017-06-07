/**
 * Created by wshwbluebird on 2017/6/8.
 */

const exec = require('child_process').exec;
const testModel = './bl/statistics/ML/CNNModel.py';

exports.CNNPredict ={
    /**
     *
     * @param code
     * @param holdingPeriod
     * @param speedMode
     * @param isENV
     * @param testMode
     * @param beginStr
     * @param callback
     */
    betterPredict : (code,holdingPeriod,speedMode,isENV,testMode,beginStr,callback)=>{
        console.time('NN')
        exec('python3' + ' ' + testModel + ' ' + code + ' ' + holdingPeriod+' '+speedMode+' '+isENV+' '+testMode+' '+beginStr, function(err, stdout, stderr){
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

