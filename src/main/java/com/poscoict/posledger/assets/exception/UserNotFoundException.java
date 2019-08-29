package com.poscoict.posledger.assets.exception;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7907137683274068542L;

	public UserNotFoundException(String id) {
        super("user's id not found : " + id);
    }
}