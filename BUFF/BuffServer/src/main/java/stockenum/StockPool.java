package stockenum;

/**
 * Created by slow_time on 2017/3/24.
 * 股票池的所有类型
 */
public enum StockPool {
    /**
     * 所有股票
     */
    All{
        @Override
        public String toString() {
            return "全部";
        }
    },
    /**
     * 沪深300
     */
    HS300{
        @Override
        public String toString() {
            return "沪深300";
        }
    },

    /**
     * 用户自定义
     */
    UserMode{
        @Override
        public String toString() {
            return "自选股票池";
        }

    };

}
