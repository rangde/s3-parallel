package org.lis.aws.s3.command;

import org.lis.aws.s3.entity.DeleteAllRequest;
import org.lis.aws.s3.helper.*;

public class S3DeleteAll implements S3Command
{
    public void process(String[] args)
    {
        try
        {
            DeleteAllRequest deleteAllRequest = new DeleteAllRequest(args);

            // Prepare AWS Helper
            AWSS3Helper awsS3Helper = new AWSS3Helper(deleteAllRequest.getAwsAccessKey(), deleteAllRequest.getAwsSecretKey(), deleteAllRequest.getBucket());
            // Prepare Audit Loggers
            AuditUtil procAuditor = AuditUtil.getAuditUtil(deleteAllRequest.getLogMode(), Processor.class);
            AuditUtil trackAuditor = AuditUtil.getAuditUtil(deleteAllRequest.getLogMode(), Tracker.class);

            Processor processor = new Processor(1, 2, 2000, awsS3Helper);
            processor.setAuditUtil(procAuditor);

            processor.submitDeleteAllTask(deleteAllRequest.getBucket());
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
