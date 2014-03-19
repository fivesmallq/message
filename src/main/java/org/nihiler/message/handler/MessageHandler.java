package org.nihiler.message.handler;

/**
 * message处理接口
 * 
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @date 2012-6-20下午04:37:58
 * @version Revision: 1.0
 */
public interface MessageHandler {
	/**
	 * 和远程服务器进行数据交互
	 * 
	 * @param message
	 * @return
	 */
	public String to(String message);

}
