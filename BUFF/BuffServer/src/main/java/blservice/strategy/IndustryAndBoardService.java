package blservice.strategy;

import java.util.List;

/**
 * Created by slow_time on 2017/4/14.
 */
public interface IndustryAndBoardService {

    /**
     * 获得所有行业的名称
     * @return
     */
    List<String> getAllIndustries();

    /**
     * 获得所有板块的名称
     * 根据老师的要求，这一部分暂时我直接返回老师所要求的那三个板块名称
     * @return
     */
    List<String> getAllBoards();
}
