package blserviceimpl.strategy;

import blservice.strategy.StrategyService;
import dataservice.strategy.StrategyDAO;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;
import vo.*;

import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Created by slow_time on 2017/3/24.
 */
public class StrategyServiceImpl implements StrategyService {

    private StrategyConditionVO strategyConditionVO;
    private StockPoolConditionVO stockPoolConditionVO;
    private List<StockPickIndexVO> stockPickIndexVOs;
    private DAOFactoryService daoFactoryService;
    private StrategyDAO strategyDAO;
    private List<PickleData> pickleDatas;


    /**
     * 可以用于后期test时将工厂替换成stub
     * @param daoFactoryService
     */
    public void setFactory(DAOFactoryService daoFactoryService) {
        this.daoFactoryService = daoFactoryService;
        this.strategyDAO = this.daoFactoryService.createStrategyDAO();
        this.pickleDatas = strategyDAO.getPickleData(strategyConditionVO, stockPoolConditionVO, stockPickIndexVOs);
    }

    @Override
    public void init(StrategyConditionVO strategyConditionVO, StockPoolConditionVO stockPoolConditionVO, List<StockPickIndexVO> stockPickIndexVOs) {
        this.strategyConditionVO = strategyConditionVO;
        this.stockPoolConditionVO = stockPoolConditionVO;
        this.stockPickIndexVOs = stockPickIndexVOs;
        setFactory(new DAOFactoryServiceImpl());
    }

    @Override
    public void setStrategyConditionVO(StrategyConditionVO strategyConditionVO) {
        this.strategyConditionVO = strategyConditionVO;
    }

    @Override
    public void setStockPoolConditionVO(StockPoolConditionVO stockPoolConditionVO) {
        this.stockPoolConditionVO = stockPoolConditionVO;
    }

    @Override
    public void setStockPickIndexVOList(List<StockPickIndexVO> stockPickIndexVOs) {
        this.stockPickIndexVOs = stockPickIndexVOs;
    }

    @Override
    public BackDetailVO getBackDetailVO() {
        //TODO 基准年化收益率，暂时不知道如何处理，可能会导致多一次的文件读取
        double baseYearProfitRate = 0.124;
        // 计算策略的年化收益率
        double yearProfitRate = 0;
        for(PickleData pickleData : pickleDatas) {
            double tempRate = 0;
            for(BackData backData : pickleData.stockCodes) {
                tempRate += (backData.lastDayClose - backData.firstDayOpen) / backData.firstDayOpen;
            }
            yearProfitRate += tempRate / pickleData.stockCodes.size();
        }
        yearProfitRate = yearProfitRate / strategyConditionVO.beginDate.until(strategyConditionVO.endDate, ChronoUnit.DAYS) * 365;
        return null;
    }

    @Override
    public List<DayRatePieceVO> getDayRatePieceVO() {
        return null;
    }

    @Override
    public List<BetterTableVO> getBetterTableVO() {
        return null;
    }

    @Override
    public List<BetterPieceVO> getOverProfitRateByFormation() {
        return null;
    }

    @Override
    public List<BetterPieceVO> getOverProfitRateByHolding() {
        return null;
    }

    @Override
    public List<BetterPieceVO> getWinRateByFormation() {
        return null;
    }

    @Override
    public List<BetterPieceVO> getWinProfitRateByHolding() {
        return null;
    }

    @Override
    public List<Double> getProfitDistributeBar() {
        return null;
    }

    @Override
    public ProfitDistributionPieVO getProfitDistributePie() {
        return null;
    }
}
