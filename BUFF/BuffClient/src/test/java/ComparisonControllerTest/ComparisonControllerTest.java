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
        linkToServer();
        factoryService = new BlFactoryServiceImpl();
        comparisonService = factoryService.createComparisonService();
    }

    @Test
    public void testBasisData() throws RemoteException {
        LocalDate beginDate = LocalDate.of(2014, 5, 1);
        LocalDate endDate = LocalDate.of(2014, 5, 10);
        BasisAnalysisVO basisAnalysis = null;
        comparisonService.resetDateRange("1", beginDate, endDate);
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




    private void linkToServer(){
        try {
            //未来或改为文件输入输出
//			File file = new File("TxtData/port.txt");
//			InputStreamReader reader = new InputStreamReader(new FileInputStream(
//					file), "UTF-8");
//			BufferedReader br = new BufferedReader(reader);
//			String str = br.readLine();
//			String port = str.trim();
//			br.close();
//			reader.close();
//			System.out.println(port);
            remoteHelper = RemoteHelper.getInstance();
            System.out.println("ready to link");
            remoteHelper.setRemote(Naming.lookup("rmi://"+"localhost"+":8888/DataRemoteObject"));
            System.out.println("linked");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
