### message [![Build Status](https://travis-ci.org/fivesmallq/message.png)](https://travis-ci.org/fivesmallq/message)

message
=========
simple rpc to send message. impl by rmi.

=========
1.Server
````java
MessageServer messageServer = new MessageServer("localhost", 1099,
				"message");
	MessageUtils.startServer(messageServer, new MessageHandler() {

		@Override
		public String to(String message) {
			System.out.println("message:" + message);
			return "get datas:" + message;
		}
	});
````
OR.
````java
new MessageServer(propsFileName);
new MessageServer(propsFile);
new MessageServer(ip,host,serveName);

````


2.Client
````java
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
````

binaries:https://github.com/fivesmallq/message/releases/download/0.0.1/message-0.0.1-SNAPSHOT.jar
