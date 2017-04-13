package blserviceimpl.statistics;

import blservice.statistics.SingleCodePredict;
import dataservice.singlestock.StockDAO;
import dataserviceimpl.singlestock.StockDAOImpl;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.fitting.GaussianCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;
import po.StockPO;
import util.RangeF;
import vo.GuassLineVO;
import vo.NormalStasticVO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wshwbluebird on 2017/4/12.
 */
public enum SingleCodePredictImpl implements SingleCodePredict {
    SINGLE_CODE_PREDICT;



    private StockDAO stockDAO = StockDAOImpl.STOCK_DAO_IMPL;


    /**
     * 外部注入数据接口
     * @param strategyDAO
     */
    public void setStrategyDAO(StockDAO strategyDAO){
        this.stockDAO = strategyDAO;
    }


    @Override
    public NormalStasticVO getNormalStasticVO(String code) {
        return caculateNormalStasticVO(code);
    }

    /**
     * 返回直接分布直方图
     * @param code
     * @return
     */
    private NormalStasticVO caculateNormalStasticVO(String code){

        List<StockPO> list = stockDAO.getStockInFoInRangeDate(code, LocalDate.of(2012,1,1),LocalDate.of(2014,1,1));

        List<Double>  doubleList = list.stream().filter(t->t.getVolume()>0).map(t->t.getAdjCloseIndex()).collect(Collectors.toList());

        if(doubleList.size()<50)  return null;

        //分割天数
        List<RangeF> rangeFList = getDep(doubleList,30);
        List<Double> values= rangeFList.stream().map(t->(double)t.cnt).collect(Collectors.toList());

        //计算峰度
        Kurtosis kurtosis = new Kurtosis();

        double[]  vv = new double[values.size()];

        for (int i = 0 ; i < values.size() ; i++){
            vv[i] = values.get(i);
        }

        double kk = kurtosis.evaluate(vv);

        //计算拟合正太分布特征值
        WeightedObservedPoints obs = new WeightedObservedPoints();

        rangeFList.forEach(t-> obs.add((t.big+t.small)/2,t.cnt==0?t.cnt:0.1));

        double[] parameters = GaussianCurveFitter.create().fit(obs.toList());

        double peak =  parameters[0];

        double loc = parameters[1];

        double bias = parameters[2];

        NormalDistribution normal = new NormalDistribution(loc, bias);

        double norm  =  peak/normal.density(loc);

        List<GuassLineVO> guessLine = rangeFList.stream().map(t->new GuassLineVO(0.5*(t.small+t.big)
                ,norm*normal.density(0.5*(t.small+t.big)))).collect(Collectors.toList());

        return  new NormalStasticVO(rangeFList,kk,guessLine);

    }

    /**
     * 计算天数的分割
     * @param doubleList
     * @param sep
     * @return
     */
    private List<RangeF> getDep( List<Double>  doubleList , int sep){
        DoubleSummaryStatistics summaryStatistics = doubleList.stream().mapToDouble(t->(double)t).summaryStatistics();
        double max = summaryStatistics.getMax();
        double min = summaryStatistics.getMin();

        double iterval =  (max-min)/sep;
        List<RangeF> rangeFList=  new ArrayList<>();
        double beg, end;
        beg = min;
        end = min+iterval;
        for(;end<=max; beg = end ,end+=iterval){
            rangeFList.add(new RangeF(beg,end));
        }
        //System.out.println(rangeFList.size());


        rangeFList = rangeFList.stream().map(t->{
            long cnt = doubleList.stream().filter(d->d>=t.small&& d<=t.big).count();
            t.cnt = cnt;
            return t;
        }).collect(Collectors.toList());


        //rangeFList.stream().forEach(t-> System.out.println(t.small+"  "+t.big+"  "+t.cnt));


        return rangeFList;
    }



}
