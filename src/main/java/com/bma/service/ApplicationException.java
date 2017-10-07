package com.bma.service;


public class ApplicationException extends Exception {
	
	private static final long serialVersionUID = 18349323234328L;

	public ApplicationException(String strErrorMessage, Throwable cause) {
		super(strErrorMessage, cause);
	}

}
