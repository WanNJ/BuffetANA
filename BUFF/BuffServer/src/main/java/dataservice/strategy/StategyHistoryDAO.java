package dataservice.strategy;

import po.SingleStrategyPO;
import po.UserStrategyPO;
import util.SaveResult;

import java.util.List;

/**
 * Created by wshwbluebird on 2017/4/16.
 */
public interface StategyHistoryDAO {

    /**
     * 用于保存动量策略或者均值策略的持久化PO
     * @param singleStrategyPO
     * @param overWrite
     * @return
     */
    public SaveResult saveSingle(SingleStrategyPO singleStrategyPO , boolean overWrite);

    /**
     * 用于保存用户自定义策略的持久化PO
     * @param userStrategyPO
     * @param overWrite
     * @return
     */
    public SaveResult saveUser(UserStrategyPO userStrategyPO , boolean overWrite);

    /**
     * 获取动量策略或者均值策略的持久化PO
     * @param name
     * @return
     */
    public SingleStrategyPO getSingleStrategyPO(String name);

    /**
     * 获取用户自定义策略的持久化PO
     * @param name
     * @return
     */
    public UserStrategyPO getUserStrategyPO(String name);

    /**
     * 获取所有信息的历史
     * @return
     */
    public List<String>  getHistoryList();
}
