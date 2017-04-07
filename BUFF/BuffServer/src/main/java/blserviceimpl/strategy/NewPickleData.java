package blserviceimpl.strategy;

/**
 * Created by wshwbluebird on 2017/4/7.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * 新的pickledata 存储了所有的股票及其
 */
public class NewPickleData {

    /**
     * 股票代码
     */
    public String code;

    /**
     *  用于存储 第一天的上一天的 复权收盘价
     */
    public double lastAdj;
    /**
     * 股票的singleBackData数据列表
     */
    public List<SingleBackData> singleBackDataList;

    public NewPickleData(String code){
        this.code = code;
        this.singleBackDataList = new ArrayList<>();
    }
}
