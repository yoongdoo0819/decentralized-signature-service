package com.poscoict.posledger.assets.exception;

public class AccessTokenInvalidException extends RuntimeException {

	private static final long serialVersionUID = -7907137683274068542L;

	public AccessTokenInvalidException(String message) {
        super(message);
    }
}