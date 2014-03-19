package org.nihiler.message.model;

import org.nihiler.message.util.JsonUtils;

public class Message {
	private String method;
	private Object[] args = new Object[] {};

	public Message() {
	}

	public Message(String method) {
		this.method = method;

	}

	public Message(String method, Object... args) {
		this.method = method;
		this.args = args;
	}

	public Object[] getArgs() {
		return args;
	}

	public String getMethod() {
		return method;
	}

	public void setArgs(Object... args) {
		this.args = args;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * 重载了toString方法，返回此对象的json格式化串
	 */
	@Override
	public String toString() {
		return JsonUtils.encode(this);
	}
}
