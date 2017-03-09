package blservice.comparison;

import vo.BasisAnalysisVO;
import vo.DailyClosingPriceVO;
import vo.DailyLogReturnVO;

import java.time.LocalDate;
import java.util.List;

/**
 * 两股比较接口
 * Created by Accident on 2017/3/9.
 */
public interface ComparisonService {
    /**
     * 给两股对比提供股票总体信息
     * @param stockCode 股票编号
     * @param beginDate 查询起始日期
     * @param endDate 查询结束日期
     * @return BasisAnalysisVO 获得日期范围内的该支股票的基本分析数据：最低值、最高值、涨幅/跌幅
     */
    BasisAnalysisVO getBasisAnalysis(String stockCode, LocalDate beginDate, LocalDate endDate);

    /**
     * 给两股对比提供股票在日期范围内每日收盘价信息
     * @param stockCode 股票编号
     * @param beginDate 查询起始日期
     * @param endDate 查询结束日期
     * @return List<DailyClosingPriceVO> 获得日期范围内该只股票每天的收盘价
     */
    List<DailyClosingPriceVO> getDailyClosingPrice(String stockCode, LocalDate beginDate, LocalDate endDate);

    /**
     * 给两股对比提供股票在日期范围内每天的对数收益率
     * @param stockCode 股票编号
     * @param beginDate 查询起始日期
     * @param endDate 查询结束日期
     * @return List<DailyLogReturnVO> 获得日期范围内该只股票每天的对数收益率
     */
    List<DailyLogReturnVO> getDailyLogReturnAnalysis(String stockCode, LocalDate beginDate, LocalDate endDate);

    /**
     * 提供对数收益率方差
     * @param stockCode 股票编号
     * @param beginDate 查询起始日期
     * @param endDate 查询结束日期
     * @return 获得日期范围内该只股票的对数收益率方差
     */
    double getLogReturnVariance(String stockCode, LocalDate beginDate, LocalDate endDate);
}
