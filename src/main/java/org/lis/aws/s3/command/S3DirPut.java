package org.lis.aws.s3.command;

import org.lis.aws.s3.entity.DirPutRequest;
import org.lis.aws.s3.helper.AWSS3Helper;
import org.lis.aws.s3.helper.AuditUtil;
import org.lis.aws.s3.helper.Generator;
import org.lis.aws.s3.helper.Processor;
import org.lis.aws.s3.helper.Tracker;

public class S3DirPut implements S3Command
{
	public void process(String[] args)
	{
		try
		{
			DirPutRequest dirPutRequest = new DirPutRequest(args);

			// Prepare AWS Helper
			AWSS3Helper awsS3Helper = new AWSS3Helper(dirPutRequest.getAwsAccessKey(), dirPutRequest.getAwsSecretKey(), dirPutRequest.getBucket());
			// Prepare Audit Loggers
			AuditUtil procAuditor = AuditUtil.getAuditUtil(dirPutRequest.getLogMode(), Processor.class);
			AuditUtil trackAuditor = AuditUtil.getAuditUtil(dirPutRequest.getLogMode(), Tracker.class);

			Tracker tracker = new Tracker();
			tracker.setAuditUtil(trackAuditor);

			Generator generator = new Generator(tracker);
			// Generate the request queue that contains all the files that are to be moved
			generator.generate(dirPutRequest.getDirPath());

			Processor processor = new Processor(dirPutRequest.getCorePoolSize(), dirPutRequest.getMaxPoolSize(), dirPutRequest.getKeepAliveSecs(), awsS3Helper);
			processor.setAuditUtil(procAuditor);
			// Until the queue is empty, process
			while(!tracker.isEmpty())
			{
				processor.submitPutTask(tracker.get());
			}
			// Wait for the all the tasks to complete
			processor.waitForCompletion();

			procAuditor.commit();
			trackAuditor.commit();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
