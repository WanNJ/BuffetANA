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
     * @return
     */
    public boolean saveSingle(SingleStrategyPO singleStrategyPO);

    /**
     * 用于保存用户自定义策略的持久化PO
     * @param userStrategyPO
     * @return
     */
    public boolean saveUser(UserStrategyPO userStrategyPO);

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
