package blserviceimpl.strategy;

import blservice.strategy.StrategyHistoryService;
import dataservice.strategy.StategyHistoryDAO;
import dataservice.strategy.StrategyDAO;
import dataserviceimpl.strategy.StategyHistoryDAOImpl;
import po.SingleStrategyPO;
import po.UserStrategyPO;
import util.SaveResult;
import vo.StrategySaveVO;

import java.util.List;

/**
 * Created by wshwbluebird on 2017/4/16.
 */
public enum  StrategyHistoryServiceImpl implements StrategyHistoryService {
    STRATEGY_HISTORY_SERVICE;

    private StategyHistoryDAO stategyHistoryDAO ;


    StrategyHistoryServiceImpl(){
        this.stategyHistoryDAO = StategyHistoryDAOImpl.STATEGY_HISTORY_DAO;
    }

    public void setStategyHistoryDAO(StategyHistoryDAO stategyHistoryDAO){
        this.stategyHistoryDAO = stategyHistoryDAO;
    }

    @Override
    public SaveResult saveStrategy(StrategySaveVO strategySaveVO, boolean overWrite) {
        List<String> userList = stategyHistoryDAO.getUserHistoryList();
        List<String> singleList = stategyHistoryDAO.getSingleHistoryList();

        String name = strategySaveVO.strategyName;
        boolean userMode = strategySaveVO.userMode;

        /**
         * 如果不是强制保存覆盖
         */
        if(overWrite) {
            if (userMode) {
                //如果策略已经在同一个要保存的列表的出现过 询问是否覆盖
                if (isAlready(userList, name))
                    return SaveResult.Already;
                //如果策略已经在不同的要保存的列表的出现过 提示名字重复错误
                if(isAlready(singleList,name))
                    return SaveResult.NameError;
            }else {
                //如果策略已经在同一个要保存的列表的出现过 询问是否覆盖
                if (isAlready(singleList, name))
                    return SaveResult.Already;
                //如果策略已经在不同的要保存的列表的出现过 提示名字重复错误
                if(isAlready(userList,name))
                    return SaveResult.NameError;
            }
        }

        //如果是要保存的
        boolean result;
        if(userMode){
            UserStrategyPO userStrategyPO = strategySaveVO.toUserStrategyPO();
            result = stategyHistoryDAO.saveUser(userStrategyPO);
        }else{
            SingleStrategyPO singleStrategyPO = strategySaveVO.toSingleStrategyPO();
            result = stategyHistoryDAO.saveSingle(singleStrategyPO);
        }

        if(!result){
            return SaveResult.fail;
        }else{
            return overWrite?SaveResult.Modify:SaveResult.Success;
        }

    }

    @Override
    public List<String> getHistoryList() {
        List<String> userList = stategyHistoryDAO.getUserHistoryList();
        List<String> singleList = stategyHistoryDAO.getSingleHistoryList();
        userList.addAll(singleList);
        return userList;
    }

    @Override
    public StrategySaveVO getStrategyHistory(String strategyName) {
        List<String> userList = stategyHistoryDAO.getUserHistoryList();
        List<String> singleList = stategyHistoryDAO.getSingleHistoryList();

        StrategySaveVO strategySaveVO = new StrategySaveVO();

        if(isAlready(userList,strategyName)){
            UserStrategyPO userStrategyPO = stategyHistoryDAO.getUserStrategyPO(strategyName);
            strategySaveVO = new StrategySaveVO(userStrategyPO);
            return strategySaveVO;
        }else if (isAlready(singleList,strategyName)){
            SingleStrategyPO singleStrategyPO = stategyHistoryDAO.getSingleStrategyPO(strategyName);
            strategySaveVO = new StrategySaveVO(singleStrategyPO);
            return strategySaveVO;
        }
        return null;
    }


    /**
     * 检测名字是否已经在特定的列表中出现过
     * @param list
     * @param name
     * @return
     */
    private boolean isAlready( List<String> list ,String name){
        for(String temp:list){
            if(temp.equals(name))  return true;
        }
        return false;
    }
}
