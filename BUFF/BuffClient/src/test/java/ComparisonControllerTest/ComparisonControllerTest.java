package ComparisonControllerTest;

import blservice.comparison.ComparisonService;
import factory.BlFactoryService;
import factory.BlFactoryServiceImpl;
import org.junit.Before;
import org.junit.Test;
import rmi.RemoteHelper;
import vo.BasisAnalysisVO;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;

/**
 * Created by Accident on 2017/3/12.
 */
public class ComparisonControllerTest {
    private RemoteHelper remoteHelper;
    BlFactoryService factoryService;
    ComparisonService comparisonService;

    @Before
    public void setup() {
        factoryService = new BlFactoryServiceImpl();
        comparisonService = factoryService.createComparisonService();
    }
}
