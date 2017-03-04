package rmi;

import rmiDemo.RMIDemoImpl;
import rmiDemo.RMIDemoService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DataRemoteObject extends UnicastRemoteObject implements RMIDemoService {
	/**
	 *  RMI 接口
	 */
	private static final long serialVersionUID = 4029039744279087114L;
	private RMIDemoService rmiDemoService;

	protected DataRemoteObject() throws RemoteException {
		this.rmiDemoService = new RMIDemoImpl();


	}

	public String getRmiDemoString() throws RemoteException {
		return rmiDemoService.getRmiDemoString();
	}
	/**
	 * LoginCheckService的接口方法
	 */


}
