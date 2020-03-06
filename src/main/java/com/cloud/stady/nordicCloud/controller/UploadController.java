package com.cloud.stady.nordicCloud.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cloud.stady.nordicCloud.services.FileService;


@Controller
public class UploadController {
	@Autowired
	private FileService fileService;

	@GetMapping(path = "/uploadCloud.html")
	public String uploadCloud() {
		return "uploadCloud";
	}

	@PostMapping(path = "/uploadCloud")
	public String uploadCloud(@RequestParam(name = "file_upload") MultipartFile file) throws Exception {
		fileService.addFile(file);

		return "uploadCloud";
	}


}
