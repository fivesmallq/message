package org.nihiler.message.service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.nihiler.message.handler.MessageHandler;

public class MessageServiceImpl extends UnicastRemoteObject implements
		MessageService {
	private final MessageHandler handler;
	private static final long serialVersionUID = 1L;

	public MessageServiceImpl(MessageHandler hanlder) throws RemoteException {
		this.handler = hanlder;
	}

	@Override
	public String to(String message) throws RemoteException {
		return handler.to(message);
	}

}
