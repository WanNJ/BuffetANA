package dataservice.strategy;

import blserviceimpl.strategy.PickleData;
import po.StockPO;
import po.StockPoolConditionPO;
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
     * @return 符合筛选条件的所有股票
     */
    List<List<StockPO>> getStocksInPool(StockPoolConditionPO stockPoolConditionPO);


    List<PickleData> getPickleData(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO,
                                   List<StockPickIndexVO> stockPickIndexVOs);
}
