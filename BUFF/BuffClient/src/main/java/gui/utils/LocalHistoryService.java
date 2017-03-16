package gui.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wshwbluebird on 2017/3/15.
 */
public enum LocalHistoryService {
    LOCAL_HISTORY_SERVICE;

    final String fileName  = "../Data/SearchHistory.txt";

    /**
     * 单例初始化!!!
     */
    LocalHistoryService(){
        File file =  new File(fileName);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取历史数据
     * @return
     */
    public List<String> getList(){
        List<String>  list =  readListFromFile();
        Collections.reverse(list);
        return list.stream().distinct().limit(40).collect(Collectors.toList());
    }


    /**
     *  从文件中读取数据
     * @return
     */
    private    List<String> readListFromFile() {
        List<String> stringList = new ArrayList<>();
        List<String> list = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {
            list = br.lines().collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }


        return list;
    }

    /**
     * 增加历史数据
     * @param code
     */
    public void addHistoryPiece(String code){
        try {
            Files.write(Paths.get(fileName),(code+'\n').getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
