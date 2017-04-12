package blservice.statistics;

import vo.IndustryCorrelationVO;

/**
 * Created by slow_time on 2017/4/12.
 */
public interface IndustryCorrelationService {

    /**
     * 根据所给股票，选取出同行业中正相关度最高的股票，并依据此来推断可能盈利率
     * @param code 所给股票代号
     * @param holdingPeriod 持仓期
     * @return
     */
    IndustryCorrelationVO getInIndustryCorrelationResult(String code, int holdingPeriod);
}
