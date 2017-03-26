package blservice.singlestock;

import vo.MAPieceVO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by slow_time on 2017/3/5.
 */
public interface MALineService extends Remote {

    /**
     * 获得该个股的均线图数据
     * @param code 个股的编码
     * @param beginDate 均线图的开始日期
     * @param endDate 均线图的结束日期
     * @return
     */
    List<MAPieceVO> getMAInfo(String code, LocalDate beginDate, LocalDate endDate) throws RemoteException;


}
