package stockenum;

import blserviceimpl.strategy.BackData;
import blserviceimpl.strategy.NewPickleData;
import blserviceimpl.strategy.PickleData;
import vo.StockPickIndexVO;

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
     * 注入排序的参数   要注意
     * 如果该股票没有形成期的参数  要把这只股票从pickledata里踢出去
     * 如果本来就没有这只股票  就跳过
     * @param pickleDatas
     * @param code
     * @param begin
     * @param end
     * @param formationPeriod
     * @return
     */
    List<PickleData>  setRankValue(List<PickleData> pickleDatas , String code
            ,LocalDate begin , LocalDate end , int formationPeriod, int index);


    /**
     * 注入 要比较的数据  最新的数据
     * @param codeList
     * @return   List<PickleData>
     */
    List<NewPickleData>  setAllValue(List<String>  codeList
            , LocalDate begin , LocalDate end , List<StockPickIndexVO> stockPickIndexVOs );
}
