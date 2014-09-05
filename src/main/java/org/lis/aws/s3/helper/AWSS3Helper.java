package org.lis.aws.s3.helper;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.S3Object;

public class AWSS3Helper
{
	private String bucket = null;
	protected AmazonS3 s3Client = null;
	
	public AWSS3Helper(String awsAccessKey, String awsSecretKey, String bucket)
	{
		this.bucket = bucket;
		AWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
		s3Client = new AmazonS3Client(awsCredentials);
	}
	
	public InputStream get(String location)
	{
		GetObjectRequest getRequest = new GetObjectRequest(bucket, location);
		S3Object s3Object = s3Client.getObject(getRequest);
		return s3Object.getObjectContent();
	}
	
	public void put(File file, String location)
	{
		PutObjectRequest putRequest = new PutObjectRequest(bucket, location, file);
		putRequest.setMetadata(updateMetadata(new ObjectMetadata()));
		s3Client.putObject(putRequest);
	}
	
	private ObjectMetadata updateMetadata(ObjectMetadata objMetadata)
	{
		objMetadata.setCacheControl("public, max-age=31536000, must-revalidate");
		return objMetadata;
	}
	
	public void delete(String location)
	{
		DeleteObjectRequest delRequest = new DeleteObjectRequest(bucket, location);
		s3Client.deleteObject(delRequest);
	}
	
	public void delete(List<String> locations)
	{
		DeleteObjectsRequest delRequest = new DeleteObjectsRequest(bucket);
		List<KeyVersion> keys = new ArrayList<KeyVersion>();
		for(String location : locations)
		{
			keys.add(new KeyVersion(location));
		}
		delRequest.setKeys(keys);
		s3Client.deleteObjects(delRequest);
	}
}
