package blserviceimpl.strategy;

import blservice.strategy.StrategyHistoryService;
import util.SaveResult;
import vo.StrategySaveVO;

import java.util.List;

/**
 * Created by wshwbluebird on 2017/4/16.
 */
public enum  StrategyHistoryServiceImpl implements StrategyHistoryService {
    STRATEGY_HISTORY_SERVICE;

    @Override
    public SaveResult saveStrategy(StrategySaveVO strategySaveVO, boolean overWrite) {
        return null;
    }

    @Override
    public List<String> getHistoryList() {
        return null;
    }

    @Override
    public StrategySaveVO getStrategyHistory(String strategyName) {
        return null;
    }
}
