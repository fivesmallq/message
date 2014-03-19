package org.nihiler.message;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.nihiler.message.exception.MessageException;
import org.nihiler.message.handler.MessageHandler;

public class MessageUtils {
	private static Logger logger = Logger.getLogger(MessageUtils.class
			.getName());

	/**
	 * 根据url返回对应的对象
	 * 
	 * @param <T>
	 * @param serviceUrl
	 *            前面不需要加rmi
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T lookUp(String serviceUrl) {
		try {
			return (T) Naming.lookup("rmi://" + serviceUrl);
		} catch (MalformedURLException e) {
			logger.log(Level.WARNING, "Service url is error !",
					new MessageException(e));
		} catch (RemoteException e) {
			logger.log(Level.WARNING, "Connect to the message server failed !",
					new MessageException(e));
		} catch (NotBoundException e) {
			logger.log(Level.WARNING, "Bound message server failed !",
					new MessageException(e));
		}
		return null;
	}

	/**
	 * 启动服务
	 * 
	 * @param server
	 *            服务对象
	 * @param handler
	 *            处理实现
	 */
	public static void startServer(MessageServer server, MessageHandler handler) {
		server.serve(handler);
	}

}
