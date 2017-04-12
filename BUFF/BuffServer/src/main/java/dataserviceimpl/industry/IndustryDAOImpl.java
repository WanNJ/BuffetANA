package dataserviceimpl.industry;

import dataservice.industry.IndustryDAO;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by slow_time on 2017/4/12.
 */
public enum IndustryDAOImpl implements IndustryDAO {
    INDUSTRY_DAO;
    @Override
    public List<String> getIndustry() {
        List<String> industries = null;
        BufferedReader br = null;

        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream("../Data/StockIndustry.csv"), "UTF-8");
            br = new BufferedReader(reader);
            String line;

            while ((line = br.readLine()) != null) {
                String[] temp = line.split(",");
                industries = Arrays.asList(temp);
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
            return industries;
        }
    }

    @Override
    public String getIndustryByCode(String code) {
        BufferedReader br = null;
        String industry = null;

        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream("../Data/SingleStock.csv"), "UTF-8");
            br = new BufferedReader(reader);
            String line;

            while ((line = br.readLine()) != null) {
                String[] temp = line.split(",");
                if(temp[0].equals(code)) {
                    industry = temp[2];
                    break;
                }
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
            return industry;
        }
    }

//    public static void main(String[] args) {
//        IndustryDAO industryDAO = IndustryDAOImpl.INDUSTRY_DAO;
//        String industry = industryDAO.getIndustryByCode("000001");
//        System.out.println(industry);
//    }
}



