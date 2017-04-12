package dataservicestub;

import dataservice.stockmap.StockNameToCodeDAO;
import po.StockNameAndCodePO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Accident on 2017/3/17.
 */
public class StockNameToCodeDAOImpl_stub implements StockNameToCodeDAO {
    @Override
    public List<StockNameAndCodePO> getNameToCodeMap() {
        List<StockNameAndCodePO> list = new ArrayList<StockNameAndCodePO>();
        StockNameAndCodePO po = new StockNameAndCodePO("深发展A", "1");
        list.add(po);
        return list;
    }

    @Override
    public List<StockNameAndCodePO> getMainBoardStock() {
        return null;
    }

    @Override
    public List<StockNameAndCodePO> getSecondBoardStock() {
        return null;
    }

    @Override
    public List<StockNameAndCodePO> getSMEBoardStock() {
        return null;
    }

    @Override
    public List<StockNameAndCodePO> getSameIndustryStocks(String industry) {
        return null;
    }
}
