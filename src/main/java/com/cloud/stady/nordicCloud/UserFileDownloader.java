package com.cloud.stady.nordicCloud;

import lombok.Data;

@Data
public class UserFileDownloader {

	private String fileName;
	private long id;
	private String pathFile;
	private long ownerId;

}
