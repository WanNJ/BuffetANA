package rmi;

import rmiDemo.RMIDemoService;

import java.rmi.Remote;

/**
 * 
 * @author wshwbluebird
 *
 */
public class RemoteHelper {
	private Remote remote;
	private static RemoteHelper remoteHelper = new RemoteHelper();
	public static RemoteHelper getInstance(){
		return remoteHelper;
	}
	
	private RemoteHelper() {
	}
	
	public void setRemote(Remote remote){
		this.remote = remote;
	}

	/**
	 * RMIDemoService RMI调用
	 * @return RMIDemoService
	 */
	public RMIDemoService getRMIDemoService(){
		return (RMIDemoService)remote;
	}


	
}
