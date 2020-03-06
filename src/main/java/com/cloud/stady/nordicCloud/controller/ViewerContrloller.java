package com.cloud.stady.nordicCloud.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cloud.stady.nordicCloud.exceptions.NotFoundFileException;
import com.cloud.stady.nordicCloud.exceptions.WrongUserException;
import com.cloud.stady.nordicCloud.services.FileService;

@Controller
public class ViewerContrloller {
	@Autowired
	private FileService fileService;
	
	@GetMapping(path = "/nordicCloud.html")
	public String list(Model model) {
		model.addAttribute("files", fileService.getList());
		return "nordicCloud";
	}
	
	@GetMapping(path = "/delete_file.html")
	public String deleteFile(@RequestParam(name = "file")long fileId) throws NotFoundFileException, WrongUserException {
		fileService.deleteFile(fileId);
		return "redirect:/nordicCloud.html";
		
	}
	
	@GetMapping(path = "/download_file.html")
	public ResponseEntity<InputStreamResource> fileDownload(@RequestParam(name = "id") long id) throws WrongUserException, NotFoundFileException, FileNotFoundException{
		var metaOfFile = fileService.getFileById(id);
		
		var file = new File(metaOfFile.getPathFile());
		var resource = new InputStreamResource(new FileInputStream(file));
		
		var contentDisposition = ContentDisposition.builder("attachment")
				.filename(metaOfFile.getFileName(), StandardCharsets.UTF_8)
				.build();
		
		var headers = new HttpHeaders();
		headers.setContentDisposition(contentDisposition);
		
		return ResponseEntity.ok()
				.headers(headers)
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.contentLength(file.length())
				.body(resource);
	}

}
