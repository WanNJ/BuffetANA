package blservice.comparison;

import blservice.exception.InvalidDateException;
import blservice.exception.InvalidStockCodeException;
import vo.BasisAnalysisVO;
import vo.DailyClosingPriceVO;
import vo.DailyLogReturnVO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * 两股比较接口
 * Created by Accident on 2017/3/9.
 */
public interface ComparisonService extends Remote {

    /**
     * 得到并设置日期区间（不用再次访问数据层）
     * @param mainStockCode 主股票编号
     * @param deputyStockCode 副股编号
     * @param beginDate 查询起始日期
     * @param endDate 查询结束日期
     */
    void init(String mainStockCode, String deputyStockCode, LocalDate beginDate, LocalDate endDate) throws RemoteException, InvalidStockCodeException, InvalidDateException;

    /**
     * 得到主股总体信息
     * @return BasisAnalysisVO 获得日期范围内的该支股票的基本分析数据：最低值、最高值、涨幅/跌幅，若返回值为null，则此区间无数据
     */
    BasisAnalysisVO getMainBasisAnalysis() throws RemoteException;

    /**
     * 得主股每日收盘价信息
     * @return List<DailyClosingPriceVO> 获得日期范围内该主股票每天的收盘价
     */
    List<DailyClosingPriceVO> getMainDailyClosingPrice() throws RemoteException;

    /**
     * 得到主股每日对数收益率
     * @return List<DailyLogReturnVO> 获得日期范围内主股每天的对数收益率
     */
    List<DailyLogReturnVO> getMainDailyLogReturnAnalysis() throws RemoteException;

    /**
     * 提供主股对数收益率方差
     * @return 获得日期范围内主股的对数收益率方差
     */
    double getMainLogReturnVariance() throws RemoteException;

    /**
     * 得到副股总体信息
     * @return BasisAnalysisVO 获得日期范围内副股的基本分析数据：最低值、最高值、涨幅/跌幅，若返回值为null，则此区间无数据
     */
    BasisAnalysisVO getDeputyBasisAnalysis() throws RemoteException;

    /**
     * 得副股每日收盘价信息
     * @return List<DailyClosingPriceVO> 获得日期范围内副股票每天的收盘价
     */
    List<DailyClosingPriceVO> getDeputyDailyClosingPrice() throws RemoteException;

    /**
     * 得到副股每日对数收益率
     * @return List<DailyLogReturnVO> 获得日期范围内副股每天的对数收益率
     */
    List<DailyLogReturnVO> getDeputyDailyLogReturnAnalysis() throws RemoteException;

    /**
     * 提供副股对数收益率方差
     * @return 获得日期范围内副股的对数收益率方差
     */
    double getDeputyLogReturnVariance() throws RemoteException;

    /**
     * 获得分析数据中最早一天的日期
     * @return
     */
    LocalDate getEarliestDate() throws RemoteException;

    /**
     * 获得分析数据中最晚一天的日期
     * @return
     */
    LocalDate getLatestDate() throws RemoteException;
}
