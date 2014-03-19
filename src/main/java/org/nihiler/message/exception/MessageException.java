package org.nihiler.message.exception;

/**
 * 数据传输异常
 * 
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @date 2012-6-20下午07:34:51
 * @version Revision: 1.0
 */
public class MessageException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MessageException() {
	}

	public MessageException(String message) {
		super(message);
	}

	public MessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageException(Throwable cause) {
		super(cause);
	}

}