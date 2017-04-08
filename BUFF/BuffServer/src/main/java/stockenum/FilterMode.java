package stockenum;

import blserviceimpl.strategy.BackData;
import blserviceimpl.strategy.NewPickleData;
import blserviceimpl.strategy.PickleData;
import blserviceimpl.strategy.SingleBackData;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by wshwbluebird on 2017/3/26.
 */
public interface FilterMode {

    /**
     * 返回过滤器
     * @param lowerBound 下线  若为空则没有下限
     * @param upBound  上限
     * @return     predict
     */
    Predicate<BackData>  getFilter(Double lowerBound , Double upBound);


    /**
     * 对特定的股票  注入过滤的参数
     * @param current
     * @param code
     * @return
     */
    List<PickleData>  setFilterValue(List<PickleData>  current , String code);

    /**
     * 对特定的股票  注入过滤的参数*
     * @param current
     * @param code
     * @return
     */
    List<SingleBackData>  setNewFilterValue(List<SingleBackData>  current , String code , int codeIndex);

}
