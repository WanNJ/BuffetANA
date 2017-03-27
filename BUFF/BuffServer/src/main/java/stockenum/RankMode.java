package stockenum;

import blserviceimpl.strategy.BackData;
import blserviceimpl.strategy.PickleData;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wshwbluebird on 2017/3/26.
 */
public interface RankMode {
    /**
     * 获取排名的比较器
     * @param asd   是否按升序排列  即从小到大
     * @return
     */
    Comparator<BackData> getCompareRank( boolean asd);

    /**
     * 注入 要比较的数据
     * @param pickleDatas
     * @param codeList
     * @return   List<PickleData>
     */
    List<PickleData>  setRankValue(List<PickleData> pickleDatas , List<String>  codeList
            ,LocalDate begin , LocalDate end , int holdPeriod);
}
