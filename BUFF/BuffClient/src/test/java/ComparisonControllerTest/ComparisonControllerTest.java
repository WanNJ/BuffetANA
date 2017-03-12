package ComparisonControllerTest;

import blservice.comparison.ComparisonService;
import factory.BlFactoryService;
import factory.BlFactoryServiceImpl;
import org.junit.Before;
import org.junit.Test;
import vo.BasisAnalysisVO;

import java.rmi.RemoteException;
import java.time.LocalDate;

/**
 * Created by Accident on 2017/3/12.
 */
public class ComparisonControllerTest {

    BlFactoryService factoryService;
    ComparisonService comparisonService;

    @Before
    public void setup() {
        factoryService = new BlFactoryServiceImpl();
        comparisonService = factoryService.createComparisonService();
    }

    @Test
    public void testBasisData() {
        LocalDate beginDate = LocalDate.of(2014, 5, 1);
        LocalDate endDate = LocalDate.of(2014, 5, 10);
        BasisAnalysisVO basisAnalysis = null;
        try {
            basisAnalysis = comparisonService.getBasisAnalysis("1", beginDate, endDate);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println(basisAnalysis.lowPrice);
        System.out.println(basisAnalysis.highPrice);
        System.out.println(basisAnalysis.openPrice);
        System.out.println(basisAnalysis.closePrice);
        System.out.println(basisAnalysis.changeRate);
    }
}
