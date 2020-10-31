package com.github.fabasset.exception;

public class UserPasswordWrongException extends RuntimeException {

	private static final long serialVersionUID = -7907137683274068542L;

	public UserPasswordWrongException(String password) {
        super("password is wrong : " + password);
    }
}