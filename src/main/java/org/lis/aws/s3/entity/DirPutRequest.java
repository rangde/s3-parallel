package org.lis.aws.s3.entity;

import com.amazonaws.util.StringUtils;

public class DirPutRequest
{
	private static final String INVALID_FORMAT = "Invalid Arguments. Correct format : PUT -C <corePoolSize> -M <maxPoolSize> -T <keepAliveSeconds> -A <awsAccessKey> -S <awsSecretKey> -B <bucket> [-R <region>] -D <dirPath> -L [logMode]";
	
	private Integer corePoolSize = 1;
	private Integer maxPoolSize = 1;
	private Integer keepAliveSecs = 10000;
	private String awsAccessKey;
	private String awsSecretKey;
	private String bucket;
	private String awsRegion;
	private String dirPath;
	private LogMode logMode = LogMode.CONSOLE;
	
	public DirPutRequest(String args[])
	{
		try
		{
			for(int index = 1; index < args.length; index += 2)
			{
				if("-C".equals(args[index]) && index + 1 < args.length)
				{
					corePoolSize = Integer.parseInt(args[index + 1]);
				}
				else if("-M".equals(args[index]) && index + 1 < args.length)
				{
					maxPoolSize = Integer.parseInt(args[index + 1]);
				} 
				else if("-T".equals(args[index]) && index + 1 < args.length)
				{
					keepAliveSecs = Integer.parseInt(args[index + 1]);
				}
				else if("-A".equals(args[index]) && index + 1 < args.length)
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
				else if("-R".equals(args[index]) && index + 1 < args.length)
				{
					awsRegion = args[index + 1];
				}
				else if("-D".equals(args[index]) && index + 1 < args.length)
				{
					dirPath = args[index + 1];
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
		if(null == corePoolSize || null == maxPoolSize || null == keepAliveSecs || null == awsAccessKey || null == awsSecretKey || null == bucket || null == dirPath)
		{
			throw new RuntimeException(INVALID_FORMAT);
		}
		StringBuilder builder = new StringBuilder("");
		if(corePoolSize < 1)
		{
			builder.append("corePoolSize should be > 0.");
		}
		if(maxPoolSize < 1 || maxPoolSize < corePoolSize)
		{
			builder.append("maxPoolSize should be > 0 and should not be less than maxPoolSize");
		}
		if(keepAliveSecs < 1)
		{
			builder.append("keepAliveSecs should be > 0.");
		}
		String errorMessages = builder.toString();
		if(!StringUtils.isNullOrEmpty(errorMessages))
		{
			throw new RuntimeException(errorMessages);
		}
	}

	public Integer getCorePoolSize() {
		return corePoolSize;
	}
	public Integer getMaxPoolSize() {
		return maxPoolSize;
	}
	public Integer getKeepAliveSecs() {
		return keepAliveSecs;
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
	public String getDirPath() {
		return dirPath;
	}
	public LogMode getLogMode() {
		return logMode;
	}

	public String getAwsRegion() {
		return awsRegion;
	}
}
