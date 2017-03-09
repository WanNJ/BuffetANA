package runner;

import rmi.RemoteHelper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientRunner {
	private RemoteHelper remoteHelper;
	
	public ClientRunner() {
		linkToServer();
		

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
