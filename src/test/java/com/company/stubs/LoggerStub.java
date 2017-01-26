package com.company.stubs;

import org.apache.log4j.Logger;

public class LoggerStub extends Logger {

	private Object lastWarning;
	private Object lastError;
	private Throwable lastThrowable;
	
	protected LoggerStub(String name) {
		super(name);
	}

	public LoggerStub() {
		super("test");
	}
	
	@Override
	public void warn(Object message) {
		this.lastWarning = message;
	}
	
	public Object getLastWarning() {
		return this.lastWarning;
	}
	
	@Override
	public void error(Object message, Throwable t) {
		lastError = message;
		lastThrowable = t;
	}
	
	public Object getLastError() {
		return this.lastError;
	}
	
	public Throwable getLastThrowable() {
		return this.lastThrowable;
	}
}
