package dataserviceimpl.stockmap;

import dataservice.stockmap.StockNameToCodeDAO;
import po.StockNameAndCodePO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slow_time on 2017/3/8.
 */
public enum StockNameToCodeDAOImpl implements StockNameToCodeDAO {
    STOCK_NAME_TO_CODE_DAO_IMPL;

    public static final String MAP_FILENAME = "../Data/StockMap.csv";

    @Override
    public List<StockNameAndCodePO> getNameToCodeMap() {
        List<StockNameAndCodePO> nameToCode = null;
        BufferedReader br = null;

        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(MAP_FILENAME), "UTF-8");
            br = new BufferedReader(reader);
            nameToCode = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null) {
                String[] nameAndCode = line.split(",");
                nameToCode.add(new StockNameAndCodePO(nameAndCode[0], nameAndCode[1]));
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
