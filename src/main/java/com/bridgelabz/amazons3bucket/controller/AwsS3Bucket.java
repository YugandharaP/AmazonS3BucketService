package com.bridgelabz.amazons3bucket.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.bridgelabz.amazons3bucket.service.IAwsS3Bucket;

/**
 * @author adminsitrator
 *
 */
@RestController
@RequestMapping("/bucket") 
public class AwsS3Bucket {

	@Autowired
	IAwsS3Bucket bucketService;

	@PostMapping("/uploadimage")
	public String uploadImage(@RequestParam(value = "folderName") String folderName,
			@RequestParam(value = "filePath") String filePath,
			@RequestParam(value = "innerFolderName") String innerFolderName)
			throws AmazonServiceException, AmazonClientException, IOException {
		String imageUrl = bucketService.uploadImage(filePath, folderName, innerFolderName);
		return imageUrl;
	}
}
