package org.lis.aws.s3;

import org.lis.aws.s3.entity.DirPutRequest;
import org.lis.aws.s3.helper.AWSS3Helper;
import org.lis.aws.s3.helper.AuditUtil;
import org.lis.aws.s3.helper.Generator;
import org.lis.aws.s3.helper.Processor;
import org.lis.aws.s3.helper.Tracker;

public class Tool
{
	public static void main(String[] args)
	{
		if(null == args || args.length == 0 || !"PUT".equals(args[0]))
		{
			System.out.println("Invalid Arguments. PUT is the only supported mode.");
		}
		else
		{
			processDirPut(args);
		}
	}
	
	private static void processDirPut(String args[])
	{
		try
		{
			DirPutRequest dirPutRequest = new DirPutRequest(args);
			
			// Prepare AWS Helper
			AWSS3Helper awsS3Helper = new AWSS3Helper(dirPutRequest.getAwsAccessKey(), dirPutRequest.getAwsSecretKey(), dirPutRequest.getBucket());
			// Prepare Audit Loggers
			AuditUtil procAuditer = AuditUtil.getAuditUtil(dirPutRequest.getLogMode(), Processor.class);
			AuditUtil trackAuditer = AuditUtil.getAuditUtil(dirPutRequest.getLogMode(), Tracker.class);
			
			Tracker tracker = new Tracker();
			tracker.setAuditUtil(trackAuditer);
			
			Generator generator = new Generator(tracker);
			// Generate the request queue that contains all the files that are to be moved
			generator.generate(dirPutRequest.getDirPath());
			
			Processor processor = new Processor(dirPutRequest.getCorePoolSize(), dirPutRequest.getMaxPoolSize(), dirPutRequest.getKeepAliveSecs(), awsS3Helper);
			processor.setAuditUtil(procAuditer);
			// Until the queue is empty, process
			while(!tracker.isEmpty())
			{
				processor.submiPutTask(tracker.get());
			}
			// Wait for the all the tasks to complete
			processor.waitForCompletion();
			
			procAuditer.commit();
			trackAuditer.commit();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
