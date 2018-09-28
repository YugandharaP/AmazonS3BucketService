package com.bridgelabz.amazons3bucket.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Service
public class AwsS3BucketImplementation implements IAwsS3Bucket {

	private static final String SUFFIX = "/";
	
	@Value("${bucketName}")
	String bucketName;
	

	AWSCredentials credentials = new BasicAWSCredentials("****", "****");

	/**
	 * @param bucketName
	 * @param folderName
	 * <p>To create folder in s3 bucket on AWS</p>
	 * @param s3client
	 */
	public void createFolder(String bucketName, String folderName, AmazonS3 s3client) {
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName + SUFFIX, emptyContent,
				metadata);
		// send request to S3 to create folder
		s3client.putObject(putObjectRequest);
	}

	/**
	 * @param bucketName2
	 * @param folderName
	 * @param innerFolderName
	 * <p>To create folder inside folder in s3 bucket on AWS</p>
	 * @param client
	 */
	public void createFolder(String bucketName2, String folderName, String innerFolderName, AmazonS3 client) {
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
				folderName + SUFFIX + innerFolderName + SUFFIX, emptyContent, metadata);
		// send request to S3 to create folder
		client.putObject(putObjectRequest);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bridgelabz.amazons3bucket.service.IAwsS3Bucket#uploadImage(org.
	 * springframework.web.multipart.MultipartFile, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String uploadImage(String absoluteFilePath, String folderName, String innerFolderName)
			throws AmazonServiceException, AmazonClientException, IOException {
		AmazonS3 s3client = new AmazonS3Client(credentials);

		s3client.createBucket(bucketName);
		createFolder(bucketName, folderName, s3client);// folder created
		createFolder(bucketName, folderName, innerFolderName, s3client);// inner folder created

		File imageFile = new File(absoluteFilePath);
		String imageName = imageFile.getName();

		String imageFilePathInS3 = folderName + SUFFIX + innerFolderName + SUFFIX + imageName;

		s3client.putObject(bucketName, imageFilePathInS3, imageFile);

		String folderPath = folderName + SUFFIX + innerFolderName;

		String imageUrl = getImageUrl(folderPath, imageName);

		return imageUrl;
	}

	/**
	 * @param file
	 * <p>To convert multipart file to java file</p>
	 * @return converted file
	 * @throws IOException
	 */
	public File convertMultipartFileToJavaFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		String filename = file.getOriginalFilename();
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	/**
	 * @param folderName
	 * @param fileName
	 * <p>To getting image url from s3 object
	 * @return image url
	 */
	public String getImageUrl(String folderPath, String fileName) {
		AmazonS3 s3client = new AmazonS3Client(credentials);

		String folder = folderPath + SUFFIX;

		List<S3ObjectSummary> fileList = s3client.listObjects(bucketName, folder).getObjectSummaries();

		String imageUrl = null;
		for (S3ObjectSummary file : fileList) {
			if ((folder + fileName).equals(file.getKey())) {
				imageUrl = ((AmazonS3Client) s3client).getResourceUrl(bucketName, file.getKey());
			}
		}
		return imageUrl;
	}
}
