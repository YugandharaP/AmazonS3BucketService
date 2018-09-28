package com.bridgelabz.amazons3bucket.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;

public interface IAwsS3Bucket {

	String uploadImage(String imageFile, String imageName, String folderName) throws AmazonServiceException, AmazonClientException, IOException;

}
