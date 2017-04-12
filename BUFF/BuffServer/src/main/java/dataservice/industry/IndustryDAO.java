package dataservice.industry;

import java.util.List;

/**
 * Created by slow_time on 2017/4/12.
 */
public interface IndustryDAO {

    /**
     * 获得所有行业
     * @return
     */
    List<String> getIndustry();

    /**
     * 根据所给的股票代号获得这支股票所在的行业
     * ！！！一支股票只有一个行业！！！！
     * @return 该股票所在行业
     */
    String getIndustryByCode(String code);
}
