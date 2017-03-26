package pick;

import blserviceimpl.strategy.PickleData;
import util.DayMA;

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
     * huode
     * @param code
     * @param begin
     * @param end
     * @param days
     * @return
     */
    List<DayMA>  getSingleCodeMAInfo(String code , LocalDate begin , LocalDate end , int days);




}
