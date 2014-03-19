package org.nihiler.message.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 信息传递接口
 * 
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @date 2012-6-20下午03:28:57
 * @version Revision: 1.0
 */
public interface MessageService extends Remote {

	/**
	 * 发送数据
	 * 
	 * @param message
	 * @return
	 * @throws RemoteException
	 */
	public String to(String message) throws RemoteException;

}
