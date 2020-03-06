package com.cloud.stady.nordicCloud.exceptions;

public class WrongUserException extends Exception {
	public WrongUserException() {
		super("У вас нет прав для этого действия");
	}

}
