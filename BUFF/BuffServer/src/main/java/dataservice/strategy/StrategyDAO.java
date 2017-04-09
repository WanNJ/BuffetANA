package dataservice.strategy;

import blserviceimpl.strategy.NewPickleData;
import blserviceimpl.strategy.PickleData;
import po.StockPoolConditionPO;
import stockenum.StrategyType;
import vo.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by slow_time on 2017/3/24.
 */
public interface StrategyDAO {

        /**
         * 获取所有符合股票池筛选条件的所有股票，构成策略的基准股票池
         * @param stockPoolConditionPO 筛选股票池的条件参数
         * @return 符合筛选条件的所有股票代码
         */
        List<String> getStocksInPool(StockPoolConditionPO stockPoolConditionPO);


        /**
         * 计算混合策略  返回原始的pickledata  即直接可计算的pickledata
         * @param beginDate
         * @param endDate
         * @param stockPoolConditionVO
         * @param stockPickIndexVOs
         * @param traceBackVO
         * @param mixedStrategyVOS
         * @return
         */
        List<PickleData> getPickleData(LocalDate beginDate , LocalDate endDate ,
                                       StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexVOs,
                                       TraceBackVO traceBackVO, List<MixedStrategyVO> mixedStrategyVOS);

        /**
         * 过滤和排序 pickledata 得到最终的要买的股票的数据
         * @param pickleDataList
         * @param stockPickIndexVOs
         * @param strategyType  策略类型
         * @param holdingNum   持股数
         * @param holdingRate  持有率  如果不是动量策略返回0
         * @param asd   是否按升序排列
         * @return   List<PickleData>
         */
        List<PickleData> rankAndFilterPickleData(List<PickleData> pickleDataList,
                                                 List<StockPickIndexVO> stockPickIndexVOs , StrategyType strategyType ,
                                                 int holdingNum , double holdingRate, boolean asd);

    /**
     * 获取新的pickledata
     * @param strategyConditionVO
     * @param stockPoolConditionVO
     * @param stockPickIndexVOs
     * @return
     */
    List<NewPickleData> getNewPickleData(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO,
                                         List<StockPickIndexVO> stockPickIndexVOs);

}
