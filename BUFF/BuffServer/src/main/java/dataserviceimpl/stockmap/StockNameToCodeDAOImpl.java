package dataserviceimpl.stockmap;

import dataservice.stockmap.StockNameToCodeDAO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by slow_time on 2017/3/5.
 */
public class StockNameToCodeDAOImpl implements StockNameToCodeDAO {

    public static final String MAP_FILENAME = "/Users/slow_time/BuffettANA/Data/StockMap.csv";

    @Override
    public HashMap<String, String> getNameToCodeMap() {
        HashMap<String, String> nameToCode = null;
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(MAP_FILENAME));
            nameToCode = new HashMap<>();
            String line;

            while ((line = br.readLine()) != null) {
                String[] nameAndCode = line.split(",");
                nameToCode.put(nameAndCode[0], nameAndCode[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return nameToCode;
        }
    }
}
