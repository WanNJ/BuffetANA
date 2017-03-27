package stockenum;

import blserviceimpl.strategy.BackData;

import java.time.LocalDate;
import java.util.function.Predicate;

/**
 * Created by wshwbluebird on 2017/3/26.
 */
public interface FilterMode {

    /**
     * 返回过滤器
     * @param begin   开始时间
     * @param end     结束时间
     * @param code     股票代码
     * @param lowerBound 下线  若为空则没有下限
     * @param upBound  上限
     * @return     predict
     */
    Predicate<BackData>
    getFilter(LocalDate begin , LocalDate end , String code ,Double lowerBound , Double upBound);
}
