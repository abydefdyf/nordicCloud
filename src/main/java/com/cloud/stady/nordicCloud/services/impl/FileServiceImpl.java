package com.cloud.stady.nordicCloud.services.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloud.stady.nordicCloud.Users;
import com.cloud.stady.nordicCloud.UserFile;
import com.cloud.stady.nordicCloud.UserFileDownloader;
import com.cloud.stady.nordicCloud.exceptions.NotFoundFileException;
import com.cloud.stady.nordicCloud.exceptions.NotFoundUserException;
import com.cloud.stady.nordicCloud.exceptions.UserAlreadyExistsException;
import com.cloud.stady.nordicCloud.exceptions.WrongPasswordException;
import com.cloud.stady.nordicCloud.exceptions.WrongUserException;
import com.cloud.stady.nordicCloud.services.FileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
	
	private long userId = 3L;
	

	@Value("${uploaded.files.path}")
	private String uploadedFilesPath;
	
	@Autowired
	private JdbcTemplate JdbcTemplate;
	

	@PostConstruct
	public void init() {
		log.info(uploadedFilesPath);
	}

	@Override
	public List<UserFile> getList() { //Получить список всех файлов и папок
		var sql = "SELECT file_name, path_to_file, type_file, owner_id, id\r\n" + 
				"	FROM public.files\r\n" + 
				"	WHERE owner_id = ?";
		return JdbcTemplate.query(sql, new Object[] {userId}, new RowMapper<UserFile>() {
			@Override
			public UserFile mapRow(ResultSet rs, int rowNum) throws SQLException{
				var userFile = new UserFile();
				userFile.setFileName(rs.getString("file_name"));
				userFile.setPathFile(rs.getString("path_to_file"));
				userFile.setId(rs.getLong("id"));
				return userFile;
			}
		});
	}

	@Override
	public void addFile(MultipartFile file) { //Добавляет файл в систему
		var sql = "INSERT INTO public.files(" + 
				" file_name, path_to_file, type_file, owner_id)" + 
				" VALUES (?, ?, ?, ?)";
		var nameFile = FilenameUtils.getExtension(file.getOriginalFilename());
		File tempFile;
		try {
			tempFile = File.createTempFile("inordic_", "_temp." + nameFile, new File(uploadedFilesPath));
			log.debug("File upload to: {}", tempFile);
			file.transferTo(tempFile);
			
			JdbcTemplate.update(sql, new Object[] {
					FilenameUtils.getName(file.getOriginalFilename()),
					tempFile.getAbsolutePath(),
					/* type_file=*/20,
					userId
			});
		} catch (IOException e) {
			log.error("addFile", e);
		}
	}
	
	@Override
	public void deleteFile(long fileId) throws NotFoundFileException, WrongUserException { //Удаляем файл из системы
		var file = getFileById(fileId);
		var sql = "DELETE FROM public.files\r\n" + 
				"	WHERE id = ?"; 
		try {
			Files.delete(Paths.get(file.getPathFile()));
		} catch (IOException e) {
			log.error("Ошибка", e);
		}
		JdbcTemplate.update(sql, new Object[] {
				fileId
		});
		
	}

	@Override
	public UserFileDownloader getFileById(long id) throws NotFoundFileException, WrongUserException { //Скачиваем файл из сисетмы
		var sql = "SELECT file_name, path_to_file, owner_id, id\r\n" + 
				"	FROM public.files\r\n" + 
				"	WHERE id = ?";
		var files = JdbcTemplate.query(sql, new Object[] {id}, new RowMapper<UserFileDownloader>() {
				@Override
				public UserFileDownloader mapRow(ResultSet rs, int rowNum) throws SQLException{
			var userFileDownloader = new UserFileDownloader();
			userFileDownloader.setFileName(rs.getString("file_name"));
			userFileDownloader.setPathFile(rs.getString("path_to_file"));
			userFileDownloader.setId(rs.getLong("id"));
			userFileDownloader.setOwnerId(rs.getLong("owner_id"));
			return userFileDownloader;
		}
	});
		if(files.isEmpty()) {
			throw new NotFoundFileException();
		}
		var file = files.get(0);
		if(file.getOwnerId() != userId) {
			throw new WrongUserException();
		}
		return file;
	}

	@Override
	public Users getUser(String login, String password) throws WrongPasswordException, NotFoundUserException { //Авторизоваться в системе
		var sql = "SELECT login, name, password, id\r\n" + 
				"	FROM public.users\r\n" + 
				"	WHERE login = ?";
		var users = JdbcTemplate.query(sql, new Object[] {login}, new RowMapper<Users>() {
			@Override
			public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
				var usersAll = new Users();
				usersAll.setLogin(rs.getString("login"));
				usersAll.setName(rs.getString("name"));
				usersAll.setPassword(rs.getString("password"));
				usersAll.setId(rs.getLong("id"));
				return usersAll;
			}
		});
		if(users.isEmpty()) {
			throw new NotFoundUserException();
		}
		
		var user = users.get(0);
//		if(!user.getPassword().equals(password)) {
//			throw new WrongPasswordException();
//		}
		
		return user;
		
	}

	@Override
	public void newUser(String login, String name, String password) throws UserAlreadyExistsException { //Добавляем нового пользователя в систему
		var sqlSelect = "SELECT login\r\n" + 
				"	FROM public.users\r\n" + 
				"	WHERE login = ?";
		var users = JdbcTemplate.query(sqlSelect, new Object[] {login}, new RowMapper<Users>() {

			@Override
			public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
				var userLogin = new Users();
				userLogin.setLogin(rs.getString("login"));
				return userLogin;
			}
		});
		
		if(!users.isEmpty()) {
			System.out.println(users);
			throw new UserAlreadyExistsException();
		}
		
		var sqlInsert = "INSERT INTO public.users(\r\n" + 
				"	login, name, password)\r\n" + 
				"	VALUES (?, ?, ?)";
		JdbcTemplate.update(sqlInsert, new Object[] {
				login,
				name,
				password
		});
		
	}
	


}
