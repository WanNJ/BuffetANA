package dataservice.stockmap;

import java.util.HashMap;

/**
 * 将所有股票的名字与它的编码一一对应
 * Created by slow_time on 2017/3/5.
 */
public interface StockNameToCodeDAO {

    HashMap<String, String> getNameToCodeMap();
}
