package com.poscoict.posledger.assets.exception;

public class AccessTokenEmptyException extends RuntimeException {

	private static final long serialVersionUID = -7907137683274068542L;

	public AccessTokenEmptyException(String message) {
        super(message);
    }
}