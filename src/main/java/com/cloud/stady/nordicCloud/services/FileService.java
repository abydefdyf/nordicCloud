package com.cloud.stady.nordicCloud.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cloud.stady.nordicCloud.Users;
import com.cloud.stady.nordicCloud.UserFile;
import com.cloud.stady.nordicCloud.UserFileDownloader;
import com.cloud.stady.nordicCloud.exceptions.NotFoundFileException;
import com.cloud.stady.nordicCloud.exceptions.NotFoundUserException;
import com.cloud.stady.nordicCloud.exceptions.UserAlreadyExistsException;
import com.cloud.stady.nordicCloud.exceptions.WrongPasswordException;
import com.cloud.stady.nordicCloud.exceptions.WrongUserException;

public interface FileService {
	/**
	 * Получить список всех файлов и папок
	 * 
	 * @return
	 */
	List<UserFile> getList();

	/**
	 * Добавляет файл в систему
	 * 
	 * @param name
	 */
	void addFile(MultipartFile file);

	/**
	 * Удаляем файл из системы
	 */
	void deleteFile(long fileId) throws NotFoundFileException, WrongUserException;

	/**
	 * Скачиваем файл из сисетмы
	 */
	UserFileDownloader getFileById(long id) throws NotFoundFileException, WrongUserException;

	/**
	 * Авторизоваться в системе
	 * 
	 * @param id
	 * @return
	 */
	Users getUser(String login, String password) throws WrongPasswordException, NotFoundUserException;
	
	/**
	 * Добавляем нового пользователя в систему
	 * @param login
	 * @param name
	 * @param password
	 */
	void newUser(String login, String name, String password) throws UserAlreadyExistsException ;

}
