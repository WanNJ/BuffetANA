package vo;

import util.RangeF;

import java.util.List;

/**
 * Created by wshwbluebird on 2017/4/12.
 */
public class NormalStasticVO {
    //显示直方图的 列表
    public List<RangeF> normalHist;

    //峰度
    public double kurtosis;


    //拟合过的正太分布图
    public List<GuassLineVO> guessLine;


    public NormalStasticVO( List<RangeF> normalHist , double kurtosis){
        this.normalHist = normalHist;
        this.kurtosis = kurtosis;

    }
}
