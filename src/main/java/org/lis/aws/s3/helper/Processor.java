package org.lis.aws.s3.helper;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.lis.aws.s3.entity.Source;

public class Processor extends AuditAware
{
	private ExecutorService executorService;
	private AWSS3Helper awsS3Helper;

	public Processor(int corePoolSize, int maxPoolSize, int keepAliveSecs, AWSS3Helper awsS3Helper)
	{
		executorService  = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveSecs, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());
		this.awsS3Helper = awsS3Helper;
	}

	public void submitPutTask(final Source source)
	{
		executorService.execute(new Runnable()
		{
			public void run()
			{
				try
				{
					auditUtil.log(String.format("Attempting to transfer %s [%s] to the bucket", source.getFileName(), source.getKeyPath()));
					awsS3Helper.put(new File(source.getAbsolutePath()), source.getKeyPath());
					auditUtil.log(String.format("SUCCESSFULLY added %s [%s] to the bucket", source.getFileName(), source.getKeyPath()));
				}
				catch(Exception e)
				{
					auditUtil.log(String.format("FAILED to add %s [%s] to the bucket - %s", source.getFileName(), source.getKeyPath(), e.getMessage()));
				}
			}
		});
	}

	public void submitDeleteAllTask(final String bucket)
	{
		executorService.execute(new Runnable()
		{
			public void run()
			{
				try
				{
					auditUtil.log(String.format("Attempting to Delete files from bucket [%s]", bucket));
                    awsS3Helper.deleteAllFromBucket(bucket);
                    auditUtil.log(String.format("SUCCESSFULLY deleted ALL from the bucket [%s]", bucket));
				}
				catch(Exception e)
				{
                    auditUtil.log(String.format("FAILED to deleted ALL from the bucket [%s]", bucket));
				}
			}
		});
	}

	public void waitForCompletion() throws InterruptedException
	{
		System.out.println("Waiting for all Tasks to complete.");
		executorService.shutdown();
		executorService.awaitTermination(60, TimeUnit.MINUTES);
		System.out.println("All tasks completed. Exiting now.");
	}
}
