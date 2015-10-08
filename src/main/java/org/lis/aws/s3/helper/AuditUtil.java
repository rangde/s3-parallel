package org.lis.aws.s3.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.lis.aws.s3.entity.LogMode;

public class AuditUtil
{
	private static final DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	private LogMode logMode;	
	private BufferedWriter logger;
	
	private AuditUtil() {}
	
	/**
	 * For LogMode : FILE or FILE_CONSOLE
	 */
	private void initLogFile(LogMode logMode, String logFileName) throws IOException
	{
		this.logMode = logMode;
		if(shouldLogToFile())
		{
			File file = new File(logFileName);
			if(!file.exists())
			{
				file.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(file);
			logger = new BufferedWriter(fileWriter);
		}
	}
	
	private boolean shouldLogToFile()
	{
		return LogMode.FILE == logMode || LogMode.FILE_CONSOLE == logMode;
	}
	
	private boolean shouldLogToConsole()
	{
		return LogMode.CONSOLE == logMode || LogMode.FILE_CONSOLE == logMode;
	}
	
	public void log(String message)
	{
		// Make the best effort to write
		try
		{
			String currentTS =  df.format(new Date());
			message = "[" + currentTS + "] : " + message;
			if(shouldLogToFile())
			{
				logger.write(message);
				logger.newLine();
			}
			if(shouldLogToConsole())
			{
				System.out.println(message);
			}
		}
		catch(Exception e)
		{
			// TODO : Use a logger
			e.printStackTrace();
		}
	}
	
	public static AuditUtil getAuditUtil(LogMode logMode, Class<?> clazz) throws IOException
	{
		AuditUtil auditUtil = new AuditUtil();
		auditUtil.logMode = null == logMode ? LogMode.NONE : logMode;
		if(LogMode.FILE == logMode || LogMode.FILE_CONSOLE == logMode)
		{
			auditUtil.initLogFile(logMode, (null == clazz ? "" : clazz.getSimpleName()) + "-"+ UUID.randomUUID().toString() + ".log");
		}
		return auditUtil;
	}
	
	public void commit() throws IOException
	{
		if(null != logger)
		{
			logger.close();
		}
	}
}
