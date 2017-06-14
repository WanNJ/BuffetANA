/**
 * Created by wshwbluebird on 2017/6/8.
 */

const exec = require('child_process').exec;
const testModel = './bl/statistics/ML/NNAfterRFC.py';

exports.RFCPredict ={
    /**
     *
     * @param code  股票代码
     * @param holdingPeriod  持仓天数  1-10天
     * @param callback  形如(err,doc)
     * doc {JSON)
     * {
  [{
  "importance":   参数的重要性   数组  数组内部是是个数组  【参数英文名，参数重要程度】
  [["upper", 0.053139083076076496], ["RSI24", 0.04712154636869235]
  , ["RSI6", 0.04457809311730247], ["WR2", 0.03873952958064584]
  , ["RSI12", 0.03653828729115833], ["BIAS6", 0.03634901648373344]
  , ["J", 0.03580555302817594], ["D", 0.0331165231894777]
  , ["DEA", 0.032973472144207906], ["MA30", 0.03283860244233161]]

  , "down": "0.201818",    下跌 涨幅小于-2.5%
    "up": "0.267653",      上涨 涨幅大于2.5%
    "smooth": "0.53053",   平稳 涨幅在-2.5% ～ 2.5% 之间
    
    "accuracy": "0.835"   在测试集的准确度
    }]
  }
     */
    rfcPredict : (code,holdingPeriod,callback)=>{
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

