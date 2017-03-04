package rmiDemo;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by wshwbluebird on 2017/3/2.
 */
public interface RMIDemoService  extends Remote {

    /**
     *  RMI的范例 接口（以后被逻辑层的接口替换）
     *  注意声明异常!
     * @return String
     * @throws RemoteException
     */
    public String getRmiDemoString() throws RemoteException;
}
