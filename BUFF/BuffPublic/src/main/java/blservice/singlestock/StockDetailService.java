package blservice.singlestock;

import vo.StockBriefInfoVO;
import vo.StockDetailVO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by slow_time on 2017/3/5.
 */
public interface StockDetailService extends Remote {

    /**
     *用户选择具体日期后，查看这支股票那天的详细信息
     * @param code 个股的编码
     * @param date 用户选择的具体日期
     * @return 该支股票此日期的详细信息
     */
    StockDetailVO getSingleStockDetails(String code, LocalDate date) throws RemoteException;

    /**
     * 获得某一个股每一天的简要信息
     * @param code 个股的名称编码
     * @return 某一个股每一天的简要信息
     */
    List<StockBriefInfoVO> getStockBriefInfo(String code) throws RemoteException;
}
