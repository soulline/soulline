package com.yyl.net;

public class YylException extends Exception {
	private static final long serialVersionUID = -1468334580306121816L;

	public YylException() {
	}

	public YylException(String detailMessage) {
		super(detailMessage);
	}

	public YylException(Throwable throwable) {
		super(throwable);
	}

	public YylException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

}
