package org.lis.aws.s3.entity;

public class DeleteAllRequest
{
	private static final String INVALID_FORMAT = "Invalid Arguments. Correct format : DELETE_ALL -A <awsAccessKey> -S <awsSecretKey> -B <bucket> -L [logMode]";

	private String awsAccessKey;
	private String awsSecretKey;
	private String bucket;
	private LogMode logMode = LogMode.CONSOLE;

	public DeleteAllRequest(String args[])
	{
		try
		{
			for(int index = 1; index < args.length; index += 2)
			{
				if("-A".equals(args[index]) && index + 1 < args.length)
				{
					awsAccessKey = args[index + 1];
				}
				else if("-S".equals(args[index]) && index + 1 < args.length)
				{
					awsSecretKey = args[index + 1];
				}
				else if("-B".equals(args[index]) && index + 1 < args.length)
				{
					bucket = args[index + 1];
				}
				else if("-L".equals(args[index]) && index + 1 < args.length)
				{
					logMode = LogMode.valueOf(args[index + 1]);
				}
			}
		}
		catch(Exception e)
		{
			throw new RuntimeException(INVALID_FORMAT);
		}
	}

	public void validate()
	{
		if(null == awsAccessKey || null == awsSecretKey || null == bucket)
		{
			throw new RuntimeException(INVALID_FORMAT);
		}
	}

	public String getAwsAccessKey() {
		return awsAccessKey;
	}
	public String getAwsSecretKey() {
		return awsSecretKey;
	}
	public String getBucket() {
		return bucket;
	}
	public LogMode getLogMode() {
		return logMode;
	}
}
