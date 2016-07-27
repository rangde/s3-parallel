package org.lis.aws.s3.helper;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AWSS3Helper
{
    private static final String[] NO_CACHE_LIST = {"index.htm"};

	private String bucket = null;
	protected AmazonS3Client s3Client = null;

	public AWSS3Helper(String awsAccessKey, String awsSecretKey, String bucket, String awsRegion)
	{
		this.bucket = bucket;
		AWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
		s3Client = new AmazonS3Client(awsCredentials);
		s3Client.setRegion(RegionUtils.getRegion(awsRegion));
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
        for(String nocache : NO_CACHE_LIST)
        {
            if(!location.contains(nocache))
            {
                putRequest.setMetadata(updateMetadata(new ObjectMetadata()));
            }
        }
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

	public ObjectListing listAll(String bucket)
	{
		ListObjectsRequest listRequest = new ListObjectsRequest();
		listRequest.setBucketName(bucket);
		return s3Client.listObjects(listRequest);
	}

	public void deleteAllFromBucket(String bucket)
	{
		ObjectListing listing = listAll(bucket);
		List<String> locations = new ArrayList<String>();
		for(S3ObjectSummary summary: listing.getObjectSummaries())
		{
			locations.add(summary.getKey());
		}
		delete(locations);
	}
}
