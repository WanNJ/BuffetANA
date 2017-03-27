package blserviceimpl.strategy;

import blservice.strategy.StrategyService;
import dataservice.strategy.StrategyDAO;
import factroy.DAOFactoryService;
import factroy.DAOFactoryServiceImpl;
import vo.*;

import java.util.List;

/**
 * Created by slow_time on 2017/3/24.
 */
public class MAMStrategyServiceImpl implements StrategyService {

    private StrategyConditionVO strategyConditionVO;
    private StockPoolConditionVO stockPoolConditionVO;
    private List<StockPickIndexVO> stockPickIndexVOs;
    private DAOFactoryService daoFactoryService;
    private StrategyDAO strategyDAO;
    private List<PickleData> pickleDatas;


    private  List<PickleData> getData() {
        return null;
    }


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
