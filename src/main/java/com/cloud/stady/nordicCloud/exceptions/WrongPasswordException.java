package com.cloud.stady.nordicCloud.exceptions;

public class WrongPasswordException extends Exception {
	public WrongPasswordException() {
		super("Вы ввели не правильный пароль");
	}
	

}
