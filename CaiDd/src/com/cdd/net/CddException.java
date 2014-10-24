package com.cdd.net;

public class CddException extends Exception {
	private static final long serialVersionUID = -1468334580306121816L;

	public CddException() {
	}

	public CddException(String detailMessage) {
		super(detailMessage);
	}

	public CddException(Throwable throwable) {
		super(throwable);
	}

	public CddException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

}
