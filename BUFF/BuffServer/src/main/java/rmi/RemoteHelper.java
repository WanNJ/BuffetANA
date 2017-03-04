package rmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RemoteHelper {
	public RemoteHelper(){
		//System.out.println("df");
		initServer();
		System.out.println("init finish");
	}
	
	public void initServer(){
		DataRemoteObject dataRemoteObject;
		try {
			dataRemoteObject = new DataRemoteObject();
			System.out.println("ok1");
			LocateRegistry.createRegistry(8888);
			System.out.println("ok2");
			Naming.bind("rmi://localhost:8888/DataRemoteObject",
					dataRemoteObject);
			System.out.println("bind well");
		
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
		
	}
}
