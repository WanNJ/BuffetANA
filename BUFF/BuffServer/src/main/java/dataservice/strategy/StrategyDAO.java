package dataservice.strategy;

import blserviceimpl.strategy.PickleData;
import po.StockPoolConditionPO;
import stockenum.StrategyType;
import vo.StockPickIndexVO;
import vo.StockPoolConditionVO;
import vo.StrategyConditionVO;

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
         * 初始化加载好已经需要的数据    非常重要的哦!!!!  选择好要购买的股票
         * @param strategyConditionVO
         * @param stockPoolConditionVO
         * @param stockPickIndexVOs
         * @return
         */
        List<PickleData> getPickleData(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO,
                                       List<StockPickIndexVO> stockPickIndexVOs);

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



}
