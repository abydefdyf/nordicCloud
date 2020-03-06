package com.cloud.stady.nordicCloud.exceptions;

public class NotFoundFileException extends Exception {
	public NotFoundFileException() {
		super("Такого файла не существует");
	}

}
