package com.cloud.stady.nordicCloud.exceptions;

public class UserAlreadyExistsException extends Exception {
	public UserAlreadyExistsException() {
		super("Такой пользователь уже существует");
	}

}
