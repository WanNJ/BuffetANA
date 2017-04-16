package dataserviceimpl.strategy;

import dataservice.strategy.StategyHistoryDAO;
import po.SingleStrategyPO;
import po.UserStrategyPO;
import util.SaveResult;

import java.io.File;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/4/16.
 */
public enum StategyHistoryDAOImpl implements StategyHistoryDAO {
    STATEGY_HISTORY_DAO;


    /**
     *
     */
    final String saveLoc = "../Data/StrategyHistory/";

    StategyHistoryDAOImpl(){
        File file  = new File(saveLoc);
        if(!file.exists()){
            file.mkdirs();
        }
    }
    @Override
    public SaveResult saveSingle(SingleStrategyPO singleStrategyPO, boolean overWrite) {
        return null;
    }

    @Override
    public SaveResult saveUser(UserStrategyPO userStrategyPO, boolean overWrite) {
        return null;
    }

    @Override
    public SingleStrategyPO getSingleStrategyPO(String name) {
        return null;
    }

    @Override
    public UserStrategyPO getUserStrategyPO(String name) {
        return null;
    }

    @Override
    public List<String> getHistoryList() {
        return null;
    }
}
