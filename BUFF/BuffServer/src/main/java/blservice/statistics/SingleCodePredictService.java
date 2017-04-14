package blservice.statistics;

import vo.DotStaticVO;
import vo.NormalStasticVO;
import vo.PriceIncomeVO;

import java.util.List;

/**
 * Created by wshwbluebird on 2017/4/12.
 */
public interface SingleCodePredictService {
        /**
         * 返回价格 次数分布的正太分布  峰值  以及拟合的曲线
         * @param code
         * @return
         */
        public  NormalStasticVO getNormalStasticVO(String code);

        /**
         * 根据天数 和 代码 获取散点图序列
         * @param code
         * @param holdPeriod
         * @return
         */
        public List<PriceIncomeVO> getDotByPeriod(String code , int holdPeriod);


        /**
         * 获取三点图的统计信息
         * @param code
         * @return
         */
        public DotStaticVO getDotStaticVO(String code);

}
