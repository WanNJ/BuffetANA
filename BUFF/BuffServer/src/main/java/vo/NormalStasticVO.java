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

    //均值
    public double mean;

    //标准差
    public double sigma;

    //推荐入手
    public double recIn;

    //推荐出手
    public double recOut;


    //拟合过的正太分布图
    public List<GuassLineVO> guessLine;


    public NormalStasticVO( List<RangeF> normalHist , double kurtosis ){
        this.normalHist = normalHist;
        this.kurtosis = kurtosis;

    }


    public NormalStasticVO( List<RangeF> normalHist , double kurtosis ,List<GuassLineVO> guessLine){
        this.normalHist = normalHist;
        this.kurtosis = kurtosis;
        this.guessLine = guessLine;

    }

    public double getKurtosis() {
        return kurtosis;
    }

    public void setKurtosis(double kurtosis) {
        this.kurtosis = kurtosis;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public double getRecIn() {
        return recIn;
    }

    public void setRecIn(double recIn) {
        this.recIn = recIn;
    }

    public double getRecOut() {
        return recOut;
    }

    public void setRecOut(double recOut) {
        this.recOut = recOut;
    }
}
