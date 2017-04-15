package blservice.strategy;

import util.SaveResult;
import vo.StrategySaveVO;

import java.util.List;

/**
 * Created by wshwbluebird on 2017/4/16.
 */
public interface StrategyHistoryService {

    /**
     * 保存当前界面的策略信息
     * @param strategySaveVO
     * @param overWrite   是否强制覆盖
     * @return
     */
    public SaveResult saveStrategy(StrategySaveVO strategySaveVO , boolean overWrite);

    /**
     * 返回保存过的所有策略的名字
     * @return
     */
    public List<String> getHistoryList();

    /**
     * 根据策略名字 获取保存的策略
     * @param strategyName
     * @return
     */
    public StrategySaveVO getStrategyHistory(String strategyName);
}
