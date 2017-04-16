package SinglePort;

import blservice.comparison.ComparisonService;
import blservice.market.MarketService;
import blservice.singlestock.*;
import blservice.statistics.IndustryCorrelationService;
import blservice.statistics.SingleCodePredictService;
import blservice.strategy.IndustryAndBoardService;
import blservice.strategy.StrategyHistoryService;
import blservice.strategy.StrategyService;
import blservice.thermometer.ThermometerService;
import blserviceimpl.comparison.ComparisonImpl;
import blserviceimpl.market.MarketServiceImpl;
import blserviceimpl.singlestock.*;
import blserviceimpl.statistics.IndustryCorrelationServiceImpl;
import blserviceimpl.statistics.SingleCodePredictServiceImpl;
import blserviceimpl.strategy.IndustryAndBoardServiceImpl;
import blserviceimpl.strategy.StrategyHistoryServiceImpl;
import blserviceimpl.strategy.StrategyServiceImpl;
import blserviceimpl.thermometer.ThermometerServiceImpl;

/**
 * Created by wshwbluebird on 2017/3/26.
 */
public enum SingleBlService {
    SINGLE_BL_SERVICE;

    public KLineService getKLineService() {
        return KLineServiceImpl.K_LINE_SERVICE;
    }

    public MALineService getMALineService() {
        return MALineServiceImpl.MA_LINE_SERVICE;
    }

    public StockDetailService getStockDetailService() {
        return StockDetailServiceImpl.STOCK_DETAIL_SERVICE;
    }

    public VolService getVolService() {
        return VolServiceImpl.VOL_SERVICE;
    }

    public AllStockService getAllStockService() {
        return AllStockServiceImpl.ALL_STOCK_SERVICE;
    }

    public ComparisonService getComparisonService() {
        return ComparisonImpl.COMPARISON_SERVICE;
    }

    public MarketService getMarketService() {
        return MarketServiceImpl.MARKET_SERVICE;
    }

    public ThermometerService getThermometerService() {
        return ThermometerServiceImpl.THERMOMETER_SERVCE;
    }

    public BenchStockService getBenchStockService() {
        return BenchStockServiceImpl.BENCH_STOCK_SERVICE;
    }

    public StrategyService getStrategyService() {
        return StrategyServiceImpl.STRATEGY_SERVICE;
    }

    public IndustryCorrelationService getIndustryCorrelationService() {
        return IndustryCorrelationServiceImpl.INDUSTRY_CORRELATION_SERVICE;
    }

    public IndustryAndBoardService getIndustryAndBoardService() {
        return IndustryAndBoardServiceImpl.INDUSTRY_AND_BOARD_SERVICE;
    }

    public SingleCodePredictService getSingleCodePredictService(){
        return SingleCodePredictServiceImpl.SINGLE_CODE_PREDICT;
    }

    public StrategyHistoryService getStrategyHistoryService() {
        return StrategyHistoryServiceImpl.STRATEGY_HISTORY_SERVICE;
    }
}
