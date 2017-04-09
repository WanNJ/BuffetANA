package pick;

import po.StockPO;
import util.DayMA;
import blserviceimpl.strategy.PickleData;
import util.FormationMOM;
import vo.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/3/26.
 */
public interface PickStockService {

    /**
     * 按照正常方式  而不是交易日分割天数
     * @param begin
     * @param end
     * @param sep
     * @return  分割好的 信息   PickkeData 内部字符串为空
     */
    List<PickleData>  seprateDaysinCommon(LocalDate begin , LocalDate end , int sep);


    /**
     * add by wsw 0404
     * 按照易日分割天数
     * @param begin
     * @param end
     * @param sep
     * @return  分割好的 信息   PickkeData 内部字符串为空
     */
    List<PickleData>  seprateDaysByTrade(LocalDate begin , LocalDate end , int sep);


    /**
     * 获得特定天内的 均线信息
     * @param code
     * @param begin
     * @param end
     * @param days   //几日均线
     * @return  返回的是日期均线的组合列表
     */
    List<DayMA>  getSingleCodeMAInfo(String code , LocalDate begin , LocalDate end , int days);


    /**
     * 获得特定天内的股票的收益率信息
     * @param code
     * @param begin
     * @param end
     * @param formationPeriod
     * @return
     */
    List<FormationMOM> getSingleCodeMOMInfo(String code, LocalDate begin, LocalDate end, int formationPeriod);


    /**
     * 已经在stockDAO 中实现了  只是为了消除循环依赖
     * @param code
     * @param begin
     * @param end
     * @return
     */
    List<StockPO>  getSingleCodeInfo(String code , LocalDate begin , LocalDate end);


    /**
     * 获得上一日的交易量
     * @param code
     * @param begin
     * @param end
     * @return
     */
    List<LongPeiceVO>  getLastVol(String code , LocalDate begin ,LocalDate end);


    /**
     * 获得上一日的涨幅
     * @param code
     * @param begin
     * @param end
     * @return
     */
    List<UpRangeVO> getLastUpRange(String code, LocalDate begin ,LocalDate end);


    /**
     * 获得上一日的复权平均价
     * @param code
     * @param begin
     * @param end
     * @return
     */
    List<AdjVO> getAdj(String code, LocalDate begin ,LocalDate end);


    /**
     * 获得换手率
     * @param code
     * @param begin
     * @param end
     * @return
     */
    List<ChangeRateVO> getChangeRate(String code, LocalDate begin ,LocalDate end);


    /**
     * 获得流通市值
     * @param code
     * @param begin
     * @param end
     * @return
     */
    List<CirculationMarketValueVO> getCirculationMarketValue(String code, LocalDate begin ,LocalDate end);


    /**
     * 获得昨日的振幅
     * @param code
     * @param begin
     * @param end
     * @return
     */
    List<AmplitudeVO> getAmplitude(String code, LocalDate begin ,LocalDate end);


}
