package blserviceimpl.comparison;

import blservice.comparison.ComparisonService;
import blservice.exception.InvalidDateException;
import blservice.exception.InvalidStockCodeException;
import vo.BasisAnalysisVO;
import vo.DailyClosingPriceVO;
import vo.DailyLogReturnVO;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Accident on 2017/3/9.
 * 两股对比服务逻辑实现代码
 */

public enum ComparisonImpl implements ComparisonService {
    COMPARISON_SERVICE;

    SingleForComparison mainStockService;
    SingleForComparison deputyStockService;


    @Override
    public void init(String mainStockCode, String deputyStockCode, LocalDate beginDate, LocalDate endDate) throws RemoteException, InvalidStockCodeException, InvalidDateException {
        mainStockService = new SingleForComparison(mainStockCode, beginDate, endDate);
        deputyStockService = new SingleForComparison(deputyStockCode, beginDate, endDate);
    }

    @Override
    public BasisAnalysisVO getMainBasisAnalysis() throws RemoteException {
        return mainStockService.getBasisAnalysis();
    }

    @Override
    public List<DailyClosingPriceVO> getMainDailyClosingPrice() throws RemoteException {
        return mainStockService.getDailyClosingPrice();
    }

    @Override
    public List<DailyLogReturnVO> getMainDailyLogReturnAnalysis() throws RemoteException {
        return mainStockService.getDailyLogReturnAnalysis();
    }

    @Override
    public double getMainLogReturnVariance() throws RemoteException {
        return mainStockService.getLogReturnVariance();
    }

    @Override
    public BasisAnalysisVO getDeputyBasisAnalysis() throws RemoteException {
        return deputyStockService.getBasisAnalysis();
    }

    @Override
    public List<DailyClosingPriceVO> getDeputyDailyClosingPrice() throws RemoteException {
        return deputyStockService.getDailyClosingPrice();
    }

    @Override
    public List<DailyLogReturnVO> getDeputyDailyLogReturnAnalysis() throws RemoteException {
        return deputyStockService.getDailyLogReturnAnalysis();
    }

    @Override
    public double getDeputyLogReturnVariance() throws RemoteException {
        return deputyStockService.getLogReturnVariance();
    }

    @Override
    public LocalDate getEarliestDate() throws RemoteException {
        if(deputyStockService.getEarliestDate().isBefore(mainStockService.getEarliestDate()))
            return deputyStockService.getEarliestDate();
        else
            return mainStockService.getEarliestDate();
    }

    @Override
    public LocalDate getLatestDate() throws RemoteException {
        if(deputyStockService.getLatestDate().isAfter(mainStockService.getLatestDate()))
            return deputyStockService.getLatestDate();
        else
            return mainStockService.getLatestDate();
    }

//    public static void main(String[] args) throws RemoteException {
//        ComparisonImpl impl = COMPARISON_SERVICE;
//        try {
//            impl.init("2932890380", "2", LocalDate.of(2014,1,6), LocalDate.of(2014,1,8));
//        } catch (InvalidStockCodeException e) {
//            e.printReason();
//            return;
//        } catch (InvalidDateException e) {
//            e.printReason();
//            return;
//        }
//        BasisAnalysisVO vo = impl.getMainBasisAnalysis();
//        System.out.println(vo.changeRate);
//    }
}
