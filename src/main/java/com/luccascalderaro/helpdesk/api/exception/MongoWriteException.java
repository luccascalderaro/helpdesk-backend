package com.luccascalderaro.helpdesk.api.exception;

public class MongoWriteException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MongoWriteException(String msg) {
		super(msg);
	}

	public MongoWriteException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
