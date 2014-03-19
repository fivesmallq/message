package org.nihiler.message;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.nihiler.message.handler.MessageHandler;
import org.nihiler.message.service.MessageService;
import org.nihiler.message.service.MessageServiceImpl;
import org.nihiler.message.util.PropertiesParser;

/**
 * 
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @date 2012-6-20下午04:59:06
 * @version Revision: 1.0
 */
public class MessageServer {
	class ShutdownThread extends Thread {
		@Override
		public void run() {

			System.out.println("Shutdown message server success.");
			keepOn = false;
			Runtime.getRuntime().halt(0);
		}
	}

	private static Logger logger = Logger.getLogger(MessageServer.class
			.getName());

	private String ip;
	private int port;
	private String serviceName;
	private final String DEFAULT_FILE_NAME = "message.properties";
	private volatile boolean keepOn = true;

	final ShutdownThread shutdownThread = new ShutdownThread();

	PropertiesParser propertiesParser = null;

	/**
	 * 默认构造方法，使用默认参数
	 */
	public MessageServer() {
		try {
			propertiesParser = new PropertiesParser(DEFAULT_FILE_NAME);
			init(propertiesParser);
		} catch (Exception e) {
			ip = "127.0.0.1";
			port = 1099;
			serviceName = "message";
		}
	}

	/**
	 * 使用传入的properties对象构建server
	 * 
	 * @param props
	 *            会获取三个参数,properties中必须key为如下三个<br>
	 *            <li>ip</li><li>port</li><li>service_name</li>
	 */
	public MessageServer(Properties props) {
		propertiesParser = new PropertiesParser(props);
		init(propertiesParser);
	}

	/**
	 * 使用传入的properties文件名构建server
	 * 
	 * @param propsName
	 *            根据此文件名会构建properties对象，然后会获取三个参数,properties文件中必须key为如下三个<br>
	 *            <li>ip</li><li>port</li><li>service_name</li>
	 */
	public MessageServer(String propsName) {
		propertiesParser = new PropertiesParser(propsName);
		init(propertiesParser);
	}

	/**
	 * 根据提供的参数构建一个messageServer
	 * 
	 * @param ip
	 *            ip地址
	 * @param port
	 *            服务端口
	 * @param serviceName
	 *            服务名
	 */
	public MessageServer(String ip, int port, String serviceName) {
		this.ip = ip;
		this.port = port;
		this.serviceName = serviceName;
	}

	private void init(PropertiesParser propertiesParser) {
		ip = propertiesParser.getStringProperty("ip");
		port = propertiesParser.getIntProperty("port");
		serviceName = propertiesParser.getStringProperty("service_name");
	}

	/**
	 * 指定信息处理方式，然后启动服务
	 * 
	 * @param handler
	 *            一个具体的messageHandler实现
	 * @see MessageHandler
	 */
	public void serve(MessageHandler handler) {
		Runtime.getRuntime().addShutdownHook(shutdownThread);
		logger.info("init context...");
		if (keepOn) {
			try {
				// 启动RMI注册服务，指定端口为设定　（1099为默认端口）
				LocateRegistry.createRegistry(this.port);
				MessageService service = new MessageServiceImpl(handler);
				String serviceUrl = ip + ":" + port + "/" + serviceName;
				Naming.rebind("rmi://" + serviceUrl, service);
				logger.info("Message server is ready. service url : "
						+ serviceUrl);
			} catch (Exception e) {
				logger.log(Level.WARNING, "Message Server start failed!", e);
			}
		}
	}
}
