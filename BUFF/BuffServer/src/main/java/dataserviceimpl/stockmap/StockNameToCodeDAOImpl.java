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
    public static final String MAIN_BOARD_FILENAME = "../Data/Block/主板.csv";
    public static final String SECOND_BOARD_FILENAME = "../Data/Block/创业板.csv";
    public static final String SME_BOARD_FILENAME = "../Data/Block/中小板.csv";

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
                nameToCode.add(new StockNameAndCodePO(nameAndCode[0], String.format("%6s", nameAndCode[1]).replace(" ", "0")));
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

    @Override
    public List<StockNameAndCodePO> getMainBoardStock() {
        return getByFileName(MAIN_BOARD_FILENAME);
    }

    @Override
    public List<StockNameAndCodePO> getSecondBoardStock() {
        return getByFileName(SECOND_BOARD_FILENAME);
    }

    @Override
    public List<StockNameAndCodePO> getSMEBoardStock() {
        return getByFileName(SME_BOARD_FILENAME);
    }

    private List<StockNameAndCodePO> getByFileName(String fileName) {
        List<StockNameAndCodePO> nameToCode = null;
        BufferedReader br = null;

        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
            br = new BufferedReader(reader);
            nameToCode = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null) {
                String[] nameAndCode = line.split(",");
                nameToCode.add(new StockNameAndCodePO(nameAndCode[1], nameAndCode[0]));
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
