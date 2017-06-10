/**
 * Created by wshwbluebird on 2017/6/3.
 */
const exec = require('child_process').exec;
const testModel = './bl/statistics/ML/testModel.py';


exports.NNPredict ={

    /**
     *
     * @param code   要预测的股票代码
     * @param holdingPeriod  持仓期天数
     * @param callback (err,doc)
     *
     * doc 格式 {JSON}
     *  [{"10-5": "0.0352829"    涨幅为-10%~-5% 的概率
     *  , "0-5": "0.396603"      涨幅为 0~5%    的概率
     *  , "5-10": "0.0341518"    涨幅为 5%~10%  的概率
     *  , "5-0": "0.493942"      涨幅为 -5%~0   的概率
     *  , "more10": "0.0257998"  涨幅大于 10%   的概率
     *  , "less10": "0.0142207   涨幅小于-10%   的概率
     *  , "accuracy": "0.435""}] 测试结果取概率最大时，在测试集上的准确度
     * @constructor
     */
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

