package org.nihiler.message;

import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.Test;
import org.nihiler.message.handler.MessageHandler;
import org.nihiler.message.model.Message;
import org.nihiler.message.service.MessageService;

public class MessageServerTest {
	private static Logger logger = Logger.getLogger(MessageServer.class
			.getName());

	@Test
	public void testServe() {
		// 如果有多种情况可以在发送的message中加字段标识（使用json格式）,具体参见message类
		// 也可以修改源代码扩展
		MessageServer messageServer = new MessageServer("localhost", 1099,
				"message");
		MessageUtils.startServer(messageServer, new MessageHandler() {

			@Override
			public String to(String message) {
				System.out.println("message:" + message);
				return "get datas:" + message;
			}
		});
		try {
			MessageService messageService = MessageUtils
					.lookUp("localhost:1099/message");
			Message message = new Message("delete", "1");
			String str = messageService.to(message.toString());
			Map<String, String> update_data = new Hashtable<String, String>();
			update_data.put("id", "1");
			update_data.put("id", "2");
			update_data.put("id", "3");
			message = new Message("updateAll", update_data.toString());
			str = messageService.to(message.toString());
			logger.info("returned " + str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
