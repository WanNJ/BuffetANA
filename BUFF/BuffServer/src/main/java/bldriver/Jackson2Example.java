package bldriver;

import org.codehaus.jackson.map.ObjectMapper;
import po.StockPoolConditionPO;
import stockenum.StockPool;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wshwbluebird on 2017/4/17.
 */
public class Jackson2Example {
    public static void main(String[] args) {
        Jackson2Example obj = new Jackson2Example();
        obj.run();
    }

    public  void run(){


        Set<String> set = new HashSet<>();
        set.add("adsa");
        set.add("sdsdfsdsdfsdfdsdsf");
        set.add("sdsdfsdsdfsdfdsdsfssd");

        StockPoolConditionPO stockPoolConditionPO = new StockPoolConditionPO(StockPool.All,null,set,true);
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("stockPool.json"), stockPoolConditionPO);
            String jsonInString = mapper.writeValueAsString(stockPoolConditionPO);
            System.out.println(jsonInString);

            // Convert object to JSON string and pretty print
            jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(stockPoolConditionPO);
            System.out.println(jsonInString);
//            StockPoolConditionPO staff = mapper.readValue(new File("stockpool.json"), StockPoolConditionPO.class);
//            System.out.println(staff.getStockPool()+"  "+staff.isExcludeST()+"  ");
//            staff.getIndustry().forEach(t-> System.out.println(t));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
