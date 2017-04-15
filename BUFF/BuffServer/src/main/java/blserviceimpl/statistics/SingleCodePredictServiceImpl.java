package blserviceimpl.statistics;

import blservice.statistics.SingleCodePredictService;
import dataservice.singlestock.StockDAO;
import dataserviceimpl.singlestock.StockDAOImpl;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.fitting.GaussianCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;
import po.StockPO;
import util.RangeF;
import vo.DotStaticVO;
import vo.GuassLineVO;
import vo.NormalStasticVO;
import vo.PriceIncomeVO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wshwbluebird on 2017/4/12.
 */
public enum SingleCodePredictServiceImpl implements SingleCodePredictService {
    SINGLE_CODE_PREDICT ;





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


        int cnt =  doubleList.size();

        doubleList.sort((a,b)->(a>b?1:-1));

        int minInd = cnt/10;


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

        rangeFList.forEach(t-> obs.add((t.big+t.small)/2,t.cnt==0?0.1:t.cnt));

        double[] parameters = GaussianCurveFitter.create().fit(obs.toList());

        double peak =  parameters[0];

        double loc = parameters[1];

        double bias = parameters[2];

        System.out.println("loc:  "+loc);

        NormalDistribution normal = new NormalDistribution(loc, bias);

        double norm  =  peak/normal.density(loc);

        List<GuassLineVO> guessLine = rangeFList.stream().map(t->new GuassLineVO(0.5*(t.small+t.big)
                ,norm*normal.density(0.5*(t.small+t.big)))).collect(Collectors.toList());



        NormalStasticVO normalStasticVO =  new NormalStasticVO(rangeFList,kk,guessLine);

        normalStasticVO.setMean(loc);

        normalStasticVO.setSigma(bias);

        normalStasticVO.setRecIn(doubleList.get(minInd));

        normalStasticVO.setRecOut(loc*2-doubleList.get(minInd));



        return normalStasticVO;

    }


    @Override
    public List<PriceIncomeVO> getDotByPeriod(String code, int holdPeriod) {
        List<StockPO> list = stockDAO.getStockInFoInRangeDate(code, LocalDate.of(2013,1,1),LocalDate.of(2014,1,1));

        list = list.stream().filter(t->t.getVolume()>0).collect(Collectors.toList());

        List<PriceIncomeVO>  incomeVOList = new ArrayList<>();
        for (int i = 0 ; i < list.size()-holdPeriod-5; i++){

            double closemax =  getMax( list.get(i+holdPeriod).getAdjCloseIndex(),
                              list.get(i+holdPeriod+1).getAdjCloseIndex(),
                              list.get(i+holdPeriod-1).getAdjCloseIndex(),
                              list.get(i+holdPeriod-2).getAdjCloseIndex(),
                              list.get(i+holdPeriod+2).getAdjCloseIndex());

         //   double closemax = list.get(i+holdPeriod).getAdjCloseIndex();
            double cur  = closemax/list.get(i).getAdjCloseIndex();
            //cur = Math.log(cur);

            incomeVOList.add(new PriceIncomeVO(list.get(i).getAdjCloseIndex()
                    ,cur));
        }
        incomeVOList.forEach(t-> System.out.println(t.price+"  "+t.incomeRate));
        incomeVOList = incomeVOList.stream().sorted((a,b)->a.price>b.price?1:-1).collect(Collectors.toList());
        return incomeVOList;
    }

    private double getMax(double ...close) {
        double maxC = 0;

        for(int i = 0 ; i < close.length ; i++){
            maxC = maxC>close[i]? maxC:close[i];
        }

        return maxC;
    }

    @Override
    public DotStaticVO getDotStaticVO(String code) {
        return null;
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
