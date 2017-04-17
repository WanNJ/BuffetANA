package dataserviceimpl.strategy;

import dataservice.strategy.StategyHistoryDAO;
import org.codehaus.jackson.map.ObjectMapper;
import po.SingleStrategyPO;
import po.StockPoolConditionPO;
import po.UserStrategyPO;
import util.SaveResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wshwbluebird on 2017/4/16.
 */
public enum StategyHistoryDAOImpl implements StategyHistoryDAO {
    STATEGY_HISTORY_DAO;


    /**
     *
     */
    final String saveLoc = "text/historyStrategy";

    StategyHistoryDAOImpl(){
        File file  = new File(saveLoc);
        if(!file.exists()){
            file.mkdirs();
        }
        //用户自定义的文件夹
        File userdir  = new File(saveLoc+"user/");
        if(!userdir.exists()){
            userdir.mkdirs();
        }

        //动量策略或者均值策略的文件夹
        File singledir  = new File(saveLoc+"single/");
        if(!singledir.exists()){
            singledir.mkdirs();
        }
    }
    @Override
    public boolean saveSingle(SingleStrategyPO singleStrategyPO) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(saveLoc+"single/"+singleStrategyPO.getStrategyName()+".json"), singleStrategyPO);
            String jsonInString = mapper.writeValueAsString(singleStrategyPO);
            System.out.println(jsonInString);
            // Convert object to JSON string and pretty print
            jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(singleStrategyPO);
            System.out.println(jsonInString);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveUser(UserStrategyPO userStrategyPO) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(saveLoc+"user/"+userStrategyPO.getStrategyName()+".json"), userStrategyPO);
            String jsonInString = mapper.writeValueAsString(userStrategyPO);
            System.out.println(jsonInString);
            // Convert object to JSON string and pretty print
            jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userStrategyPO);
            System.out.println(jsonInString);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public SingleStrategyPO getSingleStrategyPO(String name) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            SingleStrategyPO singleStrategyPO  = mapper.readValue
                    (new File(saveLoc+"single/"+name+".json"), SingleStrategyPO.class);
            return singleStrategyPO;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UserStrategyPO getUserStrategyPO(String name) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            UserStrategyPO userStrategyPO  = mapper.readValue
                    (new File(saveLoc+"user/"+name+".json"), UserStrategyPO.class);
            return userStrategyPO;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> getUserHistoryList() {
        try {
            List<String> list = Files.list(Paths.get(saveLoc+"user/"))
                    .map(t->t.getFileName().toString())
                    .map(t->t.substring(0,t.length()-5))
                    .collect(Collectors.toList());

            return  list;
        } catch (IOException e) {
            e.printStackTrace();
            return  new ArrayList<>();
        }
    }

    @Override
    public List<String> getSingleHistoryList() {
        try {
            List<String> list = Files.list(Paths.get(saveLoc+"single/"))
                    .map(t->t.getFileName().toString())
                    .map(t->t.substring(0,t.length()-5))
                    .collect(Collectors.toList());

            return  list;
        } catch (IOException e) {
            e.printStackTrace();
            return  new ArrayList<>();
        }
    }

}
