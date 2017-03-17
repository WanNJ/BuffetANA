package gui.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wshwbluebird on 2017/3/15.
 */
public enum CodeComplementUtil {
    CODE_COMPLEMENT_UTIL;

    final  String codeFile = "text/StockMap.csv";

    private List<String> CodeList;

    /**
     * 单例是初始化
     */
    CodeComplementUtil(){
        System.out.println("Construct CodeComp");

        initCodeList(codeFile);
    }

    public List<String> getComplement(String startStr){
        List<String> str =CodeList.parallelStream().filter(t->t.startsWith(startStr)).collect(Collectors.toList());
        str.sort((a,b)->a.compareTo(b));
        List<String> returnStr = str.stream().limit(5).collect(Collectors.toList());
        return returnStr;
    }

    /**
     * 一次性读取所有信息
     * @param fileName
     */
    private  void initCodeList(String fileName) {
        BufferedReader br = null;
        List<String> stringList = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = br.readLine()) != null) {
                String[] stockInfo = line.split(",");
                String temp = stockInfo[1]+"("+stockInfo[0]+")";
                stringList.add(temp);
            }
            //System.out.println(stringList.size());


        } catch (FileNotFoundException e) {
            System.out.println(fileName + " is not found");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


        this.CodeList = stringList;
    }
}
